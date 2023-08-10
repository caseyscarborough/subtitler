package sh.casey.subtitler.filter;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum FilterMode {
    RETAIN("remove all filters except the ones specified"),
    OMIT("remove only the filters specified");

    private final String description;

    @Override
    public String toString() {
        return name() + " (" + description + ")";
    }
}
