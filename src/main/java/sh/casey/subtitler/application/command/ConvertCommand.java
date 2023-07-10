package sh.casey.subtitler.application.command;

import lombok.extern.slf4j.Slf4j;
import picocli.CommandLine.Command;
import sh.casey.subtitler.converter.SubtitleConverter;
import sh.casey.subtitler.converter.SubtitleConverterFactory;
import sh.casey.subtitler.model.SubtitleFile;
import sh.casey.subtitler.model.SubtitleType;
import sh.casey.subtitler.reader.SubtitleReader;
import sh.casey.subtitler.reader.SubtitleReaderFactory;
import sh.casey.subtitler.writer.SubtitleWriter;
import sh.casey.subtitler.writer.SubtitleWriterFactory;

@Slf4j
@Command(name = "convert", aliases = "c", description = "Convert a subtitle file from one format to another.", sortOptions = false, sortSynopsis = false)
public class ConvertCommand extends BaseCommand {

    @Override
    public void doRun() {
        final String input = getInput();
        final SubtitleType inputType = getInputType();
        String output = getOutput();
        final SubtitleType outputType = getOutputType();

        final SubtitleReader<SubtitleFile> reader = new SubtitleReaderFactory().getInstance(inputType);
        final SubtitleFile file = reader.read(input);
        final SubtitleConverter<SubtitleFile, SubtitleFile> converter = new SubtitleConverterFactory().getInstance(inputType, outputType);
        final SubtitleFile converted = converter.convert(file);
        final SubtitleWriter<SubtitleFile> writer = new SubtitleWriterFactory().getInstance(outputType);
        writer.write(converted, output);
    }
}
