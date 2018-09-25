package War.DAL;

import War.Entities.Launcher;
import War.Entities.LauncherDestructor;
import War.Entities.Missile;
import War.Entities.MissileDestructor;

public interface WarDao {
    void saveLauncher(Launcher launcher);

    void saveLauncherDestructor(LauncherDestructor launcherDestructor);

    void saveMissileDestructor(MissileDestructor missileDestructor);

    void saveMissile(Missile missile);
}
