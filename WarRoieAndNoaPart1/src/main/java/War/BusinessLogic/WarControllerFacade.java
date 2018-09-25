package War.BusinessLogic;

import War.Entities.*;
import War.WarObserver.WarObserver;

import java.util.Set;
import java.util.concurrent.ExecutionException;

public interface WarControllerFacade{

    /**Main Operations */
    void addLauncher(Launcher launcher) /*throws NoSuchElementException*/;

    void addLauncherDestructor(LauncherDestructor launcherDestructor);

    void addMissileDestructor(MissileDestructor missileDestructor);

    void addMissile(Launcher launcher, Destination destination, double potentialDamage) throws InterruptedException, ExecutionException;
    void addMissile(Launcher launcher, Destination destination, double potentialDamage, long maxFlightTime) throws InterruptedException, ExecutionException;
    void addMissile(Launcher launcher, Missile missile);

   void destructMissile(MissileDestructor missileDestructor, Missile missile);
   void destructMissile(MissileDestructor missileDestructor, Missile missile, long time);

   void destructLauncher(LauncherDestructor launcherDestructor, Launcher launcher) throws InterruptedException;
   void destructLauncher(LauncherDestructor launcherDestructor, Launcher launcher, int time) throws InterruptedException;

   Statistics getStatistics();

   void exit();

   /** Support Operations */
   Set<Launcher> retrieveLaunchers();

   Set<MissileDestructor> retrieveMissileDestructors();

   Set<LauncherDestructor> retrieveLauncherDestructors();

   Set<Missile> retrieveActiveMissiles();

   /**Observer - Observable Methods */
   void subscribe(WarObserver subscriber);

   void unsubscribe(WarObserver subscriber);

   long getTime();
 
   void signalAboutMissileResult(Launcher launcher, Missile missile, boolean missileAnswer);
}
