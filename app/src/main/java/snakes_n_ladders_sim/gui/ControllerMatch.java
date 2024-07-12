package snakes_n_ladders_sim.gui;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import lombok.extern.slf4j.Slf4j;

import snakes_n_ladders_sim.simulation.Match;
import snakes_n_ladders_sim.utility.Tuple;

@Slf4j
public class ControllerMatch implements Initializable {
    // Scene switch properties
    private Stage stage;
    private Scene scene;
    private Parent root;

    // Match references
    private Match match;

    @FXML
    private GridPane board;
    private HBox[] playerBoxArray;

    @FXML
    private TableView<Tuple<String, String>> playerList;
    @FXML
    private TableColumn<Tuple<String, String>, String> playerName;
    @FXML
    private TableColumn<Tuple<String, String>, String> isPlayerTurn;

    @FXML
    private CheckBox autoRun;
    @FXML
    private Button nextButton;
    @FXML
    private Button backButton;
    @FXML
    private Button exitButton;
    ObservableList<Tuple<String, String>> playerListData = FXCollections.observableArrayList();

    @FXML
    private Label textEvent;
    @FXML
    private Label roundText;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        match = ControllerSetup.getMatch();
        int rows = match.getBoard().rows;
        int columns = match.getBoard().columns;

        // Create board
        playerBoxArray = new HBox[columns*rows];
        for (int i = 1; i < match.getBoard().end+1; i++) {
            String name = match.getBoard().getCell(i).toString();
            String number = String.format("%02d", i);

            log.debug("Added: {} ({}) | x: {}, y: {}", name, number, getX(i, rows, columns), getY(i, columns));
            board.add(getNewCell(name, number), getY(i, columns), getX(i, rows, columns));
        }

        // Setup player list
        playerName.setCellValueFactory(new PropertyValueFactory<>("x"));
        isPlayerTurn.setCellValueFactory(new PropertyValueFactory<>("y"));

        int n = match.getNumberOfPlayers();
        for (int i = 0; i < n; i++) {
            playerListData.add(new Tuple<>("Giocatore "+(i+1), ""));
        }
        playerList.setItems(playerListData);

        // Start the simulation
        match.setController(this);
        match.start();
    }

    private SplitPane getNewCell(String name, String number) {
        // Create labels
        Label cellName = new Label(name);
        Label cellNumber = new Label(number);

        // Create cell bottom
        HBox playerBox = new HBox();
        playerBox.setAlignment(Pos.CENTER);
        playerBoxArray[Integer.parseInt(number)-1] = playerBox;
        VBox bottom = new VBox(cellName, playerBox);
        bottom.setAlignment(Pos.CENTER);
        // Create cell top
        HBox top = new HBox(cellNumber);
        top.setAlignment(Pos.CENTER);

        // Create cell
        SplitPane cellPane = new SplitPane(top, bottom);

        // Setup split
        cellPane.setOrientation(Orientation.VERTICAL);
        cellPane.setDividerPositions(0.25);
        // Setup dimensions
        cellPane.setMinSize(25, 25);
        cellPane.setPrefSize(100, 100);
        cellPane.setMaxSize(100, 100);

        return cellPane;
    }

    private int getX(int i, int r, int c) {
        return (r-1) - ((i-1)/c);
    }

    private int getY(int i, int c) {
        i--;
        int v = i/c;

        if (v%2 == 0) {
            return i%c;
        } else {
            return (c-1) - (i%c);
        }
    }

    // Toggle next button with auto run
    public void autoRunListener() {
        if(autoRun.isSelected()) {
            nextButton.setDisable(true);
            log.info("Auto run: ON");
            match.setAutoRun(true);
        } else {
            nextButton.setDisable(false);
            log.info("Auto run: OFF");
            match.setAutoRun(false);
        }
    }

    public void nextTurn(ActionEvent event) {
        log.info("Next turn (manual)");
        match.notifyNextTurn();
    }

    public void backToSetup(ActionEvent event) throws IOException {
        endMatch();

        // Load root node from FXML file (Match)
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

        log.trace("Switched scene: Match (Back)");
    }

    public void exitToMainMenu(ActionEvent event) throws IOException {
        endMatch();

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

        log.trace("Switched scene: MainMenu (Back)");
    }

    private void endMatch() {
        match.interrupt();
    }

    public void appendText(String message) {
        textEvent.setText(textEvent.getText() + "\n" + message);
    }

    public void clearText() {
        textEvent.setText("---");
    }

    public void printText(String message) {
        textEvent.setText(message);
    }

    public void setRound(int round) {
        roundText.setText("Round: " + round);
    }

    public void setCurrentTurn(int playerN) {
        if(playerN == 0) {
            playerListData.set(playerListData.size()-1, new Tuple<>("Giocatore " + playerListData.size(), ""));
        } else {
            playerListData.set(playerN-1, new Tuple<>("Giocatore " + playerN, ""));
        }
        playerListData.set(playerN, new Tuple<>("Giocatore " + (playerN+1), "(X)"));
    }

    public void movePlayer(int oldPos, int newPos, int playerN) {
        if(oldPos > 0) {
            log.debug("Old position: " + oldPos);
            HBox oldPlayerBox = playerBoxArray[oldPos-1];
            log.debug("Old pos found: " + oldPlayerBox.getChildren().remove(0));
        }
        log.debug("New position: " + newPos);
        HBox newPlayerBox = playerBoxArray[newPos-1];
        newPlayerBox.getChildren().add(new Label("("+(playerN+1)+")"));
    }
}
