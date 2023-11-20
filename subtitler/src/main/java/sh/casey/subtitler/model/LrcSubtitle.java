package sh.casey.subtitler.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import sh.casey.subtitler.util.TimeUtil;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LrcSubtitle implements Subtitle {
    // LRC files only have start times, no end times.
    private String start;
    private String text;
    private Integer number;

    @Override
    public Integer getNumber() {
        return number;
    }

    @Override
    public SubtitleType getType() {
        return SubtitleType.LRC;
    }

    @Override
    public Long getStartMilliseconds() {
        return TimeUtil.timeToMilliseconds(SubtitleType.LRC, start);
    }

    @Override
    public void setStart(String start) {
        this.start = start;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    @Override
    public String getEnd() {
        throw new UnsupportedOperationException("LRC files do not have end times!");
    }

    @Override
    public Long getEndMilliseconds() {
        throw new UnsupportedOperationException("LRC files do not have end times!");
    }

    @Override
    public void setEnd(String end) {
        throw new UnsupportedOperationException("LRC files do not have end times!");
    }
}
