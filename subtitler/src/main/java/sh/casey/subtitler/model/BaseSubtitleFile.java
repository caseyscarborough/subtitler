package sh.casey.subtitler.model;

abstract class BaseSubtitleFile implements SubtitleFile {

    private String path;

    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public String getPath() {
        return path;
    }
}
