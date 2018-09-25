package gson.entities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class War {

    @SerializedName("missileLaunchers")
    @Expose
    private MissileLaunchers missileLaunchers;
    @SerializedName("missileDestructors")
    @Expose
    private MissileDestructors missileDestructors;
    @SerializedName("missileLauncherDestructors")
    @Expose
    private MissileLauncherDestructors missileLauncherDestructors;

    public MissileLaunchers getMissileLaunchers() {
        return missileLaunchers;
    }

    public void setMissileLaunchers(MissileLaunchers missileLaunchers) {
        this.missileLaunchers = missileLaunchers;
    }

    public MissileDestructors getMissileDestructors() {
        return missileDestructors;
    }

    public void setMissileDestructors(MissileDestructors missileDestructors) {
        this.missileDestructors = missileDestructors;
    }

    public MissileLauncherDestructors getMissileLauncherDestructors() {
        return missileLauncherDestructors;
    }

    public void setMissileLauncherDestructors(MissileLauncherDestructors missileLauncherDestructors) {
        this.missileLauncherDestructors = missileLauncherDestructors;
    }

}
