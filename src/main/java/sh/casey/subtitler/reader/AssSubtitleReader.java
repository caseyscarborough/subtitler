package sh.casey.subtitler.reader;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import sh.casey.subtitler.exception.SubtitleException;
import sh.casey.subtitler.model.AssDialogue;
import sh.casey.subtitler.model.AssScriptInfo;
import sh.casey.subtitler.model.AssStyle;
import sh.casey.subtitler.model.AssSubtitleFile;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static sh.casey.subtitler.config.Constants.BYTE_ORDER_MARK;

@Slf4j
public class AssSubtitleReader implements SubtitleReader<AssSubtitleFile> {

    @Override
    public AssSubtitleFile read(final String filename) {
        log.info("Reading subtitle file: " + filename);
        Validate.notBlank(filename);
        final AssSubtitleFile file = new AssSubtitleFile();
        final AssScriptInfo scriptInfo = new AssScriptInfo();
        int lineCounter = 0;
        final BufferedReader br;
        try {
            br = new BufferedReader(new FileReader(filename));
        } catch (final FileNotFoundException e) {
            throw new SubtitleException("Could not read subtitle file " + filename, e);
        }

        try {
            log.debug("Reading first line...");
            lineCounter++;
            String line = br.readLine();
            int dialogueCounter = 0;
            while (line != null) {
                line = line.trim().replace(BYTE_ORDER_MARK, "");
                if (line.startsWith("[")) {
                    if (line.equalsIgnoreCase("[Script Info]")) {
                        log.debug("Found script info...");
                        lineCounter++;
                        line = br.readLine();
                        while (line != null && !line.startsWith("[")) {
                            line = line.trim();
                            final String value = getValue(line);
                            if (line.startsWith("Title:")) {
                                scriptInfo.setTitle(value);
                            } else if (line.startsWith("Collisions: ")) {
                                scriptInfo.setCollisions(value);
                            } else if (line.startsWith("ScriptType:")) {
                                scriptInfo.setScriptType(value);
                            } else if (line.startsWith("WrapStyle:")) {
                                scriptInfo.setWrapStyle(value);
                            } else if (line.startsWith("PlayResX:")) {
                                scriptInfo.setPlayResX(value);
                            } else if (line.startsWith("PlayResY:")) {
                                scriptInfo.setPlayResY(value);
                            } else if (line.startsWith("ScaledBorderAndShadow:")) {
                                scriptInfo.setScaledBorderAndShadow(value);
                            } else if (line.startsWith("Video Aspect Ratio")) {
                                scriptInfo.setVideoAspectRatio(value);
                            } else if (line.startsWith("Video Zoom")) {
                                scriptInfo.setVideoZoom(value);
                            } else if (line.startsWith("Video Position")) {
                                scriptInfo.setVideoPosition(value);
                            } else if (line.startsWith(";")) {
                                scriptInfo.getComments().add(line);
                            } else if (StringUtils.isNotBlank(line)) {
                                log.info("Implementation has not been created for the following line in [Script Info] section: " + line);
                            }

                            lineCounter++;
                            line = br.readLine();
                        }
                        file.setScriptInfo(scriptInfo);
                    } else if (line.equalsIgnoreCase("[V4+ Styles]")) {
                        log.debug("Found styles...");
                        lineCounter++;
                        line = br.readLine().trim();

                        while (!line.startsWith("[")) {
                            final String value = getValue(line);
                            if (line.startsWith("Format:")) {
                                final List<String> formatOrder = Arrays.stream(value.split(",")).map(String::trim).collect(Collectors.toList());
                                file.setStylesFormatOrder(formatOrder);
                            } else if (line.startsWith("Style:")) {
                                if (file.getStylesFormatOrder().isEmpty()) {
                                    throw new SubtitleException(".ass subtitle is in the incorrect format. Did not find 'Format' field before 'Style' field in [V4+ Styles] section. See line number: " + lineCounter);
                                }

                                final String[] styles = value.split(",");
                                if (styles.length != file.getStylesFormatOrder().size()) {
                                    throw new SubtitleException(".ass subtitle is in the incorrect format. More styles were listed than were included in the format header. See line number: " + lineCounter);
                                }

                                final AssStyle style = new AssStyle();
                                for (int i = 0; i < file.getStylesFormatOrder().size(); i++) {
                                    final String type = file.getStylesFormatOrder().get(i);
                                    final String styleValue = styles[i].trim();

                                    if (type.equals("Name")) {
                                        style.setName(styleValue);
                                    } else if (type.equals("Fontname")) {
                                        style.setFontName(styleValue);
                                    } else if (type.equals("Fontsize")) {
                                        style.setFontSize(styleValue);
                                    } else if (type.equals("PrimaryColour")) {
                                        style.setPrimaryColor(styleValue);
                                    } else if (type.equals("SecondaryColour")) {
                                        style.setSecondaryColor(styleValue);
                                    } else if (type.equals("OutlineColour")) {
                                        style.setOutlineColor(styleValue);
                                    } else if (type.equals("BackColour")) {
                                        style.setBackColor(styleValue);
                                    } else if (type.equals("Bold")) {
                                        style.setBold(styleValue);
                                    } else if (type.equals("Italic")) {
                                        style.setItalic(styleValue);
                                    } else if (type.equals("Underline")) {
                                        style.setUnderline(styleValue);
                                    } else if (type.equals("StrikeOut")) {
                                        style.setStrikeOut(styleValue);
                                    } else if (type.equals("ScaleX")) {
                                        style.setScaleX(styleValue);
                                    } else if (type.equals("ScaleY")) {
                                        style.setScaleY(styleValue);
                                    } else if (type.equals("Spacing")) {
                                        style.setSpacing(styleValue);
                                    } else if (type.equals("Angle")) {
                                        style.setAngle(styleValue);
                                    } else if (type.equals("BorderStyle")) {
                                        style.setBorderStyle(styleValue);
                                    } else if (type.equals("Outline")) {
                                        style.setOutline(styleValue);
                                    } else if (type.equals("Shadow")) {
                                        style.setShadow(styleValue);
                                    } else if (type.equals("Alignment")) {
                                        style.setAlignment(styleValue);
                                    } else if (type.equals("MarginL")) {
                                        style.setMarginL(styleValue);
                                    } else if (type.equals("MarginR")) {
                                        style.setMarginR(styleValue);
                                    } else if (type.equals("MarginV")) {
                                        style.setMarginV(styleValue);
                                    } else if (type.equals("Encoding")) {
                                        style.setEncoding(styleValue);
                                    }
                                }
                                file.getStyles().add(style);
                            }
                            lineCounter++;
                            line = br.readLine().trim();
                        }
                    } else if (line.equalsIgnoreCase("[Events]")) {
                        log.debug("Found events...");
                        lineCounter++;
                        line = br.readLine().trim();

                        while (line != null && !line.startsWith("[")) {
                            final String value = getValue(line);
                            if (line.startsWith("Format:")) {
                                final List<String> formatOrder = Arrays.stream(value.split(",")).map(String::trim).collect(Collectors.toList());
                                file.setEventsFormatOrder(formatOrder);
                            } else if (line.startsWith("Dialogue:") || line.startsWith("Comment:")) {
                                if (file.getEventsFormatOrder().isEmpty()) {
                                    throw new SubtitleException(".ass subtitle is in the incorrect format. Did not find 'Format' field before 'Dialogue' field in [Events] section. See line number: " + lineCounter);
                                }

                                final String[] dialogues = value.split(",");
                                // This usually means that the dialogue had some commas in it.
                                // This will fix those issues.
                                if (dialogues.length > file.getEventsFormatOrder().size()) {
                                    final StringBuilder sb = new StringBuilder();
                                    boolean first = true;
                                    for (int i = file.getEventsFormatOrder().size() - 1; i < dialogues.length; i++) {
                                        if (!first) {
                                            sb.append(",");
                                        }
                                        sb.append(dialogues[i]);
                                        first = false;
                                    }
                                    dialogues[file.getEventsFormatOrder().size() - 1] = sb.toString();
                                }

                                if (dialogues.length >= file.getEventsFormatOrder().size()) {
                                    // This fixes an issue where the final comma or colon gets split off
                                    if (line.endsWith(",")) {
                                        dialogues[file.getEventsFormatOrder().size() - 1] = dialogues[file.getEventsFormatOrder().size() - 1] + ",";
                                    } else if (line.endsWith(":")) {
                                        dialogues[file.getEventsFormatOrder().size() - 1] = dialogues[file.getEventsFormatOrder().size() - 1] + ":";
                                    }
                                }

                                final AssDialogue dialogue = new AssDialogue();
                                dialogue.setComment(line.startsWith("Comment:"));
                                for (int i = 0; i < file.getEventsFormatOrder().size() && i < dialogues.length; i++) {
                                    final String type = file.getEventsFormatOrder().get(i);
                                    final String dialogueValue = dialogues[i].trim();

                                    if (type.equals("Layer")) {
                                        dialogue.setLayer(dialogueValue);
                                    } else if (type.equals("Start")) {
                                        dialogue.setStart(dialogueValue);
                                    } else if (type.equals("End")) {
                                        dialogue.setEnd(dialogueValue);
                                    } else if (type.equals("Style")) {
                                        dialogue.setStyle(dialogueValue);
                                    } else if (type.equals("Actor") || type.equals("Name")) {
                                        dialogue.setActor(dialogueValue);
                                    } else if (type.equals("MarginL")) {
                                        dialogue.setMarginL(dialogueValue);
                                    } else if (type.equals("MarginR")) {
                                        dialogue.setMarginR(dialogueValue);
                                    } else if (type.equals("MarginV")) {
                                        dialogue.setMarginV(dialogueValue);
                                    } else if (type.equals("Effect")) {
                                        dialogue.setEffect(dialogueValue);
                                    } else if (type.equals("Text")) {
                                        dialogue.setText(dialogueValue);
                                    }
                                }
                                dialogue.setNumber(++dialogueCounter);
                                file.getDialogues().add(dialogue);
                            }
                            lineCounter++;
                            line = br.readLine();
                        }
                    } else {
                        // We don't support this type.
                        log.info("Found type " + line + " which is not supported. Skipping this section.");
                        lineCounter++;
                        line = br.readLine();
                    }
                } else {
                    // This is a line in a section we do not support. Skipping.
                    lineCounter++;
                    line = br.readLine();
                }
            }
            log.debug("Read " + lineCounter + " lines from file " + filename);
        } catch (final NullPointerException e) {
            log.warn("Unexpected end of file after reading " + lineCounter + " lines for file " + filename, e);
        } catch (final IOException e) {
            throw new SubtitleException("An error occurred reading file " + filename, e);
        } finally {
            try {
                br.close();
            } catch (final IOException e) {
                log.error("Could not close stream", e);
            }
        }

        log.debug("Found " + file.getSubtitles().size() + " subtitles.");
        return file;
    }

    private String getValue(final String line) {
        final String[] split = line.split(":");
        if (split.length > 1) {
            return String.join(":", Arrays.asList(split)).substring(split[0].length() + 1).trim();
        }
        return split[0];
    }
}
