package War.WarObserver;

import War.Entities.*;

public interface WarObserver {
    Void launcherWasAdded(Launcher launcher);
    Void launcherDestructorWasAdded(LauncherDestructor launcherDestructor);
    Void missileDestructorWasAdded(MissileDestructor missileDestructor);
    Void missileLaunched(Launcher launcher, Missile missile, Destination destination);
    Void missileHit(Launcher launcher, Missile missile, Destination destination);
    Void missileDestructed(MissileDestructor missileDestructor, Missile missile);
    Void tryToDestructLauncher(LauncherDestructor launcherDestructor, Launcher launcher, int time);
    Void launcherDestructed(LauncherDestructor launcherDestructor, Launcher launcher);
    Void launcherWasHidden(LauncherDestructor launcherDestructor, Launcher launcher);
    Void setServerMessage(String message);
    Void endOfWar();
}
