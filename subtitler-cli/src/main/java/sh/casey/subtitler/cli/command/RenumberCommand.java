package sh.casey.subtitler.cli.command;

import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import sh.casey.subtitler.model.SubtitleFile;
import sh.casey.subtitler.model.SubtitleType;
import sh.casey.subtitler.reader.SubtitleReader;
import sh.casey.subtitler.reader.SubtitleReaderFactory;
import sh.casey.subtitler.renumberer.SubtitleRenumberer;
import sh.casey.subtitler.renumberer.SubtitleRenumbererFactory;
import sh.casey.subtitler.writer.SubtitleWriter;
import sh.casey.subtitler.writer.SubtitleWriterFactory;

@Command(name = "renumber", aliases = "r", description = "Renumbers the subtitles in a subtitle file so they are ordered.", sortOptions = false, sortSynopsis = false)
public class RenumberCommand extends BaseCommand {

    @Option(names = {"-s", "--start"}, description = "The number to start renumbering from (defaults to 1).", required = true, defaultValue = "1")
    private int start;

    @Override
    public void doRun() {
        final String input = getInput();
        final String output = getOutput();
        final SubtitleType subtitleType = getInputType();
        final SubtitleType outputType = getOutputType();
        final SubtitleReader<SubtitleFile> reader = new SubtitleReaderFactory().getInstance(subtitleType);
        final SubtitleFile file = reader.read(input);
        final SubtitleRenumberer<SubtitleFile> renumberer = new SubtitleRenumbererFactory().getInstance(subtitleType);
        renumberer.renumber(file, start);
        final SubtitleWriter<SubtitleFile> writer = new SubtitleWriterFactory().getInstance(outputType);
        writer.write(file, output);
    }
}
