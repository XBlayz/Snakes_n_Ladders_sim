package snakes_n_ladders_sim;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import snakes_n_ladders_sim.simulation.entities.board_build_strategy.BoardBuildStrategy;
import snakes_n_ladders_sim.simulation.entities.board_build_strategy.concrete_builder.RandomBoardBuilder;
import snakes_n_ladders_sim.simulation.Match;

public class App extends Application {

    @Override
    public void start(Stage stage) {
        String javaVersion = System.getProperty("java.version");
        String javafxVersion = System.getProperty("javafx.version");
        Label l = new Label("Hello, JavaFX " + javafxVersion + ", running on Java " + javaVersion + ".");
        Scene scene = new Scene(new StackPane(l), 640, 480);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) throws InterruptedException {
        BoardBuildStrategy boardBuildStrategy = new RandomBoardBuilder(true, true, true);
        Match match = new Match(5, 10, 10, boardBuildStrategy, 6, 2, true, true, 3);

        match.start();
    }

}