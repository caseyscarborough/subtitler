package sh.casey.subtitler.application;

import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Model.CommandSpec;
import picocli.CommandLine.Spec;
import sh.casey.subtitler.application.command.AlignCommand;
import sh.casey.subtitler.application.command.CondenseCommand;
import sh.casey.subtitler.application.command.ConvertCommand;
import sh.casey.subtitler.application.command.DualSubtitleCommand;
import sh.casey.subtitler.application.command.FilterCommand;
import sh.casey.subtitler.application.command.NormalizeCommand;
import sh.casey.subtitler.application.command.PrintCommand;
import sh.casey.subtitler.application.command.RenumberCommand;
import sh.casey.subtitler.application.command.ShiftCommand;
import sh.casey.subtitler.application.exception.ExceptionHandler;
import sh.casey.subtitler.application.provider.VersionProvider;

import java.util.concurrent.Callable;

@Command(
    name = "subtitler",
    version = "2.0",
    description = "Subtitler is a command line tool for manipulating subtitle files.",
    mixinStandardHelpOptions = true,
    subcommands = {
        AlignCommand.class,
        CondenseCommand.class,
        ConvertCommand.class,
        DualSubtitleCommand.class,
        FilterCommand.class,
        NormalizeCommand.class,
        PrintCommand.class,
        RenumberCommand.class,
        ShiftCommand.class,
    },
    versionProvider = VersionProvider.class
)
public class Subtitler implements Callable<Integer> {

    @Spec
    private CommandSpec spec;

    public static void main(String[] args) {
        final CommandLine cmd = new CommandLine(new Subtitler())
            .setCaseInsensitiveEnumValuesAllowed(true)
            .setUsageHelpAutoWidth(true)
            .setUsageHelpLongOptionsMaxWidth(25)
            .setExecutionExceptionHandler(new ExceptionHandler());

        if (cmd.isVersionHelpRequested()) {
            cmd.printVersionHelp(System.out);
            return;
        }

        final int exitCode = cmd.execute(args);

        System.exit(exitCode);
    }

    @Override
    public Integer call() {
        // Print the help command if no parameters are specified
        spec.commandLine().usage(System.err);
        return 1;
    }
}
