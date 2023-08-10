package sh.casey.subtitler.cli.provider;

import picocli.CommandLine;

public class VersionProvider implements CommandLine.IVersionProvider {
    @Override
    public String[] getVersion() {
        String version = this.getClass().getPackage().getImplementationVersion();
        return new String[]{"Subtitler " + (version != null ? version : "development build")};
    }
}
