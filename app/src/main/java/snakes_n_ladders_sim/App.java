package snakes_n_ladders_sim;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;
import ch.qos.logback.core.joran.spi.JoranException;

import snakes_n_ladders_sim.gui.ControllerSetup;
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
        stage.setMinWidth(600);
        stage.setMinHeight(400);
        stage.show();
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        Match match = ControllerSetup.getMatch();
        if(match != null) {
            match.interrupt();
        }
    }

    private static void setupLogger() {
        // Reset the logger context
        LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
        loggerContext.reset();

        // Load logback file configuration
        JoranConfigurator configurator = new JoranConfigurator();
        configurator.setContext(loggerContext);

        try {
            // loads logback file
            InputStream configStream = new FileInputStream(new File("src/main/resources/config/logback.xml"));
            configurator.doConfigure(configStream);
            configStream.close();
        } catch (JoranException | IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        setupLogger(); // Setup logger

        launch(args); // Start the application
    }
}