package War.BusinessLogic.controllers;

import War.Entities.Destructible;

import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

abstract class DestructibleController<T extends Destructible> implements Callable<Boolean> {
    private Lock lock;
    private Condition condition;
    private T destructibleWeapon;
    protected AtomicBoolean isAlive = new AtomicBoolean(false);


    public DestructibleController(T destructibleWeapon){
        setDestructibleWeapon(destructibleWeapon);
        setLock(new ReentrantLock());
        setCondition(getLock().newCondition());
    }

    public abstract boolean isDestructed();

    public abstract boolean destruct();


    public Lock getLock() {
        return lock;
    }

    protected void setLock(Lock lock) {
        this.lock = lock;
    }

    public Condition getCondition() {
        return condition;
    }

    protected T getDestructibleWeapon() {
        return destructibleWeapon;
    }

    protected void setDestructibleWeapon(T destructibleWeapon) {
        this.destructibleWeapon = destructibleWeapon;
    }

    protected void setCondition(Condition condition) {
        this.condition = condition;
    }

	public boolean isAlive() {
		return isAlive.get();
	}

	public void setIsAlive(boolean isAlive) {
		this.isAlive.set(isAlive);
	}
    
    
}
