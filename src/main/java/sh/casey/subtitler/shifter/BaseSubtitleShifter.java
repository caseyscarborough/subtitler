package sh.casey.subtitler.shifter;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import sh.casey.subtitler.exception.SubtitleException;
import sh.casey.subtitler.model.Subtitle;
import sh.casey.subtitler.model.SubtitleFile;
import sh.casey.subtitler.util.TimeUtil;

@Slf4j
abstract class BaseSubtitleShifter<T extends SubtitleFile> implements SubtitleShifter<T> {

    @Override
    public void shift(T file, final ShiftConfig config) {
        final Long ms = config.getMs();
        if (ms == null) {
            throw new SubtitleException("You must specify a time in milliseconds to shift the subtitles.");
        }
        log.info("Shifting subtitles in file {} by {}ms.", file.getPath(), ms);
        Long beforeDate = null;
        Long afterDate = null;
        if (StringUtils.isNotBlank(config.getBefore()) && !StringUtils.isNumeric(config.getBefore())) {
            // Only convert to milliseconds if the time is not numeric (uses the 00:00:00,000 format)
            beforeDate = TimeUtil.srtFormatTimeToMilliseconds(config.getBefore());
        }
        if (StringUtils.isNotBlank(config.getAfter()) && !StringUtils.isNumeric(config.getAfter())) {
            // Only convert to milliseconds if the time is not numeric (uses the 00:00:00,000 format)
            afterDate = TimeUtil.srtFormatTimeToMilliseconds(config.getAfter());
        }
        Integer beforeNumber = null;
        Integer afterNumber = null;
        if (StringUtils.isNumeric(config.getBefore())) {
            // If the before time is numeric, we know it's a subtitle number
            beforeNumber = Integer.parseInt(config.getBefore());
        }

        if (StringUtils.isNumeric(config.getAfter())) {
            // If the after time is numeric, we know it's a subtitle number
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
                log.trace("Skipping subtitle {} because it is not after the \"after\" time or number.", subtitle.getNumber());
                continue;
            }

            // If it's not before the "before" time, don't shift it.
            if ((beforeDate != null && from > beforeDate) || (beforeNumber != null && subtitle.getNumber() >= beforeNumber)) {
                log.trace("Skipping subtitle {} because it is not before the \"before\" time or number.", subtitle.getNumber());
                continue;
            }

            // If the user specified a number of a subtitle to shift, don't shift unless this is that number.
            if (config.getNumber() != null && (subtitle.getNumber() == null || !subtitle.getNumber().equals(config.getNumber()))) {
                log.trace("Skipping subtitle number {} because it is not the number specified to shift.", subtitle.getNumber());
                continue;
            }

            // If the text doesn't contain the specified matching text, don't shift it.
            if (StringUtils.isNotBlank(config.getMatches()) && !subtitle.getText().contains(config.getMatches())) {
                log.trace("Skipping subtitle number {} because it does not contain the specified matching text.", subtitle.getNumber());
                continue;
            }

            if (config.getShiftMode().equals(ShiftMode.FROM) || config.getShiftMode().equals(ShiftMode.FROM_TO)) {
                subtitle.setStart(TimeUtil.millsecondsToTime(subtitle.getType(), from + ms));
            }

            if (config.getShiftMode().equals(ShiftMode.TO) || config.getShiftMode().equals(ShiftMode.FROM_TO)) {
                subtitle.setEnd(TimeUtil.millsecondsToTime(subtitle.getType(), to + ms));
            }

            shiftCount++;
            log.trace("Shifted ." + file.getType().name().toLowerCase() + " line from " + originalFrom + " --> " + originalEnd + " to " + subtitle.getStart() + " --> " + subtitle.getEnd());
        }

        log.info("Shifted " + shiftCount + " subtitles.");
    }
}
