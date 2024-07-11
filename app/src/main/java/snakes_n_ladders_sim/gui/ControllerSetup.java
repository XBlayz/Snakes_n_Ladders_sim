package snakes_n_ladders_sim.gui;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;

import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import snakes_n_ladders_sim.simulation.entities.board_build_strategy.BoardBuildStrategy;
import snakes_n_ladders_sim.simulation.entities.board_build_strategy.concrete_builder.RandomBoardBuilder;
import snakes_n_ladders_sim.utility.MatchConfig;
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

    public void startSim() {
        // TODO: Start match scene

        buildMatch(players.getValue(), rows.getValue(), columns.getValue(), boardBuilder.getValue(), priceCells.isSelected(), stopCells.isSelected(), cards.isSelected(), extraCards.isSelected(), diceType.getValue(), nDice.getValue(), singleDice.isSelected(), doubleDice.isSelected(), nCards.getValue()).start();

        System.out.println("Simulation started"); // TODO: replace with logger
    }

    public void save() {
        // YAML parser
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        MatchConfig matchConfig = new MatchConfig(players.getValue(), rows.getValue(), columns.getValue(), boardBuilder.getValue(), priceCells.isSelected(), stopCells.isSelected(), cards.isSelected(), extraCards.isSelected(), diceType.getValue(), nDice.getValue(), singleDice.isSelected(), doubleDice.isSelected(), nCards.getValue());

        // File Chooser window
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Salva match");
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        // Set extension filter
        ExtensionFilter yamExtensionFilter = new ExtensionFilter("YAML", "*.yaml");
        fileChooser.getExtensionFilters().add(yamExtensionFilter);
        // Show save file dialog
        File file = fileChooser.showSaveDialog(stage);

        if(file != null) {
            // Try to write file
            try {
                mapper.writeValue(file, matchConfig);
                System.out.println("Match saved to: " + file.getAbsolutePath()); // TODO: replace with logger
            } catch (IOException e) {
                System.err.println("Match not saved as IOException raised"); // TODO: replace with logger
            }
        }else{
            System.out.println("Match not saved"); // TODO: replace with logger
        }
    }

    public void load() {
        // File Chooser window
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Carica match");
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        // Set extension filter
        ExtensionFilter yamExtensionFilter = new ExtensionFilter("YAML", "*.yaml");
        fileChooser.getExtensionFilters().add(yamExtensionFilter);
        // Show save file dialog
        File file = fileChooser.showOpenDialog(stage);

        // YAML parser
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        mapper.findAndRegisterModules();

        // Try to read file
        try {
            MatchConfig matchConfig = mapper.readValue(file, MatchConfig.class);
            players.getValueFactory().setValue(matchConfig.players);
            rows.getValueFactory().setValue(matchConfig.rows);
            columns.getValueFactory().setValue(matchConfig.columns);
            boardBuilder.getValueFactory().setValue(matchConfig.boardBuildStrategyString);
            priceCells.setSelected(matchConfig.isPriceCellOn);
            stopCells.setSelected(matchConfig.isStopCellOn);
            cards.setSelected(matchConfig.isCardOn);
            extraCards.setSelected(matchConfig.isExtraCardsOn);
            diceType.getValueFactory().setValue(matchConfig.diceType);
            nDice.getValueFactory().setValue(matchConfig.numberOfDice);
            singleDice.setSelected(matchConfig.isSingleDiceRuleOn);
            doubleDice.setSelected(matchConfig.isMaxDiceRuleOn);
            nCards.getValueFactory().setValue(matchConfig.numberOfEachCard);

            System.out.println("Match loaded"); // TODO: replace with logger
        } catch(StreamReadException | DatabindException e) {
            Alert a = new Alert(AlertType.ERROR);
            a.setContentText("Il file selezionato non è valido");
            a.show();
        } catch(IOException e) {
            System.err.println("Match not loaded as IOException raised"); // TODO: replace with logger
        }
    }

    private Match buildMatch(int players, int rows, int columns, String boardBuildStrategyString, boolean isPriceCellOn, boolean isStopCellOn, boolean isCardOn, boolean isExtraCardsOn, int diceType, int nDice, boolean isSingleDiceRuleOn, boolean iMaxDiceRuleOn, int nCards) {
        BoardBuildStrategy boardBuildStrategy;
        if(boardBuildStrategyString.equals(boardBuilderList.get(0))) {
            boardBuildStrategy = new RandomBoardBuilder(isPriceCellOn, isStopCellOn, isCardOn);
        }else{
            throw new IllegalStateException("Unexpected value: " + boardBuilder.getValue());
        }

        return new Match(players, rows, columns, boardBuildStrategy, isCardOn, isExtraCardsOn, diceType, nDice, isSingleDiceRuleOn, iMaxDiceRuleOn, nCards);
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
        scene = new Scene(root);

        // Set stage
        stage.setScene(scene);
        stage.setMinWidth(600);
        stage.setMinHeight(400);
        stage.show();

        System.out.println("Back to Main menu"); // TODO: replace with logger
    }
}
