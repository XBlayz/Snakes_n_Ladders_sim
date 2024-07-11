package snakes_n_ladders_sim.gui;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.List;

import javafx.collections.FXCollections;
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

import snakes_n_ladders_sim.simulation.entities.board_build_strategy.BoardBuildStrategy;
import snakes_n_ladders_sim.simulation.entities.board_build_strategy.concrete_builder.RandomBoardBuilder;
import snakes_n_ladders_sim.simulation.Match;

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
    List<String> boardBuilderList = List.of("Random");
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
    @FXML
    private Spinner<Integer> nCards;

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

        // Setup board builder spinner
        SpinnerValueFactory<String> valueFactoryBoard = new SpinnerValueFactory.ListSpinnerValueFactory<>(FXCollections.observableList(boardBuilderList));
        boardBuilder.setValueFactory(valueFactoryBoard);
        boardBuilder.setEditable(false);

        // Setup dice spinner
        SpinnerValueFactory<Integer> valueFactoryDiceType = new SpinnerValueFactory.IntegerSpinnerValueFactory(6, 6);
        SpinnerValueFactory<Integer> valueFactoryDiceNumber = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 2);
        valueFactoryDiceType.setValue(6);
        valueFactoryDiceNumber.setValue(2);
        diceType.setValueFactory(valueFactoryDiceType);
        nDice.setValueFactory(valueFactoryDiceNumber);

        // If nDice > 1, unlock singleDice and doubleDice checkbox
        nDice.valueProperty().addListener((obs, oldValue, newValue) -> {
            if(newValue > 1) {
                singleDice.setDisable(false);
                doubleDice.setDisable(false);
            }else{
                singleDice.setSelected(false);
                singleDice.setDisable(true);
                doubleDice.setSelected(false);
                doubleDice.setDisable(true);
            }
        });

        // Setup nCards spinner
        SpinnerValueFactory<Integer> valueFactoryCardsNumber = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 5);
        valueFactoryCardsNumber.setValue(3);
        nCards.setValueFactory(valueFactoryCardsNumber);
    }

    public void startSim(ActionEvent event) {
        BoardBuildStrategy boardBuildStrategy;
        if(boardBuilder.getValue().equals(boardBuilderList.get(0))) {
            boardBuildStrategy = new RandomBoardBuilder(priceCells.isSelected(), stopCells.isSelected(), cards.isSelected());
        }else{
            throw new IllegalStateException("Unexpected value: " + boardBuilder.getValue());
        }

        Match match = new Match(players.getValue(), rows.getValue(), columns.getValue(), boardBuildStrategy, cards.isSelected(), extraCards.isSelected(), diceType.getValue(), nDice.getValue(), singleDice.isSelected(), doubleDice.isSelected(), 3);

        match.start();
    }

    // If cards is checked, unlock extraCards checkbox
    public void cardsListener() {
        if(cards.isSelected()) {
            extraCards.setDisable(false);
            nCards.setDisable(false);
        }else{
            extraCards.setSelected(false);
            extraCards.setDisable(true);
            nCards.setDisable(true);
        }
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
