package snakes_n_ladders_sim;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import snakes_n_ladders_sim.simulation.entities.board_build_strategy.BoardBuildStrategy;
import snakes_n_ladders_sim.simulation.entities.board_build_strategy.concrete_builder.RandomBoardBuilder;
import snakes_n_ladders_sim.simulation.Match;

public class App extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        // Load root node from FXML file (MainMenu)
        FileInputStream fileInputStream = new FileInputStream(new File("src/main/resources/MainMenu.fxml"));
        FXMLLoader fxmlLoader = new FXMLLoader();
        Parent root = fxmlLoader.load(fileInputStream);

        // Set the scene
        Scene scene = new Scene(root, 600, 400);
        stage.setTitle("Snakes & Ladders - Simulator (v0.1)");

        // Set the stage
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) throws InterruptedException {
        launch(args);

        //BoardBuildStrategy boardBuildStrategy = new RandomBoardBuilder(true, true, true);
        //Match match = new Match(5, 10, 10, boardBuildStrategy, 6, 2, true, true, 3);

        //match.start();
    }

}