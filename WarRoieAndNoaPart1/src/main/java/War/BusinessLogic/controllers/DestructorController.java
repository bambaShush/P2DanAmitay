package War.BusinessLogic.controllers;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import War.Entities.Destructible;
import War.Entities.Destructor;

public abstract class DestructorController<E extends Destructible, D extends Destructor<E>, T extends DestructibleController<E>> {

	private D destructor;
	private Queue<T> weaponsToDestruct;
	private Queue<Long> destructTime;

	private Lock waitForTargetsLock;
	private Condition noTargets;

	public DestructorController(D destructor) {
		setDestructor(destructor);
		weaponsToDestruct = new ConcurrentLinkedQueue<>();
		destructTime = new ConcurrentLinkedQueue<>();
		waitForTargetsLock = new ReentrantLock();
		noTargets = waitForTargetsLock.newCondition();

		Executors.newSingleThreadExecutor().execute(() -> {
			try {
				destructWeapons();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		});
	}

	public D getDestructor() {
		return destructor;
	}

	public void setDestructor(D destructor) {
		this.destructor = destructor;
	}

	public void tryToDestruct(T destructibleController, long time) {
		weaponsToDestruct.add(destructibleController);
		destructTime.add(time);
		signalAboutNewTargetAdded();
	}

	private void destructWeapons() throws InterruptedException {
		while (true) {
			if (!weaponsToDestruct.isEmpty()) {
				T destructibleController = weaponsToDestruct.poll();
				long sleepTime = destructTime.poll(); // - WarController.getInstance().getTime();
				System.err.println(destructor.getId() + " sleeps: " + sleepTime);
				Thread.sleep(sleepTime * 1000);

				if (!destructibleController.isDestructed() && destructibleController.isAlive()) {
					try {
						boolean success = destructor.destruct(destructibleController.getDestructibleWeapon());

						if (success) {
							signalAboutSuccess(destructor, destructibleController.getDestructibleWeapon());
						} else {
							signalAboutFailure(destructor, destructibleController.getDestructibleWeapon());
						}
					} catch (IllegalStateException ex) {
						System.out.println(ex.getMessage() + " " + ex.getCause());
					}
				}
			} else {
				waitForNewTargets();
			}
		}
	}

	private void waitForNewTargets() {
		try {
			waitForTargetsLock.lock();
			noTargets.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			waitForTargetsLock.unlock();
		}
	}

	private void signalAboutNewTargetAdded() {
		try {
			waitForTargetsLock.lock();
			noTargets.signalAll();
		} finally {
			waitForTargetsLock.unlock();
		}
	}

	protected abstract void signalAboutSuccess(D destructor, E destructible);

	protected abstract void signalAboutFailure(D destructor, E destructible);

}
