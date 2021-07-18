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
import sh.casey.subtitler.model.SubtitleType;
import sh.casey.subtitler.shifter.ShiftMode;

import java.util.Arrays;
import java.util.stream.Collectors;

public class Application {

    private static final Logger LOGGER = Logger.getLogger(Application.class);

    public static void main(String[] args) throws ParseException {
        CommandLineParser parser = new DefaultParser();
        Options options = new Options();

        // actions
        options.addOption("d", "dual-subs", false, "Create Dual Sub file from two input files");
        options.addOption("s", "shift", false, "Shift timing on subtitles");
        options.addOption("c", "convert", false, "Convert file from one format to another");
        options.addOption("r", "renumber", false, "Renumber a subtitle file starting from 1 (requires -i flag). Useful for when splitting subtitle files that were previously multiple episodes.");
        options.addOption("cn", "condense", false, "Condense a subtitle file by putting common lines (of the same start/end times) into a single subtitle line.");
        options.addOption("h", "help", false, "Display this help menu");

        // dual-subtitle options
        options.addOption("tf", "top", true, "Top file for dual sub creation");
        options.addOption("bf", "bottom", true, "Bottom file for dual sub creation");

        // shift options
        options.addOption("sm", "shift-mode", true, "Shift mode, default is 'FROM_TO'. Options are " + Arrays.stream(ShiftMode.values()).map(ShiftMode::toString).collect(Collectors.joining(", ")));
        options.addOption("a", "after", true, "Only shift subtitles after a specific time in the file (format HH:mm:ss,SSS)");
        options.addOption("b", "before", true, "Only shift subtitles before a specific time in the file (format HH:mm:ss,SSS)");
        options.addOption("n", "number", true, "The number of a specific subtitle to shift");
        options.addOption("m", "matches", true, "Used to specify text to match a specific subtitle to shift");

        // generic options
        options.addOption("i", "input", true, "Input filename (required when using -s)");
        options.addOption("o", "output", true, "Output filename");
        options.addOption("t", "time", true, "Time in milliseconds to shift subtitles (required when using -s)");
        options.addOption("it", "input-type", true, "The type of the input file, options are " + Arrays.toString(SubtitleType.values()));
        options.addOption("ot", "output-type", true, "The type for the output file, options are " + Arrays.toString(SubtitleType.values()));

        CommandLine cmd = parser.parse(options, args);
        CommandFactory factory = new CommandFactory();
        try {
            ApplicationCommand command = factory.getInstance(cmd);
            command.execute();
        } catch (CommandNotFoundException e) {
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp("java -jar <jar-name> <options>", options);
            System.exit(0);
        } catch (InvalidCommandException e) {
            LOGGER.error(e.getMessage());
            System.exit(1);
        }
    }
}
