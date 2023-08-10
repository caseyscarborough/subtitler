package sh.casey.subtitler.model;

import sh.casey.subtitler.util.TimeUtil;

abstract class BaseSubtitle implements Subtitle {

    @Override
    public Long getStartMilliseconds() {
        return TimeUtil.timeToMilliseconds(this.getType(), this.getStart());
    }

    @Override
    public Long getEndMilliseconds() {
        return TimeUtil.timeToMilliseconds(this.getType(), this.getEnd());
    }
}
