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

    public ConvertCommand(final CommandLine cmd) {
        super(cmd);
    }

    @Override
    public void execute() {
        if (!cmd.hasOption('o')) {
            throw new InvalidCommandException("You must specify an output file.");
        }

        final String inputFile = getInputFilename();
        final SubtitleType inputType = getInputFileType();
        final SubtitleType outputType = getOutputFileType();

        final SubtitleReader<SubtitleFile> reader = new SubtitleReaderFactory().getInstance(inputType);
        final SubtitleFile file = reader.read(inputFile);
        final SubtitleConverter<SubtitleFile, SubtitleFile> converter = new SubtitleConverterFactory().getInstance(inputType, outputType);
        final SubtitleFile convertedFile = converter.convert(file);
        final SubtitleWriter<SubtitleFile> writer = new SubtitleWriterFactory().getInstance(outputType);
        writer.write(convertedFile, cmd.getOptionValue('o'));
    }
}
