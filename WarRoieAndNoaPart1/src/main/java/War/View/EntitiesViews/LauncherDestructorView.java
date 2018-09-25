package War.View.EntitiesViews;

import War.Entities.LauncherDestructor;

import javafx.scene.image.Image;
import javafx.scene.shape.Line;

public class LauncherDestructorView extends WeaponView<LauncherDestructor> {
    private final static String PLANE_URL = "War/View/images/helicopter icecream 140.png";
    private final static String SHIP_URL = "War/View/images/ship.png";
    private Line goingPath;
    public LauncherDestructorView(LauncherDestructor weapon) {
        super(weapon, PLANE_URL);

        if(weapon.getType() != LauncherDestructor.DestructorType.PLANE)
            setImage(new Image(SHIP_URL));
    }

    public void setGoingPath(Line goingPath) {
        this.goingPath = goingPath;
    }

    public Line getGoingPath() {
        return goingPath;
    }
}
