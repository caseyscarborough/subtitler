package sh.casey.subtitler.application.command.config;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import sh.casey.subtitler.model.AssStyle;

import java.util.function.BiConsumer;

@Getter
@RequiredArgsConstructor
public enum StyleConfig {
    FONT("font", String.class, AssStyle::setFontName),
    SIZE("size", Integer.class, AssStyle::setFontSize),
    BOLD("bold", Boolean.class, AssStyle::setBold),
    OUTLINE("outline", Integer.class, AssStyle::setOutline);

    private final String name;
    private final Class<?> clazz;
    private final BiConsumer<AssStyle, String> consumer;

    public static StyleConfig find(String name) {
        for (StyleConfig config : StyleConfig.values()) {
            if (config.getName().equalsIgnoreCase(name)) {
                return config;
            }
        }
        return null;
    }
}
