package sh.casey.subtitler.application.command;

import org.apache.commons.cli.CommandLine;
import sh.casey.subtitler.condenser.SubtitleCondenser;
import sh.casey.subtitler.condenser.SubtitleCondenserFactory;
import sh.casey.subtitler.model.SubtitleFile;
import sh.casey.subtitler.model.SubtitleType;
import sh.casey.subtitler.reader.SubtitleReader;
import sh.casey.subtitler.reader.SubtitleReaderFactory;
import sh.casey.subtitler.writer.SubtitleWriter;
import sh.casey.subtitler.writer.SubtitleWriterFactory;

class CondenseCommand extends BaseCommand {

    public CondenseCommand(final CommandLine cmd) {
        super(cmd);
    }

    public void execute() {
        final String input = getInputFilename();
        final String output = getOutputFilename();
        final SubtitleType type = getInputFileType();
        final SubtitleReader<SubtitleFile> reader = new SubtitleReaderFactory().getInstance(type);
        final SubtitleFile subtitleFile = reader.read(input);
        final SubtitleCondenser<SubtitleFile> condenser = new SubtitleCondenserFactory().getInstance(type);
        final SubtitleFile condensed = condenser.condense(subtitleFile);
        final SubtitleWriter<SubtitleFile> writer = new SubtitleWriterFactory().getInstance(type);
        writer.write(condensed, output);
    }
}
