package sh.casey.subtitler.application.command;

import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;
import sh.casey.subtitler.application.command.completer.FilterModeCompleter;
import sh.casey.subtitler.application.command.completer.FilterTypeCompleter;
import sh.casey.subtitler.filter.FilterMode;
import sh.casey.subtitler.filter.FilterType;
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
        "Filter a subtitle file by removing lines that match a specific criteria.",
        "Example: subs filter --filters \"style=op,ed,signs,songs\"",
    },
    sortOptions = false,
    sortSynopsis = false
)
public class FilterCommand extends BaseCommand {

    @Option(names = {"-m", "--mode"}, description = "The filter mode to use. Modes are: ${COMPLETION-CANDIDATES}. (Default is OMIT)", completionCandidates = FilterModeCompleter.class)
    private FilterMode mode = FilterMode.OMIT;

    @Option(names = {"-f", "--filters"}, description = "The type of filters to use. Filters should be key/value pairs separated with a semicolon, and multiple values can be specified with commas, e.g. --filters \"style=op,ed,songs,signs\". Filters are: ${COMPLETION-CANDIDATES}.", required = true, completionCandidates = FilterTypeCompleter.class)
    private String filters;

    @Override
    public void doRun() {
        final String input = getInput();
        final String output = getOutput();
        final SubtitleType inputType = getInputType();
        final SubtitleType outputType = getOutputType();
        final SubtitleReader<SubtitleFile> reader = new SubtitleReaderFactory().getInstance(inputType);
        final SubtitleFile file = reader.read(input);
        final FiltererFactory factory = new FiltererFactory();
        Filterer<SubtitleFile> filterer = factory.getInstance(inputType);
        filterer.filter(file, filters, mode);

        SubtitleWriter<SubtitleFile> writer = new SubtitleWriterFactory().getInstance(outputType);
        writer.write(file, output);
    }
}
