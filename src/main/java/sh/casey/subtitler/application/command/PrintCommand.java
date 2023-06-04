package sh.casey.subtitler.application.command;

import org.apache.commons.cli.CommandLine;
import sh.casey.subtitler.model.Subtitle;
import sh.casey.subtitler.model.SubtitleFile;
import sh.casey.subtitler.model.SubtitleType;
import sh.casey.subtitler.reader.SubtitleReader;
import sh.casey.subtitler.reader.SubtitleReaderFactory;

class PrintCommand extends BaseCommand {
    public PrintCommand(CommandLine cmd) {
        super(cmd);
    }

    @Override
    public void execute() {
        final String input = getInputFilename();
        final SubtitleType subtitleType = getInputFileType();
        final SubtitleReader<SubtitleFile> reader = new SubtitleReaderFactory().getInstance(subtitleType);
        final SubtitleFile file = reader.read(input);
        for (Subtitle subtitle : file.getSubtitles()) {
            System.out.println(subtitle.getStart() + " --> " + subtitle.getEnd() + "\n" + subtitle.getText() + "\n");
        }
    }
}
