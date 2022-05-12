package sh.casey.subtitler.application.command;

import org.apache.commons.cli.CommandLine;
import sh.casey.subtitler.application.exception.InvalidCommandException;
import sh.casey.subtitler.model.SubtitleFile;
import sh.casey.subtitler.model.SubtitleType;
import sh.casey.subtitler.reader.SubtitleReader;
import sh.casey.subtitler.reader.SubtitleReaderFactory;
import sh.casey.subtitler.renumberer.SubtitleRenumberer;
import sh.casey.subtitler.renumberer.SubtitleRenumbererFactory;
import sh.casey.subtitler.writer.SubtitleWriter;
import sh.casey.subtitler.writer.SubtitleWriterFactory;

class RenumberCommand extends BaseCommand {

    public RenumberCommand(final CommandLine cmd) {
        super(cmd);
    }

    @Override
    public void execute() {
        final String input = getInputFilename();
        final String output = getOutputFilename();
        final SubtitleType subtitleType = getInputFileType();
        final SubtitleReader<SubtitleFile> reader = new SubtitleReaderFactory().getInstance(subtitleType);
        final SubtitleFile file = reader.read(input);
        final SubtitleRenumberer<SubtitleFile> renumberer = new SubtitleRenumbererFactory().getInstance(subtitleType);
        try {
            final String start = cmd.getOptionValue("rs", "1");
            renumberer.renumber(file, Integer.parseInt(start));
            final SubtitleWriter<SubtitleFile> writer = new SubtitleWriterFactory().getInstance(subtitleType);
            writer.write(file, output);
        } catch (NumberFormatException e) {
            throw new InvalidCommandException("The --renumber-start (-rs) parameter must be a number (defaults to 1).");
        }
    }
}
