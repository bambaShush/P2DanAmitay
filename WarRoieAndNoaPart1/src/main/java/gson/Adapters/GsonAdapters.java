package gson.Adapters;

import War.Entities.*;

public class GsonAdapters {
    public War.Entities.Missile adaptMissile(gson.entities.Missile gMissile){
        return new Missile(gMissile.getId(),
                            gMissile.getDamage(),
                            new Destination(gMissile.getDestination()),
                            gMissile.getFlyTime(),
                            gMissile.getLaunchTime());
    }

    public War.Entities.Launcher adaptLauncher(gson.entities.Launcher gLauncher){
        if(gLauncher.isHidden()){
            return new War.Entities.HideableLauncher(gLauncher.getId());
        }
        else{
            return new War.Entities.Launcher(gLauncher.getId());
        }
    }

    public War.Entities.MissileDestructor adaptMissileDestructor(gson.entities.Destructor gMissileDestructor){
        return new War.Entities.MissileDestructor(gMissileDestructor.getId());
    }

    public War.Entities.LauncherDestructor adaptLauncherDestructor(gson.entities.Destructor_ glauncherDestructor){
        War.Entities.LauncherDestructor.DestructorType type;
        switch (glauncherDestructor.getType()) {
            case "plane":
                type = LauncherDestructor.DestructorType.PLANE;
                break;
            case "shipA":
                type = LauncherDestructor.DestructorType.SHIP;
                break;
            default:
                type = null;
                break;
        }
        return new War.Entities.LauncherDestructor(type);
    }
}
