package gson.entities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MissileLaunchers {

    @SerializedName("launcher")
    @Expose
    private List<Launcher> launcher = null;

    public List<Launcher> getLauncher() {
        return launcher;
    }

    public void setLauncher(List<Launcher> launcher) {
        this.launcher = launcher;
    }

}