package sh.casey.subtitler.application.command.config;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum DualSubConfig {
    FONT("font", String.class),
    SIZE("size", Integer.class),
    BOLD("bold", Boolean.class),
    OUTLINE("outline", Integer.class);

    private final String name;
    private final Class<?> clazz;

    public static DualSubConfig find(String name) {
        for (DualSubConfig config : DualSubConfig.values()) {
            if (config.getName().equalsIgnoreCase(name)) {
                return config;
            }
        }
        return null;
    }
}
