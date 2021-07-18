package sh.casey.subtitler.application.command;

import org.apache.commons.cli.CommandLine;
import sh.casey.subtitler.model.SubtitleFile;
import sh.casey.subtitler.model.SubtitleType;
import sh.casey.subtitler.reader.SubtitleReader;
import sh.casey.subtitler.reader.SubtitleReaderFactory;
import sh.casey.subtitler.renumberer.SubtitleRenumberer;
import sh.casey.subtitler.renumberer.SubtitleRenumbererFactory;
import sh.casey.subtitler.writer.SubtitleWriter;
import sh.casey.subtitler.writer.SubtitleWriterFactory;

class RenumberCommand extends BaseCommand {

    public RenumberCommand(CommandLine cmd) {
        super(cmd);
    }

    @Override
    public void execute() {
        String input = getInputFilename();
        String output = getOutputFilename();
        SubtitleType subtitleType = getInputFileType();
        SubtitleReader<SubtitleFile> reader = new SubtitleReaderFactory().getInstance(subtitleType);
        SubtitleFile file = reader.read(input);
        SubtitleRenumberer<SubtitleFile> renumberer = new SubtitleRenumbererFactory().getInstance(subtitleType);
        renumberer.renumber(file);
        SubtitleWriter<SubtitleFile> writer = new SubtitleWriterFactory().getInstance(subtitleType);
        writer.write(file, output);
    }
}
