package War.View.EntitiesViews;

import War.Entities.Missile;
import javafx.animation.PathTransition;

public class MissileView extends WeaponView<Missile>{
    private PathTransition pathTransition;

    public MissileView(String imageURL) {
        super(imageURL);
    }

    public MissileView(Missile missile, String imageURL) {
        super(missile, imageURL);
    }

    public void setPathTransition(PathTransition pathTransition) {
        this.pathTransition = pathTransition;
    }

    public PathTransition getPathTransition() {
        return pathTransition;
    }
}
