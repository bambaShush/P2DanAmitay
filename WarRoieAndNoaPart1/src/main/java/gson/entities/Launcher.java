package gson.entities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Launcher {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("isHidden")
    @Expose
    private boolean isHidden;
    @SerializedName("missile")
    @Expose
    private List<Missile> missile = null;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isHidden() {
        return isHidden;
    }

    public void setIsHidden(boolean isHidden) {
        this.isHidden = isHidden;
    }

    public List<Missile> getMissile() {
        return missile;
    }

    public void setMissile(List<Missile> missile) {
        this.missile = missile;
    }

}
