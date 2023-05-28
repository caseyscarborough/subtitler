package sh.casey.subtitler.application.command;

class VersionCommand implements ApplicationCommand {
    @Override
    public void execute() {
        System.out.println("Subtitler v" + this.getClass().getPackage().getImplementationVersion());
    }
}
