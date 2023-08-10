package sh.casey.subtitler.writer;

import lombok.extern.slf4j.Slf4j;
import sh.casey.subtitler.exception.SubtitleException;
import sh.casey.subtitler.model.SubtitleFile;

import java.io.FileWriter;
import java.io.IOException;

@Slf4j
abstract class BaseSubtitleWriter<T extends SubtitleFile> implements SubtitleWriter<T> {

    @Override
    public void write(T file, String outputPath) {
        try (FileWriter writer = new FileWriter(outputPath)) {
            write(file, writer);
            log.info("Successfully wrote subtitle file to {}", outputPath);
        } catch (IOException e) {
            throw new SubtitleException("Couldn't write subtitle file", e);
        }
    }
}
