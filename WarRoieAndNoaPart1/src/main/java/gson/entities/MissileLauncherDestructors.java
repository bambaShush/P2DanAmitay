
package gson.entities;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MissileLauncherDestructors {

    @SerializedName("destructor")
    @Expose
    private List<Destructor_> destructor = null;

    public List<Destructor_> getDestructor() {
        return destructor;
    }

    public void setDestructor(List<Destructor_> destructor) {
        this.destructor = destructor;
    }

}
