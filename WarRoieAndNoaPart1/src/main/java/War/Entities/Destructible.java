package War.Entities;

import java.util.concurrent.atomic.AtomicBoolean;

public abstract class Destructible extends Weapon {
    protected AtomicBoolean isDestructed = new AtomicBoolean(false);
    
    public Destructible(String id) {
        super(id);
    }

    public boolean isDestructed() {
        return isDestructed.get();
    }

    public abstract boolean destruct() throws IllegalStateException;

    
    @Override
    public String toString() {
        return String.format("%s Destructed: %4s",super.toString(), isDestructed());
    }
}
