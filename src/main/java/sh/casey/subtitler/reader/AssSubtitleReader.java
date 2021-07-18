package sh.casey.subtitler.reader;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.apache.log4j.Logger;
import sh.casey.subtitler.exception.SubtitleException;
import sh.casey.subtitler.model.AssDialogue;
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

public class AssSubtitleReader implements SubtitleReader<AssSubtitleFile> {

    private static final Logger LOGGER = Logger.getLogger(AssSubtitleReader.class);

    @Override
    public AssSubtitleFile read(String filename) {
        LOGGER.info("Reading subtitle file: " + filename);
        Validate.notBlank(filename);
        AssSubtitleFile file = new AssSubtitleFile();
        int lineCounter = 0;
        BufferedReader br;
        try {
            br = new BufferedReader(new FileReader(filename));
        } catch (FileNotFoundException e) {
            throw new SubtitleException("Could not read subtitle file " + filename, e);
        }

        try {
            LOGGER.debug("Reading first line...");
            lineCounter++;
            String line = br.readLine();
            Integer dialogueCounter = 0;
            while (line != null) {
                line = line.trim().replace(BYTE_ORDER_MARK, "");
                if (line.startsWith("[")) {
                    if (line.equalsIgnoreCase("[Script Info]")) {
                        LOGGER.debug("Found script info...");
                        lineCounter++;
                        line = br.readLine();
                        while (line != null && !line.startsWith("[")) {
                            line = line.trim();
                            String value = getValue(line);
                            if (line.startsWith("Title:")) {
                                file.setTitle(value);
                            } else if (line.startsWith("Collisions: ")) {
                                file.setCollisions(value);
                            } else if (line.startsWith("ScriptType:")) {
                                file.setScriptType(value);
                            } else if (line.startsWith("WrapStyle:")) {
                                file.setWrapStyle(value);
                            } else if (line.startsWith("PlayResX:")) {
                                file.setPlayResX(value);
                            } else if (line.startsWith("PlayResY:")) {
                                file.setPlayResY(value);
                            } else if (line.startsWith("ScaledBorderAndShadow:")) {
                                file.setScaledBorderAndShadow(value);
                            } else if (line.startsWith("Video Aspect Ratio")) {
                                file.setVideoAspectRatio(value);
                            } else if (line.startsWith("Video Zoom")) {
                                file.setVideoZoom(value);
                            } else if (line.startsWith("Video Position")) {
                                file.setVideoPosition(value);
                            } else if (line.startsWith(";")) {
                                file.getComments().add(line);
                            } else if (StringUtils.isNotBlank(line)) {
                                LOGGER.info("Implementation has not been created for the following line in [Script Info] section: " + line);
                            }

                            lineCounter++;
                            line = br.readLine();
                        }
                    } else if (line.equalsIgnoreCase("[V4+ Styles]")) {
                        LOGGER.debug("Found styles...");
                        lineCounter++;
                        line = br.readLine().trim();

                        while (!line.startsWith("[")) {
                            String value = getValue(line);
                            if (line.startsWith("Format:")) {
                                List<String> formatOrder = Arrays.stream(value.split(",")).map(String::trim).collect(Collectors.toList());
                                file.setStylesFormatOrder(formatOrder);
                            } else if (line.startsWith("Style:")) {
                                if (file.getStylesFormatOrder().isEmpty()) {
                                    throw new SubtitleException(".ass subtitle is in the incorrect format. Did not find 'Format' field before 'Style' field in [V4+ Styles] section. See line number: " + lineCounter);
                                }

                                String[] styles = value.split(",");
                                if (styles.length != file.getStylesFormatOrder().size()) {
                                    throw new SubtitleException(".ass subtitle is in the incorrect format. More styles were listed than were included in the format header. See line number: " + lineCounter);
                                }

                                AssStyle style = new AssStyle();
                                for (int i = 0; i < file.getStylesFormatOrder().size(); i++) {
                                    String type = file.getStylesFormatOrder().get(i);
                                    String styleValue = styles[i].trim();

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
                        LOGGER.debug("Found events...");
                        lineCounter++;
                        line = br.readLine().trim();

                        while (line != null && !line.startsWith("[")) {
                            String value = getValue(line);
                            if (line.startsWith("Format:")) {
                                List<String> formatOrder = Arrays.stream(value.split(",")).map(String::trim).collect(Collectors.toList());
                                file.setEventsFormatOrder(formatOrder);
                            } else if (line.startsWith("Dialogue:") || line.startsWith("Comment:")) {
                                if (file.getEventsFormatOrder().isEmpty()) {
                                    throw new SubtitleException(".ass subtitle is in the incorrect format. Did not find 'Format' field before 'Dialogue' field in [Events] section. See line number: " + lineCounter);
                                }

                                String[] dialogues = value.split(",");
                                // This usually means that the dialogue had some commas in it.
                                // This will fix those issues.
                                if (dialogues.length > file.getEventsFormatOrder().size()) {
                                    StringBuilder sb = new StringBuilder();
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

                                AssDialogue dialogue = new AssDialogue();
                                dialogue.setComment(line.startsWith("Comment:"));
                                for (int i = 0; i < file.getEventsFormatOrder().size() && i < dialogues.length; i++) {
                                    String type = file.getEventsFormatOrder().get(i);
                                    String dialogueValue = dialogues[i].trim();

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
                        LOGGER.info("Found type " + line + " which is not supported. Skipping this section.");
                        lineCounter++;
                        line = br.readLine();
                    }
                } else {
                    // This is a line in a section we do not support. Skipping.
                    lineCounter++;
                    line = br.readLine();
                }
            }
            LOGGER.debug("Read " + lineCounter + " lines from file " + filename);
        } catch (NullPointerException e) {
            LOGGER.warn("Unexpected end of file after reading " + lineCounter + " lines for file " + filename, e);
        } catch (IOException e) {
            throw new SubtitleException("An error occurred reading file " + filename, e);
        } finally {
            try {
                br.close();
            } catch (IOException e) {
                LOGGER.error("Could not close stream", e);
            }
        }

        LOGGER.debug("Found " + file.getSubtitles().size() + " subtitles.");
        return file;
    }

    private String getValue(String line) {
        String[] split = line.split(":");
        if (split.length > 1) {
            return String.join(":", Arrays.asList(split)).substring(split[0].length() + 1).trim();
        }
        return split[0];
    }
}
