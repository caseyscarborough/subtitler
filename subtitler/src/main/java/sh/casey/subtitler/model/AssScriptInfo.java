package sh.casey.subtitler.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AssScriptInfo {
    private final List<String> comments = new ArrayList<>();
    private final List<String> attributes = new ArrayList<>();

    public AssScriptInfo() {
    }

    public AssScriptInfo(AssScriptInfo info) {
        this.comments.addAll(info.getComments());
        this.attributes.addAll(info.getAttributes());
    }

    public List<String> getComments() {
        return comments;
    }

    public List<String> getAttributes() {
        return attributes;
    }

    public String getTitle() {
        return getValue("Title");
    }

    public String getScriptType() {
        return getValue("ScriptType");
    }

    public String getWrapStyle() {
        return getValue("WrapStyle");
    }

    public String getPlayResX() {
        return getValue("PlayResX");
    }

    public String getPlayResY() {
        return getValue("PlayResY");
    }

    public String getScaledBorderAndShadow() {
        return getValue("ScaledBorderAndShadow");
    }

    public String getVideoAspectRatio() {
        return getValue("VideoAspectRatio");
    }

    public String getVideoZoom() {
        return getValue("VideoZoom");
    }

    public String getVideoPosition() {
        return getValue("VideoPosition");
    }

    public String getCollisions() {
        return getValue("Collisions");
    }

    private String getValue(String value) {
        return attributes.stream().filter(a -> a.startsWith(value)).map(this::parseValue).findFirst().orElse("");
    }

    private String parseValue(final String line) {
        final String[] split = line.split(":");
        if (split.length > 1) {
            return String.join(":", Arrays.asList(split)).substring(split[0].length() + 1).trim();
        }
        return split[0];
    }
}
