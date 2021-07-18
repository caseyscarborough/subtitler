package sh.casey.subtitler.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

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
import java.nio.charset.StandardCharsets;
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

@Slf4j
public class FileUtils {

    public static void mkdirs(final String path) {
        new File(path).mkdirs();
    }

    public static List<String> ls(final String path) {
        final File file = new File(path);
        if (!file.isDirectory()) {
            throw new RuntimeException(path + " is not a directory");
        }

        final File[] files = file.listFiles();
        if (files == null) {
            return new ArrayList<>();
        }
        return Arrays.stream(files).map(File::getName).collect(Collectors.toList());
    }

    public static String getExtension(final File file) {
        return getExtension(file.getPath());
    }

    public static String getExtension(final String path) {
        return path.substring(path.lastIndexOf('.'));
    }

    public static String getFileNameWithoutExtension(final File file) {
        return getFileNameWithoutExtension(file.getName());
    }

    public static String getFileNameWithoutExtension(final String file) {
        return file.substring(0, file.lastIndexOf('.'));
    }

    public static void copyFile(final String path1, final String path2) {
        log.info("Copying file '" + path1 + "' to '" + path2 + "'");
        try {
            Files.copy(Paths.get(path1), Paths.get(path2));
        } catch (final IOException e) {
            log.error("Could not copy file from '" + path1 + "' to '" + path2 + "'");
        }
    }

    public static List<String> getFileNames(final String directory) {
        return getFileNames(directory, (dir, name) -> true);
    }

    public static List<String> getFileNames(final String directory, final FilenameFilter filter) {
        if (!exists(directory)) {
            throw new RuntimeException("Directory does not exist!");
        }

        final File file = new File(directory);
        if (!file.isDirectory()) {
            throw new RuntimeException("Path is not a directory!");
        }

        final File[] files = file.listFiles(filter);
        if (files == null) {
            throw new RuntimeException("Could not get file list!");
        }

        return Arrays.stream(files)
            .filter(File::isFile)
            .map(File::getName)
            .collect(Collectors.toList());
    }

    public static List<String> getFileNames(final String directory, final String extension) {
        return getFileNames(directory, (dir, name) -> name.endsWith("." + extension));
    }

    public static String md5Checksum(final String filePath) {
        try (final InputStream is = new FileInputStream(filePath)) {
            return DigestUtils.md5Hex(is);
        } catch (final FileNotFoundException e) {
            throw new RuntimeException("Couldn't find file " + filePath);
        } catch (final IOException e) {
            throw new RuntimeException("Couldn't read file " + filePath);
        }
    }

    public static boolean exists(final String filePath) {
        return new File(filePath).exists();
    }

    public static String readFile(final String filePath) {
        log.debug("Reading file " + filePath);
        try (final FileInputStream inputStream = new FileInputStream(filePath)) {
            return IOUtils.toString(inputStream, StandardCharsets.UTF_8);
        } catch (final FileNotFoundException e) {
            log.error("Couldn't find file " + filePath, e);
            throw new RuntimeException("Couldn't find file " + filePath);
        } catch (final IOException e) {
            log.error("An IO error occurred reading file " + filePath, e);
            throw new RuntimeException("Couldn't read file " + filePath);
        }
    }

    public static void writeFile(final String filePath, final String contents, final OpenOption... options) {
        log.debug("Writing file " + filePath);
        if (filePath.contains("/")) {
            final String dir = filePath.substring(0, filePath.lastIndexOf("/"));
            new File(dir).mkdirs();
        }
        try {
            Files.write(Paths.get(filePath), Collections.singleton(contents), StandardCharsets.UTF_8, options);
        } catch (final IOException e) {
            throw new RuntimeException("Could not write to file path " + filePath, e);
        }
        log.debug("Done writing file " + filePath);
    }

    public static void writeFileIfNotExists(final String filePath, final String contents, final OpenOption... options) {
        if (!new File(filePath).exists()) {
            writeFile(filePath, contents, options);
        }
    }

    public static void deleteFile(final String filePath) {
        log.debug("Deleting file " + filePath);
        try {
            Files.delete(Paths.get(filePath));
        } catch (final NoSuchFileException e) {
            log.debug("File didn't exist. Continuing.");
        } catch (final IOException e) {
            throw new RuntimeException("Couldn't delete file path " + filePath, e);
        }
    }

    public static <T> T doWithTempFile(final String url, final FileCallback<T> callback) {
        final String tempFile = downloadTempFile(url);
        final File file = new File(tempFile);
        final T result = callback.callback(file);
        final boolean deleted = file.delete();
        if (!deleted) {
            log.warn("Could not delete temp file " + file.getAbsolutePath());
        }
        return result;
    }

    public static void doWithTempFile(final String url, final VoidFileCallback callback) {
        final String tempFile = downloadTempFile(url);
        final File file = new File(tempFile);
        callback.callback(file);
        final boolean deleted = file.delete();
        if (!deleted) {
            log.warn("Could not delete temp file " + file.getAbsolutePath());
        }
    }

    public static String downloadTempFile(final String url) {
        final String filename = UUID.randomUUID().toString();
        return downloadTempFile(url, filename);
    }

    public static String downloadTempFile(final String url, final String filename) {
        return downloadTempFile(url, filename, 0);
    }

    public static String downloadTempFile(final String url, final String filename, final int retries) {
        final String output = getTmpDir() + filename;
        final boolean success = downloadFile(url, output, retries);
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

    public static boolean downloadFileIfNotExists(final String url, final String filename, final int retries) {
        return downloadFileIfNotExists(url, filename, retries, false);
    }

    public static boolean downloadFileIfNotExists(final String url, final String filename, final int retries, final boolean ignoreErrors) {
        if (filename == null) {
            return false;
        }

        if (!new File(filename).exists()) {
            return downloadFile(url, filename, retries, ignoreErrors);
        }
        return true;
    }

    public static boolean downloadFile(final String url, final String filename, final int retries) {
        return downloadFile(url, filename, retries, false);
    }

    public static boolean downloadFile(final String url, final String filename, final int retries, final boolean ignoreErrors) {
        if (StringUtils.isBlank(url)) {
            return false;
        }

        final Timer timer = new Timer();
        timer.start();

        log.debug("Downloading " + url + " to file " + filename);
        try {
            final URL website = new URL(url);
            final URLConnection connection = website.openConnection();
            connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/71.0.3578.98 Safari/537.36");
            final InputStream inputStream = connection.getInputStream();
            final ReadableByteChannel rbc = Channels.newChannel(inputStream);
            final FileOutputStream fos = new FileOutputStream(filename);
            fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
            fos.close();
            log.debug("Download complete for file " + filename + " in " + timer.stop() + "ms");
            return true;
        } catch (final IOException e) {
            log.error("Couldn't download from " + url, e);
            if (retries > 0) {
                log.debug("Retrying...");
                return downloadFile(url, filename, retries - 1);
            }
            if (ignoreErrors) {
                return false;
            }
            throw new RuntimeException("Couldn't download file from URL " + url, e);
        }
    }

    public static boolean downloadFile(final String url, final String filename) {
        return downloadFile(url, filename, 0);
    }

    public static boolean downloadFileIfNotExists(final String url, final String filename) {
        return downloadFileIfNotExists(url, filename, 0);
    }

    public static boolean renameFile(final String start, final String end) {
        log.debug("Renaming file " + start + " to " + end);
        try {
            Files.move(Paths.get(start), Paths.get(end));
            return true;
        } catch (final IOException e) {
            log.error("Couldn't rename file from " + start + " to " + end, e);
            return false;
        }
    }

    public static int getFileSizeFromUrl(final String url) {
        URLConnection conn = null;
        try {
            conn = new URL(url).openConnection();
            if (conn instanceof HttpURLConnection) {
                ((HttpURLConnection) conn).setRequestMethod("HEAD");
            }
            conn.getInputStream();
            return conn.getContentLength();
        } catch (final IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (conn instanceof HttpURLConnection) {
                ((HttpURLConnection) conn).disconnect();
            }
        }
    }

    public static boolean createFile(final String path) {
        try {
            return new File(path).createNewFile();
        } catch (final IOException e) {
            log.error("Could not create file at path " + path, e);
            throw new RuntimeException("Could not create file at path " + path, e);
        }
    }
}
