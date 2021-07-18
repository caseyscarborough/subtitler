package sh.casey.subtitler.application.command;

import org.apache.commons.cli.CommandLine;
import sh.casey.subtitler.application.exception.InvalidCommandException;
import sh.casey.subtitler.converter.SubtitleConverter;
import sh.casey.subtitler.converter.SubtitleConverterFactory;
import sh.casey.subtitler.model.SubtitleFile;
import sh.casey.subtitler.model.SubtitleType;
import sh.casey.subtitler.reader.SubtitleReader;
import sh.casey.subtitler.reader.SubtitleReaderFactory;
import sh.casey.subtitler.writer.SubtitleWriter;
import sh.casey.subtitler.writer.SubtitleWriterFactory;

class ConvertCommand extends BaseCommand {

    public ConvertCommand(CommandLine cmd) {
        super(cmd);
    }

    @Override
    public void execute() {
        if (!cmd.hasOption('o')) {
            throw new InvalidCommandException("You must specify an output file.");
        }

        String inputFile = getInputFilename();
        SubtitleType inputType = getInputFileType();
        SubtitleType outputType = getOutputFileType();

        SubtitleReader<SubtitleFile> reader = new SubtitleReaderFactory().getInstance(inputType);
        SubtitleFile file = reader.read(inputFile);
        SubtitleConverter<SubtitleFile, SubtitleFile> converter = new SubtitleConverterFactory().getInstance(inputType, outputType);
        SubtitleFile convertedFile = converter.convert(file);
        SubtitleWriter<SubtitleFile> writer = new SubtitleWriterFactory().getInstance(outputType);
        writer.write(convertedFile, cmd.getOptionValue('o'));
    }
}
