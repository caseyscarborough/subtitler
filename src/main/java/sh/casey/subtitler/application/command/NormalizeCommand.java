package sh.casey.subtitler.application.command;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.cli.CommandLine;
import sh.casey.subtitler.application.exception.InvalidCommandException;
import sh.casey.subtitler.util.FileUtils;

import java.text.Normalizer;

@Slf4j
class NormalizeCommand extends BaseCommand {
    public NormalizeCommand(CommandLine cmd) {
        super(cmd);
    }

    @Override
    public void execute() {
        if (!cmd.hasOption('i')) {
            throw new InvalidCommandException("Input file is required for normalization");
        }

        final String input = cmd.getOptionValue('i');
        final String output = cmd.hasOption('o') ? cmd.getOptionValue('o') : input;

        log.info("Normalizing text in file {}", input);
        String text = FileUtils.readFile(input);
        text = Normalizer.normalize(text, Normalizer.Form.NFKC);
        FileUtils.writeFile(output, text);
        log.info("Successfully normalized text and wrote output to {}", output);
    }
}
