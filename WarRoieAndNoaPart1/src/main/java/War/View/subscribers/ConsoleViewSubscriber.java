package War.View.subscribers;

import War.BusinessLogic.WarControllerFacade;
import War.Entities.*;
import War.View.ConsoleView;
import War.WarObserver.WarObserver;

public class ConsoleViewSubscriber implements WarObserver{
    private ConsoleView consoleView;

    public ConsoleViewSubscriber(WarControllerFacade warControllerFacade, ConsoleView consoleView){
        warControllerFacade.subscribe(this);
        this.consoleView = consoleView;
    }

    @Override
    public Void launcherWasAdded(Launcher launcher) {
        System.out.println("\nAdded launcher:\n"+launcher +"\n\n");
        return null;
    }

    @Override
    public Void launcherDestructorWasAdded(LauncherDestructor launcherDestructor) {
        System.out.println("\nAdded launcher destructor:\n" + launcherDestructor);
        return null;
    }

    @Override
    public Void missileDestructorWasAdded(MissileDestructor missileDestructor) {
        System.out.println("\nAdded missile destructor :\n" + missileDestructor);
        return null;
    }

    @Override
    public Void missileLaunched(Launcher launcher, Missile missile, Destination destination) {
        System.out.println(launcher +"\n"+"is launching:\n" + missile +"\nto: " +destination +"\n");
        return null;
    }

    @Override
    public Void missileHit(Launcher launcher, Missile missile, Destination destination) {
        System.out.println(missile + "\nHit at: " + destination +"\n");
        return null;
    }

    @Override
    public Void missileDestructed(MissileDestructor missileDestructor, Missile missile) {
        System.out.println(missile +"\nWas destructed successfully!\n");
        return null;
    }

    @Override
    public Void tryToDestructLauncher(LauncherDestructor launcherDestructor, Launcher launcher, int time) {
        System.out.println(launcherDestructor +"\n"+"is trying to destruct:\n" + launcher +"\n");
        return null;
    }

    @Override
    public Void launcherDestructed(LauncherDestructor launcherDestructor, Launcher launcher) {
        System.out.println(launcher +"\nWas destructed successfully by\n" + launcherDestructor);
        return null;
    }

    @Override
    public Void launcherWasHidden(LauncherDestructor launcherDestructor, Launcher launcher) {
        System.out.println(launcher +"\nWas hidden and wasn't destructed\n");
        return null;
    }

    @Override
    public Void endOfWar() {
        consoleView.showStatistics();
        return null;
    }
}
