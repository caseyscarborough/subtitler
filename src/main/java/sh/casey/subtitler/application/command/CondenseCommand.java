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

    public CondenseCommand(CommandLine cmd) {
        super(cmd);
    }

    public void execute() {
        String input = getInputFilename();
        String output = getOutputFilename();
        SubtitleType type = getInputFileType();
        SubtitleReader<SubtitleFile> reader = new SubtitleReaderFactory().getInstance(type);
        SubtitleFile subtitleFile = reader.read(input);
        SubtitleCondenser<SubtitleFile> condenser = new SubtitleCondenserFactory().getInstance(type);
        SubtitleFile condensed = condenser.condense(subtitleFile);
        SubtitleWriter<SubtitleFile> writer = new SubtitleWriterFactory().getInstance(type);
        writer.write(condensed, output);
    }
}
