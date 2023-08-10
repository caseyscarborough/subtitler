package sh.casey.subtitler.cli.logging;

import com.diogonunes.jcolor.Ansi;
import com.diogonunes.jcolor.Attribute;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.Marker;

import java.io.Serializable;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class TerminalLogger implements Logger, Serializable {

    public static boolean isTraceEnabled = false;
    private final String name;

    public static TerminalLogger getLogger(String name) {
        return new TerminalLogger(name);
    }

    public void success(String message) {
        System.out.println(Ansi.colorize(message, Attribute.GREEN_TEXT()));
    }

    public void success(String message, Object... args) {
        success(replaceArgs(message, args));
    }

    public void info(String message) {
        if (isInfoEnabled()) {
            System.out.println(Ansi.colorize(message, Attribute.CYAN_TEXT()));
        }
    }

    @Override
    public void info(String format, Object arg) {
        info(replaceArgs(format, arg));
    }

    @Override
    public void info(String format, Object arg1, Object arg2) {
        info(replaceArgs(format, arg1, arg2));
    }

    public void info(String message, Object... args) {
        info(replaceArgs(message, args));
    }

    @Override
    public void info(String msg, Throwable t) {
        info(msg);
        warn(t.getMessage());
    }

    @Override
    public boolean isInfoEnabled(Marker marker) {
        return true;
    }

    @Override
    public void info(Marker marker, String msg) {
        info(msg);
    }

    @Override
    public void info(Marker marker, String format, Object arg) {
        info(replaceArgs(format, arg));
    }

    @Override
    public void info(Marker marker, String format, Object arg1, Object arg2) {
        info(replaceArgs(format, arg1, arg2));
    }

    @Override
    public void info(Marker marker, String format, Object... arguments) {
        info(replaceArgs(format, arguments));
    }

    @Override
    public void info(Marker marker, String msg, Throwable t) {
        info(msg);
        warn(t.getMessage());
    }

    @Override
    public boolean isWarnEnabled() {
        return true;
    }

    public void error(String error) {
        if (isErrorEnabled()) {
            System.err.println(Ansi.colorize(error, Attribute.RED_TEXT()));
        }
    }

    @Override
    public void error(String format, Object arg) {
        error(replaceArgs(format, arg));
    }

    @Override
    public void error(String format, Object arg1, Object arg2) {
        error(replaceArgs(format, arg1, arg2));
    }

    public void error(String error, Object... args) {
        error(replaceArgs(error, args));
    }

    @Override
    public void error(String msg, Throwable t) {
        error(msg);
        error(t.getMessage());
    }

    @Override
    public boolean isErrorEnabled(Marker marker) {
        return true;
    }

    @Override
    public void error(Marker marker, String msg) {
        error(msg);
    }

    @Override
    public void error(Marker marker, String format, Object arg) {
        error(replaceArgs(format, arg));
    }

    @Override
    public void error(Marker marker, String format, Object arg1, Object arg2) {
        error(replaceArgs(format, arg1, arg2));
    }

    @Override
    public void error(Marker marker, String format, Object... arguments) {
        error(replaceArgs(format, arguments));
    }

    @Override
    public void error(Marker marker, String msg, Throwable t) {
        error(msg);
        error(t.getMessage());
    }

    public void warn(String error) {
        System.err.println(Ansi.colorize(error, Attribute.YELLOW_TEXT()));
    }

    @Override
    public void warn(String format, Object arg) {
        warn(replaceArgs(format, arg));
    }

    public void warn(String error, Object... args) {
        warn(replaceArgs(error, args));
    }

    @Override
    public void warn(String format, Object arg1, Object arg2) {
        warn(replaceArgs(format, arg1, arg2));
    }

    @Override
    public void warn(String msg, Throwable t) {
        warn(msg);
        warn(t.getMessage());
    }

    @Override
    public boolean isWarnEnabled(Marker marker) {
        return true;
    }

    @Override
    public void warn(Marker marker, String msg) {
        warn(msg);
    }

    @Override
    public void warn(Marker marker, String format, Object arg) {
        warn(replaceArgs(format, arg));
    }

    @Override
    public void warn(Marker marker, String format, Object arg1, Object arg2) {
        warn(replaceArgs(format, arg1, arg2));
    }

    @Override
    public void warn(Marker marker, String format, Object... arguments) {
        warn(replaceArgs(format, arguments));
    }

    @Override
    public void warn(Marker marker, String msg, Throwable t) {
        warn(msg);
        warn(t.getMessage());
    }

    @Override
    public boolean isErrorEnabled() {
        return true;
    }

    public void debug(String message) {
        if (isDebugEnabled()) {
            System.out.println(Ansi.colorize(message, Attribute.WHITE_TEXT().DIM()));
        }
    }

    @Override
    public void debug(String format, Object arg) {
        debug(replaceArgs(format, arg));
    }

    @Override
    public void debug(String format, Object arg1, Object arg2) {
        debug(replaceArgs(format, arg1, arg2));
    }

    public void debug(String message, Object... args) {
        debug(replaceArgs(message, args));
    }

    @Override
    public void debug(String msg, Throwable t) {
        debug(msg);
        warn(t.getMessage());
    }

    @Override
    public boolean isDebugEnabled(Marker marker) {
        return true;
    }

    @Override
    public void debug(Marker marker, String msg) {
        debug(msg);
    }

    @Override
    public void debug(Marker marker, String format, Object arg) {
        debug(replaceArgs(format, arg));
    }

    @Override
    public void debug(Marker marker, String format, Object arg1, Object arg2) {
        debug(replaceArgs(format, arg1, arg2));
    }

    @Override
    public void debug(Marker marker, String format, Object... arguments) {
        debug(replaceArgs(format, arguments));
    }

    @Override
    public void debug(Marker marker, String msg, Throwable t) {
        debug(msg);
        warn(t.getMessage());
    }

    @Override
    public boolean isInfoEnabled() {
        return true;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean isTraceEnabled() {
        return isTraceEnabled;
    }

    public void trace(String message) {
        if (isTraceEnabled()) {
            System.out.println(Ansi.colorize(message, Attribute.BRIGHT_BLACK_TEXT()));
        }
    }

    @Override
    public void trace(String format, Object arg) {
        trace(replaceArgs(format, arg));
    }

    @Override
    public void trace(String format, Object arg1, Object arg2) {
        trace(replaceArgs(format, arg1, arg2));
    }

    public void trace(String message, Object... args) {
        trace(replaceArgs(message, args));
    }

    @Override
    public void trace(String msg, Throwable t) {
        trace(msg);
        trace(t.getMessage());
    }

    @Override
    public boolean isTraceEnabled(Marker marker) {
        return false;
    }

    @Override
    public void trace(Marker marker, String msg) {
        trace(msg);
    }

    @Override
    public void trace(Marker marker, String format, Object arg) {
        trace(replaceArgs(format, arg));
    }

    @Override
    public void trace(Marker marker, String format, Object arg1, Object arg2) {
        trace(replaceArgs(format, arg1, arg2));
    }

    @Override
    public void trace(Marker marker, String format, Object... argArray) {
        trace(replaceArgs(format, argArray));
    }

    @Override
    public void trace(Marker marker, String msg, Throwable t) {
        trace(msg);
        trace(t.getMessage());
    }

    @Override
    public boolean isDebugEnabled() {
        return true;
    }

    private String replaceArgs(String message, Object... args) {
        for (Object arg : args) {
            if (arg == null) {
                continue;
            }
            message = message.replaceFirst("\\{}", arg.toString());
        }
        return message;
    }
}
