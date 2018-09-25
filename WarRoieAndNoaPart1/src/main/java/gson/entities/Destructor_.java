
package gson.entities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Destructor_ {

    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("destructedLauncher")
    @Expose
    private List<DestructedLauncher> destructedLauncher = null;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<DestructedLauncher> getDestructedLauncher() {
        return destructedLauncher;
    }

    public void setDestructedLauncher(List<DestructedLauncher> destructedLauncher) {
        this.destructedLauncher = destructedLauncher;
    }

}
