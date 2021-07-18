package sh.casey.subtitler.util;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.OpenOption;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class FileUtils {

    private static final Logger LOGGER = Logger.getLogger(FileUtils.class);

    public static void mkdirs(String path) {
        new File(path).mkdirs();
    }

    public static List<String> ls(String path) {
        File file = new File(path);
        if (!file.isDirectory()) {
            throw new RuntimeException(path + " is not a directory");
        }

        File[] files = file.listFiles();
        if (files == null) {
            return new ArrayList<>();
        }
        return Arrays.stream(files).map(File::getName).collect(Collectors.toList());
    }

    public static String getExtension(File file) {
        return getExtension(file.getPath());
    }

    public static String getExtension(String path) {
        return path.substring(path.lastIndexOf('.'));
    }

    public static String getFileNameWithoutExtension(File file) {
        return getFileNameWithoutExtension(file.getName());
    }

    public static String getFileNameWithoutExtension(String file) {
        return file.substring(0, file.lastIndexOf('.'));
    }

    public static void copyFile(String path1, String path2) {
        LOGGER.info("Copying file '" + path1 + "' to '" + path2 + "'");
        try {
            Files.copy(Paths.get(path1), Paths.get(path2));
        } catch (IOException e) {
            LOGGER.error("Could not copy file from '" + path1 + "' to '" + path2 + "'");
        }
    }

    public static List<String> getFileNames(String directory) {
        return getFileNames(directory, (dir, name) -> true);
    }

    public static List<String> getFileNames(String directory, FilenameFilter filter) {
        if (!exists(directory)) {
            throw new RuntimeException("Directory does not exist!");
        }

        File file = new File(directory);
        if (!file.isDirectory()) {
            throw new RuntimeException("Path is not a directory!");
        }

        File[] files = file.listFiles(filter);
        if (files == null) {
            throw new RuntimeException("Could not get file list!");
        }

        return Arrays.stream(files)
            .filter(File::isFile)
            .map(File::getName)
            .collect(Collectors.toList());
    }

    public static List<String> getFileNames(String directory, String extension) {
        return getFileNames(directory, (dir, name) -> name.endsWith("." + extension));
    }

    public static String md5Checksum(String filePath) {
        try (InputStream is = new FileInputStream(new File(filePath))) {
            return DigestUtils.md5Hex(is);
        } catch (FileNotFoundException e) {
            throw new RuntimeException("Couldn't find file " + filePath);
        } catch (IOException e) {
            throw new RuntimeException("Couldn't read file " + filePath);
        }
    }

    public static boolean exists(String filePath) {
        return new File(filePath).exists();
    }

    public static String readFile(String filePath) {
        LOGGER.debug("Reading file " + filePath);
        try (FileInputStream inputStream = new FileInputStream(filePath)) {
            return IOUtils.toString(inputStream, Charset.forName("UTF-8"));
        } catch (FileNotFoundException e) {
            LOGGER.error("Couldn't find file " + filePath, e);
            throw new RuntimeException("Couldn't find file " + filePath);
        } catch (IOException e) {
            LOGGER.error("An IO error occurred reading file " + filePath, e);
            throw new RuntimeException("Couldn't read file " + filePath);
        }
    }

    public static void writeFile(String filePath, String contents, OpenOption... options) {
        LOGGER.debug("Writing file " + filePath);
        if (filePath.contains("/")) {
            String dir = filePath.substring(0, filePath.lastIndexOf("/"));
            new File(dir).mkdirs();
        }
        try {
            Files.write(Paths.get(filePath), Collections.singleton(contents), Charset.forName("UTF-8"), options);
        } catch (IOException e) {
            throw new RuntimeException("Could not write to file path " + filePath, e);
        }
        LOGGER.debug("Done writing file " + filePath);
    }

    public static void writeFileIfNotExists(String filePath, String contents, OpenOption... options) {
        if (!new File(filePath).exists()) {
            writeFile(filePath, contents, options);
        }
    }

    public static void deleteFile(String filePath) {
        LOGGER.debug("Deleting file " + filePath);
        try {
            Files.delete(Paths.get(filePath));
        } catch (NoSuchFileException e) {
            LOGGER.debug("File didn't exist. Continuing.");
        } catch (IOException e) {
            throw new RuntimeException("Couldn't delete file path " + filePath, e);
        }
    }

    public static <T> T doWithTempFile(String url, FileCallback<T> callback) {
        String tempFile = downloadTempFile(url);
        File file = new File(tempFile);
        T result = callback.callback(file);
        boolean deleted = file.delete();
        if (!deleted) {
            LOGGER.warn("Could not delete temp file " + file.getAbsolutePath());
        }
        return result;
    }

    public static void doWithTempFile(String url, VoidFileCallback callback) {
        String tempFile = downloadTempFile(url);
        File file = new File(tempFile);
        callback.callback(file);
        boolean deleted = file.delete();
        if (!deleted) {
            LOGGER.warn("Could not delete temp file " + file.getAbsolutePath());
        }
    }

    public static String downloadTempFile(String url) {
        final String filename = UUID.randomUUID().toString();
        return downloadTempFile(url, filename);
    }

    public static String downloadTempFile(String url, String filename) {
        return downloadTempFile(url, filename, 0);
    }

    public static String downloadTempFile(String url, String filename, int retries) {
        final String output = getTmpDir() + filename;
        boolean success = downloadFile(url, output, retries);
        if (success) {
            return output;
        }
        return null;
    }

    public static String getTmpDir() {
        String tempDir = System.getProperty("java.io.tmpdir");
        if (!tempDir.endsWith("/")) {
            tempDir += "/";
        }
        return tempDir;
    }

    public static boolean downloadFileIfNotExists(String url, String filename, int retries) {
        return downloadFileIfNotExists(url, filename, retries, false);
    }

    public static boolean downloadFileIfNotExists(String url, String filename, int retries, boolean ignoreErrors) {
        if (filename == null) {
            return false;
        }

        if (!new File(filename).exists()) {
            return downloadFile(url, filename, retries, ignoreErrors);
        }
        return true;
    }

    public static boolean downloadFile(String url, String filename, int retries) {
        return downloadFile(url, filename, retries, false);
    }

    public static boolean downloadFile(String url, String filename, int retries, boolean ignoreErrors) {
        if (StringUtils.isBlank(url)) {
            return false;
        }

        Timer timer = new Timer();
        timer.start();

        LOGGER.debug("Downloading " + url + " to file " + filename);
        try {
            URL website = new URL(url);
            URLConnection connection = website.openConnection();
            connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/71.0.3578.98 Safari/537.36");
            InputStream inputStream = connection.getInputStream();
            ReadableByteChannel rbc = Channels.newChannel(inputStream);
            FileOutputStream fos = new FileOutputStream(filename);
            fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
            fos.close();
            LOGGER.debug("Download complete for file " + filename + " in " + timer.stop() + "ms");
            return true;
        } catch (IOException e) {
            LOGGER.error("Couldn't download from " + url, e);
            if (retries > 0) {
                LOGGER.debug("Retrying...");
                return downloadFile(url, filename, retries - 1);
            }
            if (ignoreErrors) {
                return false;
            }
            throw new RuntimeException("Couldn't download file from URL " + url, e);
        }
    }

    public static boolean downloadFile(String url, String filename) {
        return downloadFile(url, filename, 0);
    }

    public static boolean downloadFileIfNotExists(String url, String filename) {
        return downloadFileIfNotExists(url, filename, 0);
    }

    public static boolean renameFile(String start, String end) {
        LOGGER.debug("Renaming file " + start + " to " + end);
        try {
            Files.move(Paths.get(start), Paths.get(end));
            return true;
        } catch (IOException e) {
            LOGGER.error("Couldn't rename file from " + start + " to " + end, e);
            return false;
        }
    }

    public static int getFileSizeFromUrl(String url) {
        URLConnection conn = null;
        try {
            conn = new URL(url).openConnection();
            if (conn instanceof HttpURLConnection) {
                ((HttpURLConnection) conn).setRequestMethod("HEAD");
            }
            conn.getInputStream();
            return conn.getContentLength();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (conn instanceof HttpURLConnection) {
                ((HttpURLConnection) conn).disconnect();
            }
        }
    }

    public static boolean createFile(String path) {
        try {
            return new File(path).createNewFile();
        } catch (IOException e) {
            LOGGER.error("Could not create file at path " + path, e);
            throw new RuntimeException("Could not create file at path " + path, e);
        }
    }
}
