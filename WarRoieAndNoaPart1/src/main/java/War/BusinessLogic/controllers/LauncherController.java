package War.BusinessLogic.controllers;

import War.BusinessLogic.WarController;
import War.Entities.Launcher;
import War.Entities.Missile;

import java.util.HashMap;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.PriorityQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class LauncherController extends DestructibleController<Launcher> implements Iterable<MissileController> {
	private HashMap<Missile, MissileController> missileControllers = new HashMap<>();
	private MissileController activeMissileController = null;
	private final ReentrantLock waitForMissilesLock = new ReentrantLock();
	private final Condition notEmpty = waitForMissilesLock.newCondition();

	private PriorityQueue<MissileController> waitingMissiles = new PriorityQueue<>(
			(m1, m2) -> Long.compare(m1.getMissile().getLaunchTime(), m2.getMissile().getLaunchTime()));

	public LauncherController(Launcher destructibleWeapon) {
		super(destructibleWeapon);
	}

	public Launcher getLauncher() {
		return getDestructibleWeapon();
	}

	@Override
	public boolean isDestructed() {
		return getDestructibleWeapon().isDestructed();
	}

	@Override
	public boolean destruct() {

		if (isDestructed())
			throw new IllegalStateException("Launcher " + getLauncher().getId() + " already destructed");

		if (getLauncher().isDestructed())
			return false;

		if (getLauncher().isHidden())
			return false;

		if (getLock().tryLock()) {
			getLauncher().destruct();
			getCondition().signalAll();
			getLock().unlock();
			isAlive.set(false);
			return true;
		}
		return false;
	}
	/*
	 * public synchronized boolean launch(Missile missile, long maxFlightTime)
	 * throws InterruptedException, ExecutionException {
	 * while(isCurrentlyLaunching());
	 * 
	 * //create new missile&missileController getLauncher().addMissile(missile);
	 * MissileController missileController = new MissileController(missile,
	 * maxFlightTime,this); missileControllers.put(missile, missileController);
	 * setActiveMissileController(missileController); setCurrentlyLaunching(true);
	 * 
	 * //start missile flying ExecutorService missileFlyingThread =
	 * Executors.newSingleThreadExecutor(); Future<Boolean> missileHit =
	 * missileFlyingThread.submit(missileController);
	 * 
	 * //waiting for hit/destruct synchronized (this){ wait(); }
	 * 
	 * setCurrentlyLaunching(false); setActiveMissileController(null);
	 * 
	 * return missileHit.get(); }
	 */

	public synchronized void launch(Missile missile, long maxFlightTime)
			throws InterruptedException, ExecutionException {
		// create new missile&missileController
		getLauncher().addMissile(missile);
		MissileController missileController = new MissileController(missile, maxFlightTime, this);
		missileControllers.put(missile, missileController);
		setActiveMissileController(missileController);

		waitingMissiles.add(missileController);
		signalAboutNewMissile();
	}

	private synchronized boolean executeMissile(MissileController missileController)
			throws InterruptedException, ExecutionException {
		try {
			setCurrentlyLaunching(true);

			WarController.getInstance().signalAboutMissileLaunched(getLauncher(), missileController.getMissile());
			// start missile flying
			ExecutorService missileFlyingThread = Executors.newSingleThreadExecutor();
			Future<Boolean> missileHit = missileFlyingThread.submit(missileController);

			// waiting for hit/destruct
			synchronized (this) {
				wait();
			}

			missileFlyingThread.shutdown();

			setCurrentlyLaunching(false);
			setActiveMissileController(null);
			return missileHit.get();

		} catch (IllegalStateException ex) {
			System.err.println(ex.getLocalizedMessage() + " " + ex.getMessage() + " " + ex.getCause());
		}
		throw new IllegalStateException("Missile didn't hit nor distructed");
	}

	public MissileController getActiveMissileController() {
		return activeMissileController;
	}

	private void setActiveMissileController(MissileController activeMissileController) {
		this.activeMissileController = activeMissileController;
	}

	public boolean isCurrentlyLaunching() {
		return getLauncher().isCurrentlyLaunching();
	}

	public void setCurrentlyLaunching(boolean currentlyLaunching) {
		getLauncher().setCurrentlyLaunching(currentlyLaunching);
	}

	@Override
	public Iterator<MissileController> iterator() {
		return missileControllers.values().iterator();
	}

	public MissileController getMissileController(Missile missile) {
		if (!missileControllers.containsKey(missile))
			throw new NoSuchElementException("Missile id was not found in this launcher controller");

		return missileControllers.get(missile);
	}

	private void launchMissiles() throws ExecutionException, InterruptedException {

		MissileController missile;
		if (!waitingMissiles.isEmpty() && !isDestructed()) {
			missile = waitingMissiles.poll();
			//waitForMissileLaunchTime(missile);
			if (!isDestructed()) {
				try {
				boolean missileAnswer = executeMissile(missile);
			
				if (!missile.isDestructed()) {
					WarController.getInstance().signalAboutMissileResult(getLauncher(), missile.getMissile(),
							missileAnswer);
				}
				
				}catch(IllegalStateException ex) {
					System.err.println(missile.getMissile().getId() + ": " + ex.getMessage());
				}
			}
		} else {
			waitForNewMissiles();
		}

	}

	private void signalAboutNewMissile() {
		try {
			waitForMissilesLock.lock();
			notEmpty.signalAll();
		} finally {
			waitForMissilesLock.unlock();
		}
	}

	private void waitForNewMissiles() throws InterruptedException {
		try {
			waitForMissilesLock.lock();
			notEmpty.await();
		} finally {
			waitForMissilesLock.unlock();
		}

	}
/*
	private void waitForMissileLaunchTime(MissileController missileController) {
		long timeToSleep = missileController.getMissile().getLaunchTime() - WarController.getInstance().getTime();
		try {
			Thread.sleep(timeToSleep * 1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}*/

	@Override
	public Boolean call() throws Exception {
		isAlive.set(true);
		getLock().lock();
		ExecutorService es = Executors.newSingleThreadExecutor();

		try {
			es.execute(() -> {
				while (true) {
					try {
						launchMissiles();
					} catch (ExecutionException | InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

			});

			getCondition().await();
		} finally {
			getLock().unlock();
			es.shutdown();
		}
		
		isAlive.set(false);
		
		return true;
	}

}