package War.View;

import War.BusinessLogic.Statistics;
import War.BusinessLogic.WarController;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;


import java.net.URL;
import java.util.ResourceBundle;

public class StatisticsWindowController implements Initializable{
    @FXML
    private Label launchedMissilesLabel;

    @FXML
    private Label missileHitsLabel;

    @FXML
    private Label destructedMissilesLabel;

    @FXML
    private Label destructedLaunchersLabel;

    @FXML Label totalDamageLabel;

    @FXML
    private Button exitButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Statistics statistics = WarController.getInstance().getStatistics();
        launchedMissilesLabel.setText(statistics.getNumOfLaunchedMissiles() +"");
        missileHitsLabel.setText(statistics.getNumOfHits() +"");
        destructedMissilesLabel.setText(statistics.getNumOfDestructedMissiles() +"");
        destructedLaunchersLabel.setText(statistics.getNumOfDestructedLaunchers() +"");
        totalDamageLabel.setText(statistics.getTotalDamage() +"");
        exitButton.setOnAction(e -> WarController.getInstance().exit());
    }
}
