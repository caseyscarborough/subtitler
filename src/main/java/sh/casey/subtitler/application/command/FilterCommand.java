package sh.casey.subtitler.application.command;

import lombok.extern.slf4j.Slf4j;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
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

import java.util.Arrays;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
@Slf4j
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

        filterer.filter(file, getFilterMap(file, filters), mode, threshold);

        final SubtitleWriter<SubtitleFile> writer = new SubtitleWriterFactory().getInstance(getOutputType());
        writer.write(file, getOutput());
    }

    private Map<FilterType, List<String>> getFilterMap(SubtitleFile file, String filters) {
        final String[] parts = filters.split(";");
        Map<FilterType, List<String>> output = new EnumMap<>(FilterType.class);
        for (String filter : parts) {
            final String[] keyValue = filter.split("=");
            if (keyValue.length != 2) {
                throw new IllegalArgumentException("Filters argument was invalid: " + filter + ". Filters must be in the format of key1=value1,value2;key2=value3,value4");
            }
            final FilterType key = FilterType.findByName(keyValue[0]);
            if (!key.getSupportedTypes().contains(file.getType())) {
                log.warn("Filter \"{}\" is not supported with {} subtitles, ignoring...", key.getName(), file.getType().getExtension());
                return output;
            }
            final List<String> values = Arrays.stream(keyValue[1].split(",")).collect(Collectors.toList());
            output.put(key, values);
        }
        return output;
    }
}
