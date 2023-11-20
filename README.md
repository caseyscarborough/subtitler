# subtitler 💬

![Build](https://github.com/caseyscarborough/subtitler/actions/workflows/release.yml/badge.svg)

This is a cross-platform, full-featured Java library and CLI utility for working with subtitles.

It currently supports the following subtitle types:

- `.srt` - [SubRip Subtitle](https://en.wikipedia.org/wiki/SubRip) (most common format)
- `.ass` - [Advanced SubStation Alpha](https://fileformats.fandom.com/wiki/SubStation_Alpha) (SSAv4+)
- `.ttml` - [Timed Text Markup Language](https://www.speechpad.com/captions/ttml)
- `.dfxp` - [Distribution Format Exchange Profile](https://www.speechpad.com/captions/dfxp)
- `.vtt` - [WebVTT](https://en.wikipedia.org/wiki/WebVTT)

It supports the following features for each subtitle type:

|         | Reading | Writing | Renumbering | Shifting | Filtering | Condensing |
|---------|---------|---------|-------------|----------|-----------|------------|
| `.srt`  | ✅       | ✅       | ✅           | ✅        | ✅         | ✅          |
| `.ass`  | ✅       | ✅       | ✅           | ✅        | ✅         | ✅          |
| `.ttml` | ✅       |         |             |          |           |            |
| `.dfxp` | ✅       |         |             |          |           |            |
| `.vtt`  | ✅       |         |             | ✅        |           |            |

Most of the functions can be performed by converting to another subtitle type first that is supported with the feature
you are trying to use. For example, if you would like to shift a `.vtt` file, then convert it to `.srt` or `.ass` first.

For conversions, the following conversions are available:

| ⬇️ From - To ➡️ | `.srt` | `.ass` | `.ttml` | `.dfxp` | `.vtt` |
|-----------------|--------|--------|---------|---------|--------|
| `.srt`          | N/A    | ✅      | ❌       | ❌       | ❌      |
| `.ass`          | ✅      | N/A    | ❌       | ❌       | ❌      |
| `.ttml`         | ✅      | 🔄     | N/A     | ❌       | ❌      |
| `.dfxp`         | 🔄     | ✅      | ❌       | N/A     | ❌      |
| `.vtt`          | ✅      | 🔄     | ❌       | ❌       | N/A    |

Legend:

- ✅ - Available natively
- 🔄 - Available transitively, meaning you need to convert to another format first, for example `.dfxp -> .srt`
  requires `.dfxp -> .ass` then `.ass -> .srt`.
- ❌ - Not available currently. Feel free to [open an issue](https://github.com/caseyscarborough/subtitler/issues) if one
  is missing that you need.
- `N/A` - Doesn't apply (i.e. converting `.srt` to `.srt` is pointless).

The original intention of this library/utility was to convert other subtitle types to `.ass`, so converting to `.ass`
first will give you the most support.

## Table of Contents

- [Installation](#installation)
    * [Homebrew (Mac OS X)](#homebrew-mac-os-x)
    * [Download](#download)
    * [Building from Source](#building-from-source)
        + [Requirements](#requirements)
        + [Building](#building)
- [CLI Usage](#cli-usage)
- [API Usage](#api-usage)
    * [Including the Dependency](#including-the-dependency)
        + [Jitpack](#jitpack)
        + [GitHub](#github)
    * [Get Subtitle Type from Filename](#get-subtitle-type-from-filename)
    * [Reading Subtitles](#reading-subtitles)
    * [Shifting Subtitles](#shifting-subtitles)
    * [Writing Subtitles](#writing-subtitles)
    * [Converting Subtitles](#converting-subtitles)
    * [Renumbering Subtitles](#renumbering-subtitles)
    * [Condensing Subtitles](#condensing-subtitles)
    * [Filtering Subtitles](#filtering-subtitles)
    * [Creating Dual-Sub File](#creating-dual-sub-file)
- [Issues or Contributing](#issues-or-contributing)
- [TODO](#todo)

## Installation

### Homebrew (Mac OS X)

```bash
brew tap caseyscarborough/subtitler
brew install subtitler
subtitler -v
```

> Note: This uses a Homebrew tap currently because this project does not have enough notoriety (30 forks or 75 stars) to
> be merged into homebrew-core. You can see the source code for the
> tap [here](https://github.com/caseyscarborough/homebrew-subtitler/blob/master/Formula/subtitler.rb).

### Download

Download the `.zip` file from [the latest release](https://github.com/caseyscarborough/subtitler/releases/latest). Unzip
the file and add the `bin` directory to your `PATH` environment variable.

### Building from Source

#### Requirements

- Java 8+

#### Building

Clone the repository, build using Gradle, and unzip the built `.zip` file. Add the `bin` directory in the resulting
extracted folder to your `PATH` environment variable.

```bash
git clone https://github.com/caseyscarborough/subtitler.git
./gradlew -b subtitler-cli/build.gradle distZip
unzip subtitler-cli/build/distributions/subtitler-cli.zip
export PATH="$(pwd)/subtitler-cli/bin:$PATH"
subtitler-cli -V
```

## CLI Usage

You can retrieve the usage by running `subtitler -h` or `subtitler --help`.

```
subtitler -h
Usage: subtitler [-hV] [COMMAND]
Subtitler is a command line tool for manipulating subtitle files.
  -h, --help      Show this help message and exit.
  -V, --version   Print version information and exit.
Commands:
  align, a      Align subtitles within a specific threshold to a reference file. This will adjust the start/end times of subtitles to closely match the reference file.
  condense      Condense a subtitle file by putting common lines (of the same start/end times) into a single subtitle line. Also combines adjacent subtitles having the same text.
  convert, c    Convert a subtitle file from one format to another.
  dual, d       Create a dual subtitle file from two subtitle files.
  filter, f     Filter a subtitle file by removing lines that match (or don't match) a specific criteria.
  normalize, n  Normalize subtitles (convert half-width kana to full width, etc.)
  print, p      Parses, formats, and prints the contents of a subtitle file to the console.
  renumber, r   Renumbers the subtitles in a subtitle file so they are ordered.
  shift, s      Shifts the subtitles by the specified amount of time.
```

You can get details for any of the commands by running `subtitler <command> -h`, for example:

```
➜ subtitler shift -h
Usage: subtitler shift [-h] -i=<file> [-I=<type>] [-o=<file>] [-O=<type>] [--trace] -t=<time> [-b=<time|number>] [-a=<time|number>] [-n=<number>] [-c=<text>] [-m=<mode>]
Shifts the subtitles by the specified amount of time.
  -h, --help                   Display this help message.
  -i, --input=<file>           The input file to read from.
  -I, --input-type=<type>      The type of subtitle file to read from. If not specified, the file extension will be used. Valid options: ASS, SRT, TTML, DFXP, VTT, SSA
  -o, --output=<file>          The output file to write to. If not specified, the input file will be used.
  -O, --output-type=<type>     The type of subtitle file to write to. If not specified, the input type will be used. Valid options: ASS, SRT, TTML, DFXP, VTT, SSA
      --trace                  Enable trace logging.
  -t, --time=<time>            The amount of time to shift the subtitles in milliseconds.
  -b, --before=<time|number>   Only shift subtitles in the file before this time or subtitle number (format HH:mm:ss,SSS for time or DDD for number).
  -a, --after=<time|number>    Only shift subtitles in the file after this time or subtitle number (format HH:mm:ss,SSS for time or DDD for number).
  -n, --number=<number>        The number of a specific subtitle to shift.
  -c, --contains=<text>        The text (or part of the text) of subtitle to shift.
  -m, --mode=<mode>            The mode to use when shifting the subtitles. Valid values are: FROM (shifts only the start time in each subtitle), FROM_TO (shifts the start and end times in the
                                 subtitle (default)), TO (shifts only the end time in each subtitle).
```

## API Usage

This has been developed so that it can be integrated easily into other applications. You can work with all of the
aforementioned features directly from Java.

### Including the Dependency

Add the dependency to your `build.gradle` using either Jitpack or GitHub Packages:

#### Jitpack

```groovy
repositories {
    maven { url 'https://jitpack.io' }
}

dependencies {
    implementation 'com.github.caseyscarborough:subtitler:2.0.0'
}
```

#### GitHub

```groovy
repositories {
    maven { url 'https://maven.pkg.github.com/caseyscarborough/subtitler' }
}

dependencies {
    implementation 'sh.casey.subtitler:subtitler:2.0.0'
}
```

### Get Subtitle Type from Filename

If you don't already know the subtitle type you're working with, you can use the `SubtitleType#find` method to determine
the type based on file extension. You will need the subtitle type for working with all other aspects of the API, so this
is the first thing you'll want to retrieve.

```java
final String filename = "subtitles.ass";
final String extension = filename.substring(filename.lastIndexOf("."));
final SubtitleType type = SubtitleType.find(extension);
```

### Reading Subtitles

Each subtitle type has its own implementation of
the [`SubtitleFile` interface](https://github.com/caseyscarborough/subtitler/blob/master/src/main/java/sh/casey/subtitler/model/SubtitleFile.java).
You should work with the interface instead of using the subclasses directly.

```java
SubtitleReaderFactory factory = new SubtitleReaderFactory();
SubtitleReader reader = factory.getInstance(SubtitleType.SRT);

// This will be an SrtSubtitleFile under the hood.
SubtitleFile file = reader.read("/path/to/subtitle/file.srt");
```

### Shifting Subtitles

Once you've gotten your `SubtitleFile` you can shift the subtitles. You will want to use the `ShiftConfig` class to
configure how you want to shift, similar to how you would pass flags into the CLI in the above instructions.

```java
ShiftConfig config=ShiftConfig.builder()
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

// proceed to write the file, etc.
```

Afterwards the subtitle should be shifted and saved to the output file.

### Writing Subtitles

If you are programmatically creating subtitles and want to write the output to a subtitle file you can do this similarly
to reading:

```java
SrtSubtitleFile srt=new SrtSubtitleFile();
// programmatically add subtitles
SubtitleWriterFactory factory = new SubtitleWriterFactory();
SubtitleWriter<SubtitleFile> writer = factory.getInstance(SubtitleType.SRT);
writer.write(srt, "/path/to/output.srt");

// Alternatively use a java.io.Writer
try(StringWriter sw = new StringWriter()){
    writer.write(srt, sw);
    System.out.println(sw.toString());
}
```

### Converting Subtitles

Subtitles can be converted similarly. You need a `SubtitleFile`s and to know its type, along with the type you want to
convert to.

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
renumberer.renumber(file);

// proceed to write, convert, etc.
```

### Condensing Subtitles

Condensing a subtitle file will concatenate duplicate subtitle lines that are next to each
other into a single subtitle, for example, if the subtitle file has the following subtitles:

```
00:00:01,000 --> 00:00:03,234
Hello world!

00:00:03,234 --> 00:00:04,545
Hello world!

00:00:04,545 --> 00:00:05,412
Hello world!
```

Then the result after condensing would be:

```
00:00:01,000 --> 00:00:05,412
Hello world!
```

The API is similar to other methods:

```java
SubtitleFile file = reader.read("/some/file.srt");
SubtitleCondenser<SubtitleFile> condenser = new SubtitleCondenserFactory().getInstance(SRT);
SubtitleFile condensed = condenser.condense(file);
```

### Filtering Subtitles

You can also filter to remove subtitles. There are multiple different filters you can use:

- `after` - filters subtitles after a given time.
- `before` - filters subtitles before a given time.
- `text` - filters subtitles that exactly match a specific text.
- `styles` - filters subtitles that have a specific style (.ass/.ssa only)
- `matches` - filters subtitles matching a regex.

Filtering has two modes, `OMIT` and `RETAIN`. `OMIT` will remove any subtitles matched by
the filter, and `RETAIN` will invert this, retaining the subtitles matched by the filter.

```java
SubtitleFilterer<SubtitleFile> filterer = new SubtitleFiltererFactory().getInstance(SubtitleType.ASS);
Map<FilterType, List<String>> filters = new EnumMap<>(FilterType.class);
// filter subtitles before/after 30 minute mark
filters.put(FilterType.BEFORE, List.of("00:30:00.000"));
filters.put(FilterType.AFTER, List.of("00:30:00.000"));

// remove subtitles that are only a music note
filters.put(FilterType.TEXT, List.of("♪〜", "〜♪"));

// remove subtitles matching a specific Aegisub style
filters.put(FilterType.STYLE, List.of("OP", "ED", "Songs"));

FilterMode mode = FilterMode.OMIT;
filterer.filter(file, filters, mode);

// Optionally you can pass a "threshold" as the final parameter that
// will prevent the filtering if more subtitles would be removed
// than the threshold.

// Filter, only if 10 or less subtitles would be removed:
filterer.filter(file, filters, mode, 10);
```

### Creating Dual-Sub File

You can programmatically create a dual-sub file using the following method:

```java
SubtitleFile top = reader.read("/path/to/top.srt");
SubtitleFile bottom = reader.read("/path/to/bottom.ass");
DualSubtitleCreator creator = new DualSubtitleCreator();
SubtitleFile dual = creator.create(top,bottom);

// proceed to write the file
```

You can customize the output with the `DualSubtitleConfig` class.

```java
DualSubtitleConfig config = DualSubtitleConfig.builder()
    .keepTopStyles(true)
    .align(true)
    .topStyle(StyleConfig.FONT, "Menlo")
    .topStyle(StyleConfig.OUTLINE, "3")
    // ...
    .build();

SubtitleFile dual = creator.create(top,bottom);
```

## Issues or Contributing

If you find an issue, have a question, or would like to request a feature
please [open an issue](https://github.com/caseyscarborough/subtitler/issues).

Please also feel free to contribute by [opening a pull request](https://github.com/caseyscarborough/subtitler/pulls).
