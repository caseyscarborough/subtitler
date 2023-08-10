package sh.casey.subtitler.model;

import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

// See this document for information on ASS subtitle tags:
// http://docs.aegisub.org/3.2/ASS_Tags/
@NoArgsConstructor
public class AssSubtitleFile extends BaseSubtitleFile {

    // Script Info
    private AssScriptInfo scriptInfo;

    // Styles
    private List<String> stylesFormatOrder = new ArrayList<>();
    private List<AssStyle> styles = new ArrayList<>();

    // Events
    private List<String> eventsFormatOrder = new ArrayList<>();
    private List<AssDialogue> dialogues = new ArrayList<>();
    private AssStyleVersion styleVersion;

    // Copy Constructor
    public AssSubtitleFile(AssSubtitleFile file) {
        setEventsFormatOrder(new ArrayList<>(file.getEventsFormatOrder()));
        setStylesFormatOrder(new ArrayList<>(file.getStylesFormatOrder()));
        setDialogues(file.getDialogues().stream().map(AssDialogue::new).collect(Collectors.toList()));
        setStyles(file.getStyles().stream().map(AssStyle::new).collect(Collectors.toList()));
        setStyleVersion(file.getStyleVersion());
        setScriptInfo(new AssScriptInfo(file.getScriptInfo()));
    }

    public AssScriptInfo getScriptInfo() {
        return scriptInfo;
    }

    public void setScriptInfo(AssScriptInfo scriptInfo) {
        this.scriptInfo = scriptInfo;
    }

    public List<String> getStylesFormatOrder() {
        return stylesFormatOrder;
    }

    public void setStylesFormatOrder(final List<String> stylesFormatOrder) {
        this.stylesFormatOrder = stylesFormatOrder;
    }

    public List<AssStyle> getStyles() {
        return styles;
    }

    public void setStyles(final List<AssStyle> styles) {
        this.styles = styles;
    }

    public List<String> getEventsFormatOrder() {
        return eventsFormatOrder;
    }

    public void setEventsFormatOrder(final List<String> eventsFormatOrder) {
        this.eventsFormatOrder = eventsFormatOrder;
    }

    public List<AssDialogue> getDialogues() {
        return dialogues;
    }

    public void setDialogues(final List<AssDialogue> dialogues) {
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

    public AssStyleVersion getStyleVersion() {
        return styleVersion;
    }

    public void setStyleVersion(AssStyleVersion styleVersion) {
        this.styleVersion = styleVersion;
    }
}
