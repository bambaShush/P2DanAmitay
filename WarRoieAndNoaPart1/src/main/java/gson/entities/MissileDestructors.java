
package gson.entities;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MissileDestructors {

    @SerializedName("destructor")
    @Expose
    private List<Destructor> destructor = null;

    public List<Destructor> getDestructor() {
        return destructor;
    }

    public void setDestructor(List<Destructor> destructor) {
        this.destructor = destructor;
    }

}
