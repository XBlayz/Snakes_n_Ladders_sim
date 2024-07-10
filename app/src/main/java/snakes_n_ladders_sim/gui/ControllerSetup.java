package snakes_n_ladders_sim.gui;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.stage.Stage;

public class ControllerSetup implements Initializable {
    // Scene switch properties
    private Stage stage;
    private Scene scene;
    private Parent root;

    // Components
    // players
    @FXML
    private Spinner<Integer> players;
    // board
    @FXML
    private Spinner<Integer> rows;
    @FXML
    private Spinner<Integer> columns;
    @FXML
    private Spinner<String> boardBuilder;
    // dice
    @FXML
    private Spinner<Integer> diceType;
    @FXML
    private Spinner<Integer> nDice;
    @FXML
    private CheckBox singleDice;
    @FXML
    private CheckBox doubleDice;
    // special cells
    @FXML
    private CheckBox stopCells;
    @FXML
    private CheckBox priceCells;
    @FXML
    // cards
    private CheckBox cards;
    @FXML
    private CheckBox extraCards;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Setup players spinner
        SpinnerValueFactory<Integer> valueFactoryPlayers = new SpinnerValueFactory.IntegerSpinnerValueFactory(2, 10);
        valueFactoryPlayers.setValue(2);
        players.setValueFactory(valueFactoryPlayers);

        // Setup rows and columns spinner
        SpinnerValueFactory<Integer> valueFactoryRows = new SpinnerValueFactory.IntegerSpinnerValueFactory(3, 15);
        SpinnerValueFactory<Integer> valueFactoryColumns = new SpinnerValueFactory.IntegerSpinnerValueFactory(3, 15);
        valueFactoryRows.setValue(10);
        valueFactoryColumns.setValue(10);
        rows.setValueFactory(valueFactoryRows);
        columns.setValueFactory(valueFactoryColumns);

        // TODO: Setup board builder spinner

        // TODO: Setup dice spinner

        // TODO: Setup dice checkboxes

        // TODO: Setup special cells checkboxes

        // TODO: Setup cards checkboxes

        // TODO: If nDice > 1, unlock singleDice and doubleDice checkbox
        // TODO: If cards is checked, unlock extraCards checkbox
    }

    public void backToMenu(ActionEvent event) throws IOException {
        // Load root node from FXML file (MainMenu)
        FileInputStream fileInputStream = new FileInputStream(new File("src/main/resources/MainMenu.fxml"));
        FXMLLoader fxmlLoader = new FXMLLoader();
        root = fxmlLoader.load(fileInputStream);

        // Get stage
        stage = (Stage)((Node) event.getSource()).getScene().getWindow();

        // Set scene
        scene = new Scene(root, 600, 400);

        // Set stage
        stage.setScene(scene);
        stage.show();

        System.out.println("Main menu"); // TODO: replace with logger
    }
}
