package War.View;


import War.BusinessLogic.WarController;
import War.BusinessLogic.WarControllerFacade;
import War.Entities.*;
import War.View.EntitiesViews.*;
import War.View.subscribers.FXViewSubscriber;
import War.WarObserver.WarObserver;
import javafx.animation.KeyFrame;
import javafx.animation.PathTransition;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Bounds;
import javafx.geometry.NodeOrientation;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.*;
import javafx.util.Duration;

import java.net.URL;
import java.util.Optional;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;

public class MainWindowController extends BorderPane implements ViewFacade, Initializable{
    private WarControllerFacade controller = WarController.getInstance();

    @FXML
    private BorderPane container;
    @FXML
    private VBox launchersBox;
    @FXML
    private VBox missileDestructorsBox;
    @FXML
    private HBox planesBox;
    @FXML
    private HBox shipsBox;
    @FXML
    private VBox targetsBox;

    private ToggleGroup targetsGroup = new ToggleGroup();


    private ObservableList<Node> missilesOnAir = FXCollections.observableArrayList();

    private WarObserver warViewSubscriber;

    private int targetNumber = 0;

    public WarControllerFacade getController() {
        return controller;
    }

    @FXML
    @Override
    public void addLauncher() {
        System.out.println("Add Launcher Clicked");
        Launcher launcher = new Random().nextBoolean() ? new Launcher() : new HideableLauncher();
        controller.addLauncher(launcher);
    }

    @FXML
    public void addTarget(){
        Destination destination = new Destination("Target #" + targetNumber++);
        TargetView targetView = new TargetView(destination);
        targetsBox.getChildren().add(targetView);
        targetView.setToggleGroup(targetsGroup);
    }

    @FXML
    @Override
    public void addLauncherDestructor() {

    }

    @FXML
    public void addPlaneLauncherDestructor() {
        LauncherDestructor launcherDestructor = new LauncherDestructor(LauncherDestructor.DestructorType.PLANE);
        controller.addLauncherDestructor(launcherDestructor);
    }

    @FXML
    public void addShipLauncherDestructor() {
        LauncherDestructor launcherDestructor = new LauncherDestructor(LauncherDestructor.DestructorType.SHIP);
        controller.addLauncherDestructor(launcherDestructor);
    }

    @FXML
    @Override
    public void addMissileDestructor() {
        MissileDestructor missileDestructor = new MissileDestructor();
        controller.addMissileDestructor(missileDestructor);
    }

    @FXML
    @Override
    public void launchMissile() throws ExecutionException, InterruptedException {
        double damage;
        LauncherView launcherView = (LauncherView) getSelectedView(launchersBox.getChildren());
        TargetView targetView = (TargetView) getSelectedView(targetsBox.getChildren());

        TextInputDialog textInputDialog = new TextInputDialog();
        textInputDialog.setHeaderText("Determine Max Damage");
        textInputDialog.setContentText("Max damage:");
        textInputDialog.getEditor().textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("(\\d+(.?\\d*)?)")) {
                textInputDialog.getEditor().setText(newValue.replaceAll("[^\\d]", ""));
            }});

        if(launcherView != null && targetView != null) {
            Optional<String> answer = textInputDialog.showAndWait();
            if(answer.isPresent()){
                damage = Double.parseDouble(answer.get());
            }else {
                return;
            }
           Executors.newSingleThreadExecutor().submit(() -> {
               try {
                   controller.addMissile(launcherView.getWeapon(), targetView.getWeapon(), damage);
               } catch (InterruptedException | ExecutionException e) {
                   e.printStackTrace();
               }
           });
        }
    }


    private WeaponView<?> getSelectedView(ObservableList<Node> weaponViews){
        FilteredList<Node> filtered = weaponViews.filtered(wv -> ((WeaponView<?>)wv).isSelected());
        if(filtered.isEmpty())
            return null;
        else
            return (WeaponView<?>) filtered.get(0);
    }

    public PathTransition launchingAnimation(WeaponView<?> launcherView, WeaponView<?> targetView, MissileView missileView, long flightTime){
        container.getChildren().add(missileView);
        missilesOnAir.add(missileView);

        Bounds launcherBounds = launcherView.localToScene(launcherView.getBoundsInLocal());

        missileView.setX(launcherBounds.getMaxX());
        missileView.setY(launcherBounds.getMinY());
        PathTransition pathTransition = new PathTransition();
        CubicCurveTo cubicCurveTo = cubicCurveToTarget(launcherView, targetView);
        Path path = new Path();
        path.getElements().addAll(new MoveTo(launcherBounds.getMaxX(),launcherBounds.getMinY() + 20), cubicCurveTo);
        pathTransition.setPath(path);
        pathTransition.setDuration(Duration.seconds(flightTime));
        pathTransition.setOrientation(PathTransition.OrientationType.ORTHOGONAL_TO_TANGENT);
        pathTransition.setNode(missileView);
        launcherView.setOpacity(1);
        missileView.setPathTransition(pathTransition);
        pathTransition.play();


        return pathTransition;
    }

    public void launchHitAnimation(WeaponView<?> launcherView, WeaponView<?> targetView, ObservableList<Node> targetsBox, MissileView missileView ){
        launcherView.setSelected(false);
        targetView.setSelected(false);
        missileView.setSelectable(false);
        targetView.setVisible(false);
        missilesOnAir.remove(missileView);
        missileView.setVisible(false);

        if(targetView instanceof TargetView)
            new Timeline( new KeyFrame(Duration.millis(700), e -> targetsBox.remove(targetView))).play();

        if(launcherView.getWeapon() instanceof HideableLauncher){
            launcherView.setOpacity(0.5);
        }
    }
    private CubicCurveTo cubicCurveToTarget(WeaponView<?> launcherView, WeaponView<?> targetView){
        final double PARABOLA_HEIGHT = 300;
        Bounds launcherBounds = launcherView.localToScene(launcherView.getBoundsInLocal());
        Bounds targetBounds = targetView.localToScene(targetView.getBoundsInLocal());
        Point2D targetCenter = center(targetBounds);
        CubicCurveTo cubicCurveTo = new CubicCurveTo();
        cubicCurveTo.setX(targetCenter.getX());
        cubicCurveTo.setY(targetCenter.getY());
        double avgX = average(launcherBounds.getMaxX(), targetBounds.getMaxX()), avgY = average(launcherBounds.getMaxY(), targetBounds.getMaxY());
        cubicCurveTo.setControlX1(avgX);
        cubicCurveTo.setControlY1(avgY - PARABOLA_HEIGHT);
        cubicCurveTo.setControlX2(avgX);
        cubicCurveTo.setControlY2(avgY - PARABOLA_HEIGHT);
        return cubicCurveTo;
    }

    @FXML
    @Override
    public void destructMissile() {
        MissileDestructorView missileDestructorView = null;
        MissileView targetView = null;

        missileDestructorView = (MissileDestructorView) getSelectedView(missileDestructorsBox.getChildren());
        targetView = (MissileView) getSelectedView(missilesOnAir);

        if(missileDestructorView != null && targetView != null && targetView.getWeapon() != null) {
            MissileDestructor missileDestructor = missileDestructorView.getWeapon();
            Missile missile = targetView.getWeapon();
            Executors.newSingleThreadExecutor().submit(() -> controller.destructMissile(missileDestructor, missile));
        }else{
            System.out.println("missile view was not selected");
        }
    }

    @FXML
    @Override
    public void destructLauncher() {
        LauncherDestructorView launcherDestructorView = null;
        LauncherView targetView = null;

        launcherDestructorView = (LauncherDestructorView) getSelectedView(planesBox.getChildren());

        if(launcherDestructorView == null){
            launcherDestructorView = (LauncherDestructorView) getSelectedView(shipsBox.getChildren());
        }

        targetView = (LauncherView) getSelectedView(launchersBox.getChildren());

        if(launcherDestructorView != null && targetView != null) {
            LauncherDestructor launcherDestructor = launcherDestructorView.getWeapon();
            Launcher launcher = targetView.getWeapon();
            Executors.newSingleThreadExecutor().submit(() ->
                    {
						try {
							controller.destructLauncher(launcherDestructor, launcher);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					});

        }else{
            System.out.println("missile view was not selected");
        }
    }

    public void destructingLauncherAnimation(LauncherDestructorView destructorView, LauncherView launcherView
            , int flightTime){
        final int FIX = 10;
        Bounds destructorBounds = destructorView.localToScene(destructorView.getBoundsInLocal());
        Bounds launcherBounds = launcherView.localToScene(launcherView.getBoundsInLocal());
        Point2D initDestructorCoor = center(destructorBounds).subtract(destructorBounds.getWidth() + FIX,destructorBounds.getHeight() / 2 + FIX);
        Point2D finalDestructorCoor = new Point2D(launcherBounds.getMinX(), initDestructorCoor.getY());

        if(destructorView.getWeapon().getType().equals(LauncherDestructor.DestructorType.SHIP)){
            initDestructorCoor = initDestructorCoor.subtract(300, 725);
            finalDestructorCoor = finalDestructorCoor.subtract(0, 725);
        }else{
            finalDestructorCoor = finalDestructorCoor.subtract(destructorBounds.getWidth() / 2, 20).add(20,0);
        }

       Line destructorPath =  new Line(finalDestructorCoor.getX(), finalDestructorCoor.getY() ,
                initDestructorCoor.getX(), initDestructorCoor.getY());

        destructorView.setGoingPath(destructorPath);
        PathTransition pathTransition = new PathTransition(Duration.seconds(flightTime), destructorPath);
        pathTransition.setAutoReverse(false);
        pathTransition.setCycleCount(1);
        pathTransition.setNode(destructorView);

        pathTransition.play();

        System.out.println("Play: launcher destructor");
    }

    public void launcherDestructorDroppingABomb(LauncherDestructorView destructorView, LauncherView launcherView,
                                                 MissileView missileView,int flightTime){
        Bounds launcherBounds = launcherView.localToScene(launcherView.getBoundsInLocal());
        Bounds launchingDestructorBounds = destructorView.localToScene(destructorView.getBoundsInLocal());
        Point2D centerLaunchingDest = center(launchingDestructorBounds);
        Line missilePath = new Line(centerLaunchingDest.getX(),  launchingDestructorBounds.getMaxY(),
                launcherBounds.getMinX() + launcherBounds.getWidth() / 2, launcherBounds.getMinY() + launcherBounds.getHeight() / 2);
        container.getChildren().add(missileView);
        missileView.setX(missilePath.getStartX());
        missileView.setY(missilePath.getStartY());
        PathTransition missileTransition = new PathTransition(Duration.seconds(flightTime), missilePath);
        missileTransition.setNode(missileView);
        missileTransition.play();
        missileTransition.setOnFinished(event -> {

            missileView.setVisible(false);
            launcherView.setVisible(false);
            PathTransition goingBack = goingBack(destructorView, flightTime);
            goingBack.setOnFinished( event1 -> launchersBox.getChildren().remove(launcherView));
        });
    }

    public PathTransition goingBack(LauncherDestructorView destructorView, int time){
        Line backPath = new Line();
        backPath.setStartX(destructorView.getGoingPath().getEndX());
        backPath.setStartY(destructorView.getGoingPath().getEndY());
        backPath.setEndX(destructorView.getGoingPath().getStartX()- 100);
        backPath.setEndY(destructorView.getGoingPath().getStartY());
        PathTransition goingBack = new PathTransition(Duration.seconds(time),backPath , destructorView);
        goingBack.play();

        return goingBack;
    }

    private Point2D center(Bounds bounds) {
        return new Point2D(average(bounds.getMaxX(), bounds.getMinX()), average(bounds.getMaxY(), bounds.getMinY()));
    }
    private double average(double d1, double d2){
        return (d1 + d2) / 2;
    }

    @FXML
    @Override
    public void showStatistics() {

    }

    @FXML
    @Override
    public void exit() {

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        planesBox.setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
        shipsBox.setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);

         warViewSubscriber = new FXViewSubscriber(this);
         controller.subscribe(warViewSubscriber);


    }

    public VBox getLaunchersBox() {
        return launchersBox;
    }

    public VBox getMissileDestructorsBox() {
        return missileDestructorsBox;
    }

    public HBox getPlanesBox() {
        return planesBox;
    }

    public HBox getShipsBox() {
        return shipsBox;
    }

    public VBox getTargetsBox() {
        return targetsBox;
    }

    public ObservableList<Node> getMissilesOnAir() {
        return missilesOnAir;
    }

    public BorderPane getContainer() {
        return container;
    }
}
