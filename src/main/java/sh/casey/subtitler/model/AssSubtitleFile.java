package sh.casey.subtitler.model;

import java.util.ArrayList;
import java.util.List;

// See this document for information on ASS subtitle tags:
// http://docs.aegisub.org/3.2/ASS_Tags/
public class AssSubtitleFile implements SubtitleFile {

    // Script Info
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

    // Styles
    private List<String> stylesFormatOrder = new ArrayList<>();
    private List<AssStyle> styles = new ArrayList<>();

    // Events
    private List<String> eventsFormatOrder = new ArrayList<>();
    private List<AssDialogue> dialogues = new ArrayList<>();

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

    public List<String> getStylesFormatOrder() {
        return stylesFormatOrder;
    }

    public void setStylesFormatOrder(List<String> stylesFormatOrder) {
        this.stylesFormatOrder = stylesFormatOrder;
    }

    public List<AssStyle> getStyles() {
        return styles;
    }

    public void setStyles(List<AssStyle> styles) {
        this.styles = styles;
    }

    public List<String> getEventsFormatOrder() {
        return eventsFormatOrder;
    }

    public void setEventsFormatOrder(List<String> eventsFormatOrder) {
        this.eventsFormatOrder = eventsFormatOrder;
    }

    public List<AssDialogue> getDialogues() {
        return dialogues;
    }

    public void setDialogues(List<AssDialogue> dialogues) {
        this.dialogues = dialogues;
    }

    @Override
    public List<AssDialogue> getSubtitles() {
        return getDialogues();
    }

    @Override
    public SubtitleType getType() {
        return SubtitleType.ASS;
    }
}
