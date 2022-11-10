package sh.casey.subtitler.shifter;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import sh.casey.subtitler.exception.SubtitleException;
import sh.casey.subtitler.model.Subtitle;
import sh.casey.subtitler.model.SubtitleFile;
import sh.casey.subtitler.model.SubtitleType;
import sh.casey.subtitler.reader.SubtitleReader;
import sh.casey.subtitler.reader.SubtitleReaderFactory;
import sh.casey.subtitler.util.TimeUtil;
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

    public abstract SubtitleType getSubtitleType();

    @Override
    public void shift(final ShiftConfig config) {
        final String input = config.getInput();
        final String output = config.getOutput();
        final Integer ms = config.getMs();
        if (StringUtils.isBlank(input)) {
            throw new SubtitleException("Input file is required.");
        }

        if (StringUtils.isBlank(output)) {
            throw new SubtitleException("Output file is required.");
        }

        if (ms == null) {
            throw new SubtitleException("You must specify a time in milliseconds to shift the subtitles.");
        }
        log.debug("Shifting subtitles in file " + input + " by " + ms + "ms. Sending output to " + output + "...");
        final SubtitleReader<T> reader = new SubtitleReaderFactory().getInstance(getSubtitleType());
        final T file = reader.read(input);
        log.debug("Found " + file.getSubtitles().size() + " lines");

        Long beforeDate = null;
        Long afterDate = null;
        if (StringUtils.isNotBlank(config.getBefore())) {
            beforeDate = TimeUtil.srtFormatTimeToMilliseconds(config.getBefore());
        }
        if (StringUtils.isNotBlank(config.getAfter())) {
            afterDate = TimeUtil.srtFormatTimeToMilliseconds(config.getAfter());
        }
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
        for (final Subtitle subtitle : file.getSubtitles()) {
            final String originalFrom = subtitle.getStart();
            final String originalEnd = subtitle.getEnd();
            final Long from = subtitle.getStartMilliseconds();
            final Long to = subtitle.getEndMilliseconds();

            // If it's not after the "after" time, don't shift it.
            if ((afterDate != null && from < afterDate) || (afterNumber != null && subtitle.getNumber() <= afterNumber)) {
                continue;
            }

            // If it's not before the "before" time, don't shift it.
            if ((beforeDate != null && from > beforeDate) || (beforeNumber != null && subtitle.getNumber() >= beforeNumber)) {
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
                subtitle.setStart(TimeUtil.millsecondsToTime(subtitle.getType(), from + ms));
            }

            if (config.getShiftMode().equals(ShiftMode.TO) || config.getShiftMode().equals(ShiftMode.FROM_TO)) {
                subtitle.setEnd(TimeUtil.millsecondsToTime(subtitle.getType(), to + ms));
            }

            shiftCount++;
            log.trace("Shifted ." + getSubtitleType().name().toLowerCase() + " line from " + originalFrom + " --> " + originalEnd + " to " + subtitle.getStart() + " --> " + subtitle.getEnd());
        }

        log.debug("Shifted " + shiftCount + " subtitles.");
        final SubtitleWriterFactory factory = new SubtitleWriterFactory();
        final SubtitleWriter<T> writer = factory.getInstance(getSubtitleType());
        writer.write(file, output);
    }
}
