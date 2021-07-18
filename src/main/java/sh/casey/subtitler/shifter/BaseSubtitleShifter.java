package sh.casey.subtitler.shifter;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import sh.casey.subtitler.exception.SubtitleException;
import sh.casey.subtitler.model.Subtitle;
import sh.casey.subtitler.model.SubtitleFile;
import sh.casey.subtitler.model.SubtitleType;
import sh.casey.subtitler.reader.SubtitleReader;
import sh.casey.subtitler.reader.SubtitleReaderFactory;
import sh.casey.subtitler.writer.SubtitleWriter;
import sh.casey.subtitler.writer.SubtitleWriterFactory;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static java.util.Calendar.MILLISECOND;
import static sh.casey.subtitler.util.TimeUtil.assTrim;

@Slf4j
abstract class BaseSubtitleShifter<T extends SubtitleFile> implements SubtitleShifter<T> {

    private static final String SHIFT_BEFORE_AFTER_FORMAT = "HH:mm:ss,SSS";

    public abstract SubtitleType getSubtitleType();

    public abstract String getDefaultTime();

    @Override
    public void shift(ShiftConfig config) {
        String input = config.getInput();
        String output = config.getOutput();
        int ms = config.getMs();
        log.debug("Shifting subtitles in file " + input + " by " + ms + "ms. Sending output to " + output + "...");
        SubtitleReader<T> reader = new SubtitleReaderFactory().getInstance(getSubtitleType());
        T file = reader.read(input);
        log.debug("Found " + file.getSubtitles().size() + " lines");

        Date beforeDate = getBeforeAfterDate(config.getBefore());
        Date afterDate = getBeforeAfterDate(config.getAfter());

        Integer beforeNumber = null;
        Integer afterNumber = null;
        if (StringUtils.isNumeric(config.getBefore())) {
            beforeNumber = Integer.parseInt(config.getBefore());
        }

        if (StringUtils.isNumeric(config.getAfter())) {
            afterNumber = Integer.parseInt(config.getAfter());
        }

        if (beforeDate != null || beforeNumber != null) {
            log.debug("Only shifting subtitles before " + config.getBefore());
        }

        if (afterDate != null || afterNumber != null) {
            log.debug("Only shifting subtitles after " + config.getAfter());
        }

        if (config.getNumber() != null) {
            log.debug("Only shifting subtitle if it is number " + config.getNumber());
        }

        if (config.getMatches() != null) {
            log.debug("Only shifting subtitle if it matches \"" + config.getMatches() + "\"");
        }

        int shiftCount = 0;
        try {
            DateFormat format = new SimpleDateFormat(file.getType().getTimeFormat());
            Calendar fromCal = Calendar.getInstance();
            Calendar toCal = Calendar.getInstance();

            for (Subtitle subtitle : file.getSubtitles()) {
                String originalFrom = subtitle.getStart();
                String originalEnd = subtitle.getEnd();
                Date from = format.parse(subtitle.getStart());
                Date to = format.parse(subtitle.getEnd());

                // If it's not after the "after" time, don't shift it.
                if ((afterDate != null && !from.after(afterDate)) || (afterNumber != null && subtitle.getNumber() <= afterNumber)) {
                    continue;
                }

                // If it's not before the "before" time, don't shift it.
                if ((beforeDate != null && !from.before(beforeDate)) || (beforeNumber != null && subtitle.getNumber() >= beforeNumber)) {
                    continue;
                }

                // If the user specified a number of a subtitle to shift, don't shift unless this is that number.
                if (config.getNumber() != null && (subtitle.getNumber() == null || !subtitle.getNumber().equals(config.getNumber()))) {
                    continue;
                }

                // If the text doesn't contain the specified matching text, don't shift it.
                if (StringUtils.isNotBlank(config.getMatches()) && !subtitle.getText().contains(config.getMatches())) {
                    continue;
                }

                if (config.getShiftMode().equals(ShiftMode.FROM) || config.getShiftMode().equals(ShiftMode.FROM_TO)) {
                    fromCal.setTime(from);
                    int fromDay = fromCal.get(Calendar.DAY_OF_YEAR);
                    fromCal.add(MILLISECOND, ms);
                    int newFromDay = fromCal.get(Calendar.DAY_OF_YEAR);
                    if (fromDay == newFromDay) {
                        String startTime = format.format(fromCal.getTime());
                        if (getSubtitleType().equals(SubtitleType.ASS)) {
                            startTime = assTrim(startTime);
                        }
                        subtitle.setStart(startTime);
                    } else {
                        subtitle.setStart(getDefaultTime());
                    }
                }

                if (config.getShiftMode().equals(ShiftMode.TO) || config.getShiftMode().equals(ShiftMode.FROM_TO)) {
                    toCal.setTime(to);
                    int toDay = toCal.get(Calendar.DAY_OF_YEAR);
                    toCal.add(MILLISECOND, ms);
                    int newToDay = toCal.get(Calendar.DAY_OF_YEAR);
                    if (toDay == newToDay) {
                        String endTime = format.format(toCal.getTime());
                        if (getSubtitleType().equals(SubtitleType.ASS)) {
                            endTime = assTrim(endTime);
                        }
                        subtitle.setEnd(endTime);
                    } else {
                        subtitle.setEnd(getDefaultTime());
                    }
                }

                shiftCount++;
                log.trace("Shifted ." + getSubtitleType().name().toLowerCase() + " line from " + originalFrom + " --> " + originalEnd + " to " + subtitle.getStart() + " --> " + subtitle.getEnd());
            }
        } catch (ParseException e) {
            throw new SubtitleException("An error occurred shifting subtitles", e);
        }

        log.debug("Shifted " + shiftCount + " subtitles.");
        SubtitleWriterFactory factory = new SubtitleWriterFactory();
        SubtitleWriter<T> writer = factory.getInstance(getSubtitleType());
        writer.write(file, output);
    }

    private Date getBeforeAfterDate(String time) {
        if (StringUtils.isBlank(time) || StringUtils.isNumeric(time)) {
            return null;
        }

        DateFormat format = new SimpleDateFormat(SHIFT_BEFORE_AFTER_FORMAT);
        try {
            return format.parse(time);
        } catch (ParseException e) {
            throw new SubtitleException("'Before' time was in the incorrect format. Please use " + SHIFT_BEFORE_AFTER_FORMAT + " format.", e);
        }
    }
}
