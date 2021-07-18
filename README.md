# subtitler 💬

This is a cross-platform, full-featured Java library and CLI utility for working with subtitles.

It currently supports the following subtitle types:

- `.srt` - Most common subtitle format.
- `.ass` - SubStation Alpha
- `.ttml` - Timed Text Markup Language
- `.dfxp` - Commonly used from subtitles on Netflix.

## Installation

### Download

Download the `.zip` file from [the latest release](https://github.com/caseyscarborough/subtitler/releases/latest). Unzip the file and add the `bin` directory to your `PATH` environment variable.

### Building from Source

#### Requirements

- Java 8+

#### Building
Clone the repository, build using Gradle, and unzip the built `.zip` file. Add the `bin` directory in the resulting extracted folder to your `PATH` environment variable.

```bash
git clone https://github.com/caseyscarborough/subtitler.git
./gradlew distZip
unzip build/distributions/subtitler.zip
export PATH="$(pwd)/subtitler/bin:$PATH"
```

## CLI Usage

You can retrieve the usage by running `subtitler -h` or `subtitler --help`.

```
usage: subtitler <options>
 -a,--after <arg>          Only shift subtitles after a specific time in
                           the file (format HH:mm:ss,SSS)
 -b,--before <arg>         Only shift subtitles before a specific time in
                           the file (format HH:mm:ss,SSS)
 -bf,--bottom <arg>        Bottom file for dual sub creation
 -c,--convert              Convert file from one format to another
 -cn,--condense            Condense a subtitle file by putting common
                           lines (of the same start/end times) into a
                           single subtitle line.
 -d,--dual-subs            Create Dual Sub file from two input files
 -h,--help                 Display this help menu
 -i,--input <arg>          Input filename (required when using -s)
 -it,--input-type <arg>    The type of the input file, options are [ASS,
                           SRT, TTML, DFXP]
 -m,--matches <arg>        Used to specify text to match a specific
                           subtitle to shift
 -n,--number <arg>         The number of a specific subtitle to shift
 -o,--output <arg>         Output filename
 -ot,--output-type <arg>   The type for the output file, options are [ASS,
                           SRT, TTML, DFXP]
 -r,--renumber             Renumber a subtitle file starting from 1
                           (requires -i flag). Useful for when splitting
                           subtitle files that were previously multiple
                           episodes.
 -s,--shift                Shift timing on subtitles
 -sm,--shift-mode <arg>    Shift mode, default is 'FROM_TO'. Options are
                           FROM (Shifts only the 'from' time in each
                           subtitle), FROM_TO (Shifts the 'from' and 'to'
                           times in the subtitle (default)), TO (Shifts
                           only the 'to' time in each subtitle)
 -t,--time <arg>           Time in milliseconds to shift subtitles
                           (required when using -s)
 -tf,--top <arg>           Top file for dual sub creation
```

### Shifting

The CLI tool has multiple different options for shifting subtitles.

#### Shift All Subtitles

You can shift subtitles using the `-s`/`--shift` flag. This also require the `-t`/`--time` flag for specifying the time in milliseconds you would like to shift.

```bash
# Shift the subtitle.srt file back 10 seconds.
subtitler --shift --input subtitle.srt --time -10000
```

If you don't specify an output file using `-o`/`--output` then the file will be shifted in place.

#### Shift Subtitles Before or After a Specific Time

You can use the `-a`/`--after` or `-b`/`--before` flags to only shift subtitles before or after a specific time. The format for the time is `HH:mm:ss,SSS`.

```bash
# Shift all subtitles after 5m30s forwards 1.5s
subtitler --shift --input subtitle.ass --after 00:05:30,000 --time 1500

# Shift all subtitles before 10m backwards 10s
subtitler --shift --input subtitle.srt --before 00:10:00,000 --time -10000
```

#### Shift a Specific Subtitle Number

You can use the `-n` or `--number` flag to shift only a specific subtitle number.

#### Shifting Only the Beginning or End Times

There are three different shift modes, `FROM_TO`, `FROM`, and `TO`. `FROM_TO` is the default and will shift both the "start" and "end" times of each subtitle line. If you want to shift only the start times, you can use `FROM` and only the end times you can use `TO`. This is useful for extending the length of each subtitle if they are all too short, for example.

This is specified using `-sm` or `--shift-mode`.

#### Shift Subtitles Matching Specific Text

You can use `-m` or `--matches` to shift a subtitle that matches a specific text.

```bash
# Shift the subtitle "This text is off" forwards by 1 second.
subtitler --shift --input subtitles.srt --matches "This text is off." --time 1000
```

### Converting

You can convert subtitles between formats easily using the `-c` or `--convert` flags. You only need to specify the input and output files using `-i`/`--input` and `-o`/`--output`.

```bash
# Convert file from .ass to .srt
subtitler --convert --input input.ass --output output.srt
```

By default the tool will determine the subtitle type based on the filename extension, but if your files don't have extensions or the extensions do not match the subtitle type you can specify the input and output types using `-it`/`--input-type` and `-ot`/`--output-type`.

```bash
# Convert .txt subtitles from .ass to .srt
subtitler --convert \
  --input subtitles.txt \
  --input-type ASS \
  --output output.txt \
  --output-type SRT
```

### Renumbering

You can renumber all subtitles in a file so that they are in order using the `r`/`--renumber` flag. This is useful if you have combined two subtitle files, if you split a multi-episode file into separate files, or if you've added new subtitle lines anywhere in the middle of other lines.

```bash
# Renumber all subtitles in subtitles.srt so they are in order
subtitler --renumber --input subtitles.srt
```

### Condensing

You can condense a subtitle file using `-cn`/`--condense`. This will put all lines that have been separated onto a single line. This is useful when you have a lot of subtitles that all have the same start and end times, but they've been added separately in the subtitle file.

```bash
subtitler --condense --input subtitles.srt
```

### Create Dual Subtitle Files

You can create a "dual-subtitle" file using `-d`/`--dual-subs`. This will create an `.ass` file that will have subtitles on the top and bottom at the same time. This is especially useful for language learners that would like to put the translation on the bottom and the subtitles for their target language on the top, for example.

This requires you to specify a "top" file and a "bottom" file using `-tf`/`--top-file` and `-bf`/`--bottom-file`. You must also specify the output file.

```java
subtitler --dual-subs
  --top-file japanese.srt
  --bottom-file english.srt
  --output subtitles.ja-en.ass
```

## API Usage

This has been developed so that it can be integrated easily into other applications. You can work with all of the aforementioned features directly from Java.

### Including the Dependency

Add the dependency to your `build.gradle` using either Jitpack or GitHub Packages:

#### Jitpack

```groovy
repositories {
    maven { url 'https://jitpack.io' }
}

dependencies {
    implementation 'com.github.caseyscarborough:subtitler:1.0.1'
}
```

#### GitHub

```groovy
repositories {
    maven { url 'https://maven.pkg.github.com/caseyscarborough/subtitler' }
}

dependencies {
    implementation 'sh.casey.subtitler:subtitler:1.0.1'
}
```

### Get Subtitle Type from Filename

If you don't already know the subtitle type you're working with, you can use the `SubtitleType#find` method to determine the type based on file extension. You will need the subtitle type for working with all other aspects of the API, so this is the first thing you'll want to retrieve. 

```java
final String filename = "subtitles.ass";
final SubtitleType type = SubtitleType.find(filename.substring(filename.lastIndexOf(".")));
```

### Reading Subtitles

Each subtitle type has its own implementation of the [`SubtitleFile` interface](https://github.com/caseyscarborough/subtitler/blob/master/src/main/java/sh/casey/subtitler/model/SubtitleFile.java). You should work with the interface instead of using the subclasses directly.

```java
SubtitleReaderFactory factory = new SubtitleReaderFactory();
SubtitleReader reader = factory.getInstance(SubtitleType.SRT);

// This will be an SrtSubtitleFile under the hood.
SubtitleFile file = reader.read("/path/to/subtitle/file.srt");
```

### Shifting Subtitles

Once you've gotten your `SubtitleFile` you can shift the subtitles. You will want to use the `ShiftConfig` class to configure how you want to shift, similar to how you would pass flags into the CLI in the above instructions.

```java
ShiftConfig config = ShiftConfig.builder()
    .input("/path/to/input.srt")   // input file path
    .output("/path/to/output.srt") // output file path
    .ms(10_000)                    // number of milliseconds to shift
    .before("00:05:00,000")        // (optional) the time to shift before
    .after("00:02:00,000")         // (optional) the time to shift after
    .number(number)                // (optional) the specific subtitle number to shift
    .matches("some text")          // (optional) shift only subtitles that match specific text
    .shiftMode(ShiftMode.FROM_TO)  // (optional) defaults to FROM_TO
    .build();

SubtitleShifterFactory factory = new SubtitleShifterFactory();
SubtitleShifter<SubtitleFile> shifter = factory.getInstance(SubtitleType.SRT);
shifter.shift(config);
```

Afterwards the subtitle should be shifted and saved to the output file.

### Writing Subtitles

If you are programmatically creating subtitles and want to write the output to a subtitle file you can do this similarly to reading:

```java
SrtSubtitleFile srt = new SrtSubtitleFile();
// programmatically add subtitles

SubtitleWriterFactory factory = new SubtitleWriterFactory();
SubtitleWriter<SubtitleFile> writer = factory.getInstance(SubtitleType.SRT);
writer.write(srt, "/path/to/output.srt");
```

### Converting Subtitles

Subtitles can be converted similarly. You need a `SubtitleFile`s and to know its type, along with the type you want to convert to.

```java
SubtitleFile srt = reader.read("/path/to/some.srt");

SubtitleConverterFactory factory = new SubtitleConverterFactory();

// The first parameter is the current type and the second is the type you're converting to.
SubtitleConverter<SubtitleFile, SubtitleFile> converter = factory.getInstance(SubtitleType.SRT, SubtitleType.ASS);

// This will be an AssSubtitleFile under the hood.
SubtitleFile converted = converter.convert(srt);

// proceed to write the file, etc.
```

### Renumbering Subtitles

Renumbering works similarly to reading and writing.

```java
// Read your file
SubtitleReader<SubtitleFile> reader = new SubtitleReaderFactory().getInstance(SRT);
SubtitleFile file = reader.read(input);

// Renumber
SubtitleRenumbererFactory factory = new SubtitleRenumbererFactory();
SubtitleRenumberer<SubtitleFile> renumberer = factory.getInstance(subtitleType);
SubtitleFile file = renumberer.renumber(file);

// proceed to write, convert, etc.
```

### Condensing Subtitles

The same pattern applies here:

```java
SubtitleFile file = reader.read("/some/file.srt");
SubtitleCondenser<SubtitleFile> condenser = new SubtitleCondenserFactory().getInstance(SRT);
SubtitleFile condensed = condenser.condense(file);
```

## Known Issues

Not all features work with every subtitle type. Most of the functions can be performed by converting to another subtitle type first that is supported with the feature you are trying to use. For example, if you would like to shift a `.dfxp` file, then convert it to `.ass` first.

Currently here are the supported functions for each subtitle type:

- Reading
  * `.ass`
  * `.srt`
  * `.dfxp`
  * `.ttml`
- Writing
  * `.ass`
  * `.srt`
- Converting
  * From `.srt` to `.ass`
  * From `.ttml` to `.ass`
  * From `.dfxp` to `.ass`
- Renumbering
  * `.ass`
  * `.srt`
- Shifting  
  * `.ass`
  * `.srt`
- Condensing
  * `.srt`

I plan to implement more types in the future.  

## Issues or Contributing

If you find an issue, have a question, or would like to request a feature please [open an issue](https://github.com/caseyscarborough/subtitler/issues).

Please also feel free to contribute by [opening a pull request](https://github.com/caseyscarborough/subtitler/pulls).

## TODO

- Add to package managers like Chocolatey and Homebrew.
- Implement features for other subtitle types.
