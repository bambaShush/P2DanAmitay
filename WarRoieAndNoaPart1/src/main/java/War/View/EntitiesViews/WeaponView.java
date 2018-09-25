package War.View.EntitiesViews;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.ImageView;

public class WeaponView<W> extends ImageView implements Toggle{
    private W weapon = null;
    private BooleanProperty isSelected = new SimpleBooleanProperty(this, "isSelected",false);
    private boolean selectable = false;
    private ObjectProperty<ToggleGroup> toggleGroup = new SimpleObjectProperty<>();

    protected WeaponView(W weapon, String imageURL){
        super(imageURL);
        setWeapon(weapon);
        initialize();
    }

    protected WeaponView(String imageURL){
        super(imageURL);
        initialize();
    }

    private void initialize(){
        getStyleClass().add("weapon");
        setSelectable(true);

        isSelected.addListener((v, oldVal, newVal) -> {
            if(newVal && toggleGroup.get() != null)
                toggleGroup.get().selectToggle(this);
        });
    }

    public W getWeapon() {
        return weapon;
    }

    private void setWeapon(W weapon) {
        this.weapon = weapon;
    }

    @Override
    public ToggleGroup getToggleGroup() {
        return toggleGroup.get();
    }

    @Override
    public ObjectProperty<ToggleGroup> toggleGroupProperty() {
        return toggleGroup;
    }

    public void setToggleGroup(ToggleGroup toggleGroup) {
        this.toggleGroup.set(toggleGroup);
    }

    @Override
    public boolean isSelected() {
        return isSelected.get();
    }

    @Override
    public void setSelected(boolean selected) {
        isSelected.set(selected);
        if(!selected)
            setStyle("-fx-effect: dropshadow(three-pass-box, rgb(6,6,6), 10, 0, 0, 0);\n");
    }

    @Override
    public BooleanProperty selectedProperty() {
        return isSelected;
    }

    public boolean isSelectable() {
        return selectable;
    }

    public void setSelectable(boolean selectable) {
        this.selectable = selectable;
        if(selectable){
            activateSelection();
        }else{
            disableSelection();
            setStyle("-fx-effect: dropshadow(three-pass-box, rgb(251,253,255), 10, 0, 0, 0);\n");
            isSelected.set(false);
        }
    }

    private void activateSelection(){
        setOnMouseEntered(e -> {
            if(!isSelected.get())
                setStyle("-fx-effect: dropshadow(three-pass-box, rgb(251,253,255), 10, 0, 0, 0);\n");
        });


        setOnMouseClicked(e -> {
            if (!isSelected.get()) {
                setStyle("-fx-effect: dropshadow(three-pass-box, rgb(0,255,144), 15, 0, 0, 0);\n");
                isSelected.set(true);
           /*     synchronized (session){
                    session.notify();
                }*/
            } else {
                setStyle("-fx-effect: dropshadow(three-pass-box, rgb(251,253,255), 10, 0, 0, 0);\n");
                isSelected.set(false);
            }
        });
        setOnMouseExited(e -> {
            if(!isSelected.get())
                setStyle("-fx-effect: dropshadow(three-pass-box, rgb(6,6,6), 10, 0, 0, 0);\n");
        });
    }

    private void disableSelection(){
        setOnMouseEntered(e -> { });
        setOnMouseClicked(e -> { });
        setOnMouseExited(e -> { });
    }



}
