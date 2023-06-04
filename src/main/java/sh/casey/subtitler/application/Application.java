package sh.casey.subtitler.application;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.log4j.Logger;
import sh.casey.subtitler.application.command.ApplicationCommand;
import sh.casey.subtitler.application.command.CommandFactory;
import sh.casey.subtitler.application.exception.CommandNotFoundException;
import sh.casey.subtitler.application.exception.InvalidCommandException;
import sh.casey.subtitler.filter.FilterType;
import sh.casey.subtitler.model.SubtitleType;
import sh.casey.subtitler.shifter.ShiftMode;

import java.util.Arrays;
import java.util.stream.Collectors;

public class Application {

    private static final Logger log = Logger.getLogger(Application.class);

    public static void main(final String[] args) throws ParseException {
        final CommandLineParser parser = new DefaultParser();
        final Options options = new Options();

        // actions
        options.addOption(null, "align", false, "Align subtitles with each other. Slightly shifts the start/end times of subtitles to match the other subtitle file.");
        options.addOption("v", "version", false, "Display the current version");
        options.addOption("d", "dual-subs", false, "Create dual subtitle file from two input files. The styles are copied from the bottom subtitle (unless it is SRT format).");
        options.addOption("s", "shift", false, "Shift timing on subtitles");
        options.addOption("c", "convert", false, "Convert file from one format to another");
        options.addOption("r", "renumber", false, "Renumber a subtitle file starting from 1 by default (requires -i flag). Useful for when splitting subtitle files that were previously multiple episodes. Use the --renumber-start (-rs) flag to specify the starting number.");
        options.addOption("rs", "renumber-start", true, "The number to start renumbering from.");
        options.addOption("cn", "condense", false, "Condense a subtitle file by putting common lines (of the same start/end times) into a single subtitle line. Also combines adjacent subtitles having the same text.");
        options.addOption("n", "normalize", false, "Normalize subtitles (convert half-width kana to full width, etc.)");
        options.addOption("h", "help", false, "Display this help menu");
        options.addOption("p", "print", false, "Print all the dialogues from the subtitle file");

        // dual-subtitle options
        options.addOption("tf", "top", true, "Top file for dual sub creation");
        options.addOption("bf", "bottom", true, "Bottom file for dual sub creation");
        options.addOption("dc", "dual-subs-config", true, "Configuration for dual subs (modifies the top subtitle only). Option keys are font, size, and bold. The format is key=value,key=value, e.g. --dual-subs-config 'font=Arial,size=64,bold=false'");
        options.addOption(null, "keep-top-styles", false, "Keep the styles for the top file from the input subtitle during dual subtitle creation.");

        // shift options
        options.addOption("sm", "shift-mode", true, "Shift mode, default is 'FROM_TO'. Options are " + Arrays.stream(ShiftMode.values()).map(ShiftMode::toString).collect(Collectors.joining(", ")));
        options.addOption("a", "after", true, "Only shift subtitles after a specific time in the file (format HH:mm:ss,SSS)");
        options.addOption("b", "before", true, "Only shift subtitles before a specific time in the file (format HH:mm:ss,SSS)");
        options.addOption("no", "number", true, "The number of a specific subtitle to shift");
        options.addOption("m", "matches", true, "Used to specify text to match a specific subtitle to shift");

        // generic options
        options.addOption("i", "input", true, "Input filename (required when using -s)");
        options.addOption("o", "output", true, "Output filename");
        options.addOption("t", "time", true, "Time in milliseconds to shift subtitles (required when using -s)");
        options.addOption("it", "input-type", true, "The type of the input file, options are " + Arrays.toString(SubtitleType.values()));
        options.addOption("ot", "output-type", true, "The type for the output file, options are " + Arrays.toString(SubtitleType.values()));

        // align options
        options.addOption(null, "reference", true, "Reference subtitle file to align to (required when using --align)");
        options.addOption(null, "threshold", true, "The threshold for alignment (default is 500ms)");
        options.addOption(null, "align-mode", true, "The alignment mode, options are " + Arrays.stream(ShiftMode.values()).map(ShiftMode::getFlag).collect(Collectors.joining(", ")));

        for (FilterType value : FilterType.values()) {
            options.addOption(value.getShortOpt(), value.getLongOpt(), true, value.getDescription());
        }

        final CommandLine cmd = parser.parse(options, args);
        final CommandFactory factory = new CommandFactory();
        try {
            final ApplicationCommand command = factory.getInstance(cmd);
            command.execute();
        } catch (final CommandNotFoundException e) {
            final HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp("subtitler <options>", options);
            System.exit(0);
        } catch (final InvalidCommandException e) {
            log.error(e.getMessage());
            System.exit(1);
        }
    }
}
