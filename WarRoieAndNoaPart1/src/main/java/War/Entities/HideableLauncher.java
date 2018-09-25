package War.Entities;

public class HideableLauncher extends Launcher{
    public HideableLauncher() {
        super();
    }
    public HideableLauncher(String id) {
        super(id);
    }
    @Override
    public boolean isHidden() {
        return !isCurrentlyLaunching();
    }

    @Override
    public String toString() {
        return String.format("%s, Hideable",super.toString());
    }

}
