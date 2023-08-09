package sh.casey.subtitler.application.command;

import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import sh.casey.subtitler.application.command.completer.FilterModeCompleter;
import sh.casey.subtitler.application.command.completer.FilterTypeCompleter;
import sh.casey.subtitler.filter.FilterMode;
import sh.casey.subtitler.filter.Filterer;
import sh.casey.subtitler.filter.FiltererFactory;
import sh.casey.subtitler.model.SubtitleFile;
import sh.casey.subtitler.model.SubtitleType;
import sh.casey.subtitler.reader.SubtitleReader;
import sh.casey.subtitler.reader.SubtitleReaderFactory;
import sh.casey.subtitler.writer.SubtitleWriter;
import sh.casey.subtitler.writer.SubtitleWriterFactory;

@Command(
    name = "filter",
    aliases = "f",
    description = {
        "Filter a subtitle file by removing lines that match (or don't match) a specific criteria.",
        "Examples:\n" +
            "  subs filter --filters \"style=op,ed,signs,songs\" --mode OMIT (remove subtitles with the op, ed, signs, or songs styles)\n" +
            "  subs filter --filters \"after=00:30:00.000\" --mode OMIT (remove subtitles after 30 minutes)\n" +
            "  subs filter --filters \"text=♪♪～\" --mode OMIT (remove subtitles matching ♪♪～)\n" +
            "  subs filter --filters \"style=Main,Default\" --mode RETAIN (remove subtitles that aren't the Main or Default style)"
    },
    sortOptions = false,
    sortSynopsis = false
)
public class FilterCommand extends BaseCommand {

    private final FiltererFactory factory = new FiltererFactory();

    @Option(names = {"-m", "--mode"}, description = "The filter mode to use. Modes are: ${COMPLETION-CANDIDATES}. (Default is OMIT)", completionCandidates = FilterModeCompleter.class)
    private FilterMode mode = FilterMode.OMIT;

    @Option(names = {"-f", "--filters"}, description = "The type of filters to use. Filters should be key/value pairs separated with a semicolon, and multiple values can be specified with commas. Multiple filters are executed with 'OR' logic (all filters are applied).\nExample: --filters \"style=op,ed,songs,signs;after=00:30:00.000\" --mode OMIT (remove all subtitles matching the specified styles or after 30 minutes).\nFilters: ${COMPLETION-CANDIDATES}.", required = true, completionCandidates = FilterTypeCompleter.class)
    private String filters;

    @Option(names = {"-t", "--threshold"}, description = "The threshold below which to filter. This will prevent filtering if the number of subtitles that would be removed exceeds the threshold.", defaultValue = "2147483647")
    private int threshold;

    @Override
    public void doRun() {
        final SubtitleType inputType = getInputType();
        final SubtitleReader<SubtitleFile> reader = new SubtitleReaderFactory().getInstance(inputType);
        final Filterer<SubtitleFile> filterer = factory.getInstance(inputType);
        final SubtitleFile file = reader.read(getInput());

        filterer.filter(file, filters, mode, threshold);

        final SubtitleWriter<SubtitleFile> writer = new SubtitleWriterFactory().getInstance(getOutputType());
        writer.write(file, getOutput());
    }
}
