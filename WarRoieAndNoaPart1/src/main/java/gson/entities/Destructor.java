package gson.entities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Destructor {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("destructedMissile")
    @Expose
    private List<DestructedMissile> destructedMissile = null;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<DestructedMissile> getDestructedMissile() {
        return destructedMissile;
    }

    public void setDestructedMissile(List<DestructedMissile> destructedMissile) {
        this.destructedMissile = destructedMissile;
    }

}
