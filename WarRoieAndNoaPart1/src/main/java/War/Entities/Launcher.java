package War.Entities;


import java.util.HashMap;
import java.util.concurrent.atomic.AtomicBoolean;

public class Launcher extends Destructible {
    private static int idGenerator = 101;
    private AtomicBoolean currentlyLaunching = new AtomicBoolean();

    private HashMap<String, Missile> missiles;

    public Launcher() {
        super( "L" + idGenerator++);
        missiles = new HashMap<>();
    }

    public Launcher(String id) {
        super(id);
        missiles = new HashMap<>();
    }
    public synchronized void addMissile(Missile missile) throws IllegalCallerException{
        missiles.put(missile.getId(),missile);
    }

    public Missile getMissile(String id){
        return missiles.get(id);
    }

    public int getNumOfLaunchedMissiles(){
        return missiles.size();
    }

    public boolean isHidden() {
        return false;
    }

    public boolean isCurrentlyLaunching() {
        return currentlyLaunching.get();
    }

    public void setCurrentlyLaunching(boolean currentlyLaunching) {
        this.currentlyLaunching.set(currentlyLaunching);
    }

    @Override
    public String toString() {
        return String.format("%s Hidden: %4s Launching: %4s",super.toString(), isHidden(), isCurrentlyLaunching());
    }

	@Override
	public boolean destruct() {
		if(isDestructed())
			throw new IllegalStateException();
			
		isDestructed.set(!isHidden());
		
		return !isHidden();
	}
}
