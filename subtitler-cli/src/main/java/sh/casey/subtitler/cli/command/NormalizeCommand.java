package sh.casey.subtitler.cli.command;

import lombok.extern.slf4j.Slf4j;
import picocli.CommandLine.Command;
import sh.casey.subtitler.util.FileUtils;

import java.text.Normalizer;

@Slf4j
@Command(name = "normalize", aliases = "n", description = "Normalize subtitles (convert half-width kana to full width, etc.)", sortOptions = false, sortSynopsis = false)
public class NormalizeCommand extends BaseCommand {

    @Override
    public void doRun() {
        final String input = getInput();
        final String output = getOutput();

        log.info("Normalizing text in file {}", input);
        String text = FileUtils.readFile(input);
        text = Normalizer.normalize(text, Normalizer.Form.NFKC);
        FileUtils.writeFile(output, text);
        log.info("Successfully normalized text and wrote output to {}", output);
    }
}
