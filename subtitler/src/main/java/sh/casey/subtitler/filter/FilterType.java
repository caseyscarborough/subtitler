package sh.casey.subtitler.filter;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import sh.casey.subtitler.model.SubtitleType;

import java.util.Arrays;
import java.util.List;

import static sh.casey.subtitler.model.SubtitleType.ASS;
import static sh.casey.subtitler.model.SubtitleType.SRT;
import static sh.casey.subtitler.model.SubtitleType.SSA;

@Getter
@RequiredArgsConstructor
public enum FilterType {
    STYLE("style", Arrays.asList(ASS, ASS), Multiplicity.MANY,"Filter subtitles with specific Aegisub style names (only available for .ass and .ssa subtitles). Styles should be separated by comma and are case-insensitive, e.g. \"style=op,ed,signs,songs\" (filter dialogues with op, ed, signs, or song as the style)"),
    TEXT("text", Arrays.asList(ASS, SSA, SRT), Multiplicity.MANY, "Filter subtitles with specific text, e.g. \"text=♪♪～\" --mode OMIT (filter subtitles where the text is exactly ♪♪～)"),
    AFTER("after", Arrays.asList(ASS, SSA, SRT), Multiplicity.ONE, "Filter subtitles after a specific time, e.g. \"after=00:30:00.000\" --mode OMIT (filter subtitles after the 30 minute mark)"),
    BEFORE("before", Arrays.asList(ASS, SSA, SRT), Multiplicity.ONE ,"Filter subtitles before a specific time, e.g. \"before=00:01:00.000\" --mode OMIT (filter subtitles before the 1 minute mark)"),
    MATCHES("matches", Arrays.asList(ASS, SSA, SRT), Multiplicity.ONE, "Filter subtitles based on a regex, e.g. \"matches=[A-z0-9]\" (filter subtitles that are alphanumeric)");

    private final String name;
    private final List<SubtitleType> supportedTypes;
    private final Multiplicity multiplicity;
    private final String description;

    public static FilterType findByName(String name) {
        return Arrays.stream(FilterType.values())
            .filter(f -> f.getName().equalsIgnoreCase(name))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("Could not find Filter Type with name '" + name + "'"));
    }

    @Override
    public String toString() {
        return "\"" + name + "\" - " + description;
    }

    public enum Multiplicity {
        ONE,
        MANY,
    }
}
