package War.View.application;

import War.BusinessLogic.WarController;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Optional;

public class WarApplication extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/War/View/WarMainWindow.fxml"));
        Scene mainScene = new Scene(loader.load());
        Platform.setImplicitExit(false);
        primaryStage.setScene(mainScene);
        primaryStage.setFullScreen(true);
      //  primaryStage.setResizable(false);

        primaryStage.setOnCloseRequest(v -> {
            primaryStage.close();
            FXMLLoader statisticsLoader = new FXMLLoader(getClass().getResource("/War/View/WarStatisticsWindow.fxml"));
            try {
                Scene statScene = new Scene(statisticsLoader.load());
                Stage statStage = new Stage();
                statStage.setScene(statScene);
                statStage.setResizable(false);
                statStage.showAndWait();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        
        askIfToLoadFromJSON();

         primaryStage.show();

    }

    public static void askIfToLoadFromJSON(){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Loading from JSON confirmation");
        alert.setHeaderText("Would you like to load war from JSON file?");

        ButtonType buttonTypeOne = new ButtonType("Yes");
        ButtonType buttonTypeTwo = new ButtonType("No");
        ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(buttonTypeOne, buttonTypeTwo, buttonTypeCancel);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == buttonTypeOne){
                War.loadFromJson();

        } else if (result.get() == buttonTypeTwo) {


        } else {
            System.exit(0);
        }
        WarController.startTime();
    }
}
