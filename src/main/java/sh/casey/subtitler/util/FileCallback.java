package sh.casey.subtitler.util;

import java.io.File;

public interface FileCallback<T> {

    T callback(File file);
}
