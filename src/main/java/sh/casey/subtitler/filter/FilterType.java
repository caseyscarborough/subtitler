package sh.casey.subtitler.filter;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum FilterType {
    STYLE("Filter out subtitles with specific Aegisub style names (only available for .ass and .ssa subtitles). Styles should be separated by comma and are case-insensitive, e.g. --styles \"op,ed,signs,songs\"");
    private final String description;

    @Override
    public String toString() {
        return name() + " (" + description + ")";
    }
}
