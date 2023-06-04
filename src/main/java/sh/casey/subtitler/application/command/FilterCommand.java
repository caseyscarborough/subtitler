package sh.casey.subtitler.application.command;

import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;
import sh.casey.subtitler.application.command.completer.FilterTypeCompleter;
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
        "Example: subs filter --type STYLE \"op,ed,signs,songs\"",
    },
    sortOptions = false,
    sortSynopsis = false
)
public class FilterCommand extends BaseCommand {

    @Option(names = {"-t", "--type"}, description = "The type of filter to use. Options are: ${COMPLETION-CANDIDATES}.", required = true, completionCandidates = FilterTypeCompleter.class)
    private FilterType type;

    @Parameters(index = "0", description = "The filter to use. This should match the type of filter specified with the -t option.", arity = "1")
    private String filter;

    @Override
    public void run() {
        final String input = getInput();
        final String output = getOutput();
        final SubtitleType inputType = getInputType();
        final SubtitleType outputType = getOutputType();
        final SubtitleReader<SubtitleFile> reader = new SubtitleReaderFactory().getInstance(inputType);
        final SubtitleFile file = reader.read(input);
        final FiltererFactory factory = new FiltererFactory();
        Filterer<SubtitleFile> filterer = factory.getInstance(inputType);
        filterer.filter(file, type, filter);

        SubtitleWriter<SubtitleFile> writer = new SubtitleWriterFactory().getInstance(outputType);
        writer.write(file, output);
    }
}
