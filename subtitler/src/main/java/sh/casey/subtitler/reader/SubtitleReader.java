package sh.casey.subtitler.reader;

import sh.casey.subtitler.model.SubtitleFile;

import java.io.InputStream;


public interface SubtitleReader<F extends SubtitleFile> {

    F read(String filepath);

    F read(InputStream inputStream, String filepath);

}

