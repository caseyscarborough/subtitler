package sh.casey.subtitler.application.command;

import org.apache.commons.cli.CommandLine;
import sh.casey.subtitler.filter.FilterType;
import sh.casey.subtitler.filter.Filterer;
import sh.casey.subtitler.filter.FiltererFactory;
import sh.casey.subtitler.model.SubtitleFile;
import sh.casey.subtitler.model.SubtitleType;
import sh.casey.subtitler.reader.SubtitleReader;
import sh.casey.subtitler.reader.SubtitleReaderFactory;
import sh.casey.subtitler.writer.SubtitleWriter;
import sh.casey.subtitler.writer.SubtitleWriterFactory;

class FilterCommand extends BaseCommand {

    public FilterCommand(CommandLine cmd) {
        super(cmd);
    }

    @Override
    public void execute() {
        final String input = getInputFilename();
        final String output = getOutputFilename();
        final SubtitleType subtitleType = getInputFileType();
        final SubtitleReader<SubtitleFile> reader = new SubtitleReaderFactory().getInstance(subtitleType);
        final SubtitleFile file = reader.read(input);
        final FiltererFactory factory = new FiltererFactory();
        Filterer<SubtitleFile> filterer = factory.getInstance(subtitleType);
        for (FilterType value : FilterType.values()) {
            if (cmd.hasOption(value.getLongOpt())) {
                filterer.filter(file, value, cmd.getOptionValue(value.getLongOpt()));
            }
        }
        SubtitleWriter<SubtitleFile> writer = new SubtitleWriterFactory().getInstance(subtitleType);
        writer.write(file, output);
    }
}
