package sh.casey.subtitler.filter;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum FilterType {
    STYLE("style", "Filter subtitles with specific Aegisub style names (only available for .ass and .ssa subtitles). Styles should be separated by comma and are case-insensitive, e.g. --filters \"style=op,ed,signs,songs\""),
    TEXT("text", "Filter subtitles with specific text, e.g. --filters \"text=♪♪～\" --mode OMIT");
    private final String name;
    private final String description;

    @Override
    public String toString() {
        return name + " (" + description + ")";
    }
}
