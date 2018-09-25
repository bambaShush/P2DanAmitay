package gson.entities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DestructedLauncher {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("destructTime")
    @Expose
    private int destructTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getDestructTime() {
        return destructTime;
    }

    public void setDestructTime(int destructTime) {
        this.destructTime = destructTime;
    }

}
