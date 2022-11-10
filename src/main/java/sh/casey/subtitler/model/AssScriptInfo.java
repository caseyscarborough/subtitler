package sh.casey.subtitler.model;

import java.util.ArrayList;
import java.util.List;

public class AssScriptInfo {
    private List<String> comments = new ArrayList<>();
    private String title;
    private String scriptType;
    private String wrapStyle;
    private String playResX;
    private String playResY;
    private String scaledBorderAndShadow;
    private String videoAspectRatio;
    private String videoZoom;
    private String videoPosition;
    private String collisions;

    public List<String> getComments() {
        return comments;
    }

    public void setComments(List<String> comments) {
        this.comments = comments;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getScriptType() {
        return scriptType;
    }

    public void setScriptType(String scriptType) {
        this.scriptType = scriptType;
    }

    public String getWrapStyle() {
        return wrapStyle;
    }

    public void setWrapStyle(String wrapStyle) {
        this.wrapStyle = wrapStyle;
    }

    public String getPlayResX() {
        return playResX;
    }

    public void setPlayResX(String playResX) {
        this.playResX = playResX;
    }

    public String getPlayResY() {
        return playResY;
    }

    public void setPlayResY(String playResY) {
        this.playResY = playResY;
    }

    public String getScaledBorderAndShadow() {
        return scaledBorderAndShadow;
    }

    public void setScaledBorderAndShadow(String scaledBorderAndShadow) {
        this.scaledBorderAndShadow = scaledBorderAndShadow;
    }

    public String getVideoAspectRatio() {
        return videoAspectRatio;
    }

    public void setVideoAspectRatio(String videoAspectRatio) {
        this.videoAspectRatio = videoAspectRatio;
    }

    public String getVideoZoom() {
        return videoZoom;
    }

    public void setVideoZoom(String videoZoom) {
        this.videoZoom = videoZoom;
    }

    public String getVideoPosition() {
        return videoPosition;
    }

    public void setVideoPosition(String videoPosition) {
        this.videoPosition = videoPosition;
    }

    public String getCollisions() {
        return collisions;
    }

    public void setCollisions(String collisions) {
        this.collisions = collisions;
    }
}
