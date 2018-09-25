package War.BusinessLogic;

import War.BusinessLogic.controllers.LauncherController;
import War.BusinessLogic.controllers.LauncherDestructorController;
import War.BusinessLogic.controllers.MissileController;
import War.BusinessLogic.controllers.MissileDestructorController;
import War.Entities.*;
import War.WarObserver.WarObservable;
import War.WarObserver.WarObserver;

import java.time.LocalTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.*;

import static java.time.temporal.ChronoUnit.SECONDS;


public class WarController extends WarObservable implements WarControllerFacade{ //Singleton
    private static WarController warController = new WarController();

    private static LocalTime startRunTime = LocalTime.now();
    private HashMap<Launcher, LauncherController> missileLaunchers;
    private HashMap<MissileDestructor, MissileDestructorController> missileDestructors;
    private HashMap<LauncherDestructor, LauncherDestructorController> launcherDestructors;

    private Statistics statistics = new Statistics();


    private ExecutorService executorService = Executors.newCachedThreadPool();

    private WarController(){
        missileLaunchers = new HashMap<>();
        missileDestructors = new HashMap<>();
        launcherDestructors = new HashMap<>();
    }

    public static void startTime(){
        startRunTime = LocalTime.now();
    }

    public static WarController getInstance(){
        return warController;
    }

    @Override
    public void addLauncher(Launcher launcher){
        LauncherController launcherController = new LauncherController(launcher);
        executorService.submit(launcherController);

        missileLaunchers.put(launcher, launcherController);

        publish(subscriber -> subscriber.launcherWasAdded(launcher));
    }

    @Override
    public void addLauncherDestructor(LauncherDestructor launcherDestructor) {
        launcherDestructors.put(launcherDestructor, new LauncherDestructorController(launcherDestructor));
        publish(subscriber -> subscriber.launcherDestructorWasAdded(launcherDestructor));
    }

    @Override
    public void addMissileDestructor(MissileDestructor missileDestructor) {
        missileDestructors.put(missileDestructor, new MissileDestructorController(missileDestructor));
        publish(subscriber -> subscriber.missileDestructorWasAdded(missileDestructor));
    }

    @Override
    public void addMissile(Launcher launcher, Destination destination, double potentialDamage) {
        final int MAX_TIME = 11;

        int maxFlightTime = new Random().nextInt(MAX_TIME) + 4;
        addMissile(launcher, destination, potentialDamage, maxFlightTime);
    }

    @Override
    public void addMissile(Launcher launcher, Destination destination, double potentialDamage, long maxFlightTime) {
        Missile missile = new Missile(potentialDamage, destination, maxFlightTime, getTime());

        addMissile(launcher, missile);
    }

    @Override
    public void addMissile(Launcher launcher, Missile missile){
        LauncherController launcherController = missileLaunchers.getOrDefault(launcher, null);

        if(launcherController == null){
        	throw new NoSuchElementException("No such missile");
        }

        try {
            executorService.submit(() -> addMissileToLauncher(launcherController, missile, missile.getFlightTime()));

        }catch (Exception e){
            e.printStackTrace();
        }

    }
    
    private void addMissileToLauncher(LauncherController launcherController, Missile missile, long maxFlightTime){
        try {
            if(!launcherController.isDestructed())
                launcherController.launch(missile, maxFlightTime);
            
        } catch (InterruptedException |ExecutionException e1) {
            e1.printStackTrace();
        }
    }
    
    public void signalAboutMissileLaunched(Launcher launcher, Missile missile) {

        publish(subscriber -> subscriber.missileLaunched(launcher, missile, missile.getDestination()));

        statistics.increaseNumOfLaunchedMissiles();
    }

    public void signalAboutMissileResult(Launcher launcher, Missile missile, boolean hit) {
    	if(hit) {
            statistics.increaseNumOfHits();
            statistics.raiseDamage(missile.getDamage());
            publish(subscriber -> subscriber.missileHit(launcher, missile, missile.getDestination()));
        }else {
        	
        }
    }


    @Override
    public void destructMissile(MissileDestructor missileDestructor, Missile missile) {
        final int MAX = 5, OFFSET = 1;
        destructMissile(missileDestructor, missile, new Random().nextInt(MAX) + OFFSET);
    }

    @Override
    public void destructMissile(MissileDestructor missileDestructor, Missile missile, long time) {
    	
        MissileDestructorController missileDestructorController = missileDestructors.get(missileDestructor);
        LauncherController launcherControllers[] = missileLaunchers.values().toArray(new LauncherController[0]);
        for(LauncherController launcherController: launcherControllers){
            if(launcherController.isCurrentlyLaunching()){
                MissileController missileController = launcherController.getActiveMissileController();

                if(missileController.getMissile().equals(missile)){

                    final long timeFromLaunch =  time - 1;// - missileController.getMissile().getLaunchTime();

            		missileDestructorController.tryToDestruct(missileController, timeFromLaunch); 

                }
            }
        }
    }
    
	
	public void signalAboutMissileDestructionAttempt(MissileDestructor missileDestructor, Missile missile, boolean destruct) {
		if (destruct) {
			statistics.increaseNumOfDestructedMissiles();
			publish(subscriber -> subscriber.missileDestructed(missileDestructor, missile));

		}

	}

    @Override
    public void destructLauncher(LauncherDestructor launcherDestructor, Launcher launcher) throws InterruptedException {
        final int MAX_TIME = 5, TIME_OFFSET = 1;
        int destructionTime = new Random().nextInt(MAX_TIME) + TIME_OFFSET;
        destructLauncher(launcherDestructor, launcher, destructionTime);
    }

    @Override
    public void destructLauncher(LauncherDestructor launcherDestructor, Launcher launcher, int destructionTime) throws InterruptedException {
        LauncherController launcherController = missileLaunchers.getOrDefault(launcher, null);

        if(launcherController == null)
            throw new IllegalArgumentException("launcher doesn't not exist or already destructed");
        
        LauncherDestructorController launcherDestructorController =
                launcherDestructors.get(launcherDestructor);

        publish(subscriber -> subscriber.tryToDestructLauncher(launcherDestructor, launcher, destructionTime));
        
        launcherDestructorController.tryToDestruct(launcherController, destructionTime);
    }
    
    public void signalAboutLauncherDestructionAttempt(LauncherDestructor launcherDestructor, Launcher launcher, boolean succeed) {
    	if(succeed){
            publish(subscriber -> subscriber.launcherDestructed(launcherDestructor, launcher));
            missileLaunchers.remove(launcher);
            statistics.increaseNumOfDestructedLaunchers();
        }else{
            publish(subscriber -> subscriber.launcherWasHidden(launcherDestructor, launcher));
        }
    }

    @Override
    public Statistics getStatistics() {
        return statistics;
    }

    @Override
    public void exit() {
        executorService.shutdown();
        publish(WarObserver::endOfWar);
        System.exit(0);
    }

    @Override
    public Set<Launcher> retrieveLaunchers() {
        return missileLaunchers.keySet();
    }

    @Override
    public Set<MissileDestructor> retrieveMissileDestructors() {
        return missileDestructors.keySet();
    }

    @Override
    public Set<LauncherDestructor> retrieveLauncherDestructors() {
        return launcherDestructors.keySet();
    }

    @Override
    public Set<Missile> retrieveActiveMissiles() {
        Set<Missile> activeMissiles = new HashSet<>();
        for(LauncherController launcherController : missileLaunchers.values()){
            if(launcherController.isCurrentlyLaunching()) {
                activeMissiles.add(launcherController.getActiveMissileController().getMissile());
            }
        }

        return activeMissiles;
    }

    public long getTime(){
        return SECONDS.between(startRunTime,LocalTime.now());
    }

   
}
