package sh.casey.subtitler.application.command;

import picocli.CommandLine.Command;
import sh.casey.subtitler.model.Subtitle;
import sh.casey.subtitler.model.SubtitleFile;
import sh.casey.subtitler.model.SubtitleType;
import sh.casey.subtitler.reader.SubtitleReader;
import sh.casey.subtitler.reader.SubtitleReaderFactory;

@Command(name = "print", aliases = "p", description = "Parses, formats, and prints the contents of a subtitle file to the console.")
public class PrintCommand extends BaseCommand {

    @Override
    public void run() {
        final String input = getInput();
        final SubtitleType subtitleType = getInputType();
        final SubtitleReader<SubtitleFile> reader = new SubtitleReaderFactory().getInstance(subtitleType);
        final SubtitleFile file = reader.read(input);
        for (Subtitle subtitle : file.getSubtitles()) {
            System.out.println(subtitle.getStart() + " --> " + subtitle.getEnd() + "\n" + subtitle.getText() + "\n");
        }
    }
}
