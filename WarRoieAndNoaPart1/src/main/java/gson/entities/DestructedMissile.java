package gson.entities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DestructedMissile {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("destructAfterLaunch")
    @Expose
    private int destructAfterLaunch;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getDestructAfterLaunch() {
        return destructAfterLaunch;
    }

    public void setDestructAfterLaunch(int destructAfterLaunch) {
        this.destructAfterLaunch = destructAfterLaunch;
    }

}
