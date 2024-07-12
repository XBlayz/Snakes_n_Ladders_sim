package snakes_n_ladders_sim.gui;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ControllerMainMenu {
    // Scene switch properties
    private Stage stage;
    private Scene scene;
    private Parent root;

    public void newSim(ActionEvent event) throws IOException {
        // Load root node from FXML file (SimSetup)
        FileInputStream fileInputStream = new FileInputStream(new File("src/main/resources/SimSetup.fxml"));
        FXMLLoader fxmlLoader = new FXMLLoader();
        root = fxmlLoader.load(fileInputStream);

        // Get stage
        stage = (Stage)((Node) event.getSource()).getScene().getWindow();

        // Set scene
        scene = new Scene(root);

        // Set stage
        stage.setScene(scene);
        stage.setMinWidth(600);
        stage.setMinHeight(400);
        stage.show();

        log.trace("Switched sceene: SimSetup");
    }

    public void exit(ActionEvent event) {
        // Get stage
        stage = (Stage)((Node) event.getSource()).getScene().getWindow();

        // Close stage
        stage.close();

        log.trace("Application exited");
    }

}
