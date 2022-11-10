package sh.casey.subtitler.filter;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum FilterType {
    FILTER_STYLE("fs", "filter-style", "Filter the subtitle dialogues by the name of the style (only works for SSA/ASS subtitles).");
    private final String shortOpt;
    private final String longOpt;
    private final String description;
}
