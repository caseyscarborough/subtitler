package sh.casey.subtitler.cli.command;

import picocli.CommandLine.Command;
import sh.casey.subtitler.condenser.SubtitleCondenser;
import sh.casey.subtitler.condenser.SubtitleCondenserFactory;
import sh.casey.subtitler.model.SubtitleFile;
import sh.casey.subtitler.model.SubtitleType;
import sh.casey.subtitler.reader.SubtitleReader;
import sh.casey.subtitler.reader.SubtitleReaderFactory;
import sh.casey.subtitler.writer.SubtitleWriter;
import sh.casey.subtitler.writer.SubtitleWriterFactory;

@Command(name = "condense", description = "Condense a subtitle file by putting common lines (of the same start/end times) into a single subtitle line. Also combines adjacent subtitles having the same text.", sortOptions = false, sortSynopsis = false)
public class CondenseCommand extends BaseCommand {

    @Override
    public void doRun() {
        final String input = getInput();
        final String output = getOutput();
        final SubtitleType type = getInputType();
        final SubtitleReader<SubtitleFile> reader = new SubtitleReaderFactory().getInstance(type);
        final SubtitleFile subtitleFile = reader.read(input);
        final SubtitleCondenser<SubtitleFile> condenser = new SubtitleCondenserFactory().getInstance(type);
        final SubtitleFile condensed = condenser.condense(subtitleFile);
        final SubtitleWriter<SubtitleFile> writer = new SubtitleWriterFactory().getInstance(getOutputType());
        writer.write(condensed, output);
    }
}
