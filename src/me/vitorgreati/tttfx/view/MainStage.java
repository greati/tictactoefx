package me.vitorgreati.tttfx.view;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import me.vitorgreati.tttfx.control.GameConstants;
import me.vitorgreati.tttfx.control.GameControl;
import me.vitorgreati.tttfx.control.GameState;

/**
 * Main interface of the game.
 * 
 * @autor Vitor Greati.
 * */
public class MainStage extends Application {
	
	// The game logic
	private GameControl game = new GameControl();
	
	// GUI Components
	final Text currentPlayerText = new Text();
	final Text winnerText = new Text();
	final Button btReset = new Button("Reiniciar");
	final GridPane gridPane = new GridPane();
	private Scene initialScene = null;
	private Scene mainScene = null;
	final Button btMenu = new Button("Menu");
	
	final Button selectAI = new Button("Player vs. AI");
	final Button selectPVP = new Button("Player vs. Player");
	final Button selectX = new Button("X");
	final Button selectO = new Button("O");
	final Button startButton = new Button("Start!");

	
	@Override
	public void start(Stage primaryStage) throws Exception {		
		primaryStage.setMinHeight(500);
		primaryStage.setMinWidth(500);
		
		// Create master container
		FlowPane generalPane = new FlowPane();
		generalPane.setOrientation(Orientation.VERTICAL);
		generalPane.setMinHeight(500);
		generalPane.setMinWidth(500);
		generalPane.setAlignment(Pos.CENTER);
		
		// Hud
		HBox hud = new HBox(15);
		hud.setMinWidth(500);
		hud.getChildren().add(currentPlayerText);
		hud.getChildren().add(winnerText);
		hud.getChildren().add(btReset);
		hud.getChildren().add(btMenu);
		
	    //HBox.setMargin(currentPlayerText,new Insets(0, 60, 0, 5));
	    //HBox.setMargin(btReset, new Insets(0, 0, 0, 35));
	    //HBox.setMargin(btMenu, new Insets(0, 0, 0, 65));
	    
		currentPlayerText.setText("Vez de " + game.getGameState().getNextPlayer());
		
		winnerText.setText("Em curso.");
		
		generalPane.getChildren().add(hud);
		
		// Style of grid
		gridPane.setHgap(4.0);
		gridPane.setVgap(4.0);
		
		// Insert labels (clickable labels)
		buildGrid();
		
		generalPane.getChildren().add(gridPane);
		
		// Configure reset button
		btReset.setOnAction(event->{
			buildGrid();
			game.setWhoPlays(GameConstants.X);
			game.getGameState().reset();
			game.getAiPlayer().reset();
			this.winnerText.setText("Em curso.");
			this.currentPlayerText.setText("Vez de " + game.getGameState().getNextPlayer());
			// AI starts
			if (game.getGameMode() == GameControl.GameMode.P_V_AI && 
					game.getWhoPlays() == game.getAiPlayer().getMe()) {
				try {
					performAI();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		
		btMenu.setOnAction(event->{
			primaryStage.setScene(initialScene);
		});
		
		// Creates the initial scene
		FlowPane initialPane = new FlowPane(10,10);
		initialPane.setOrientation(Orientation.VERTICAL);
		initialPane.setMinHeight(500);
		initialPane.setMinWidth(500);
		initialPane.setAlignment(Pos.CENTER);
		
		Text logo = new Text("TIC-TAC-TOE FX!");
		logo.getStyleClass().add("logo");
		initialPane.getChildren().add(logo);
		
		HBox modeBox = new HBox(10);
		modeBox.setMinWidth(500);
		
		selectAI.setOnAction(event->{
			game.setGameMode(GameControl.GameMode.P_V_AI);
			selectAI.setStyle("-fx-border-color:black");
			selectPVP.setStyle("-fx-border-color:gray");
		});
		
		
		selectPVP.setOnAction(event->{
			game.setGameMode(GameControl.GameMode.P_V_P);
			selectPVP.setStyle("-fx-border-color:black");
			selectAI.setStyle("-fx-border-color:gray");
		});
		
		modeBox.getChildren().addAll(selectAI, selectPVP);
		initialPane.getChildren().add(modeBox);
		
		HBox playerBox = new HBox(10);
		playerBox.setMinWidth(500);
		
		selectX.setOnAction(event->{
			if(game.getGameMode() == GameControl.GameMode.P_V_AI)
				game.setAIChar(GameConstants.O);
			selectX.setStyle("-fx-border-color:black");
			selectO.setStyle("-fx-border-color:gray");

		});
		
		selectO.setOnAction(event->{
			if(game.getGameMode() == GameControl.GameMode.P_V_AI)
				game.setAIChar(GameConstants.X);
			selectO.setStyle("-fx-border-color:black");
			selectX.setStyle("-fx-border-color:gray");
		});
		
		playerBox.getChildren().addAll(selectX, selectO);
		initialPane.getChildren().add(playerBox);
		
		startButton.setOnAction(event->{
			primaryStage.setScene(mainScene);
			buildGrid();
			game.setWhoPlays(GameConstants.X);
			game.getGameState().reset();
			game.getAiPlayer().reset();
			this.winnerText.setText("Em curso.");
			this.currentPlayerText.setText("Vez de " + game.getGameState().getNextPlayer());
			// AI starts
			if (game.getGameMode() == GameControl.GameMode.P_V_AI && 
					game.getWhoPlays() == game.getAiPlayer().getMe()) {
				try {
					performAI();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		
		initialPane.getChildren().add(startButton);
		
		initialScene = new Scene(initialPane, 500, 500);
		initialScene.getStylesheets().add("me/vitorgreati/tttfx/view/style.css");
		
		// Creates the main scene (the board)
		mainScene = new Scene(generalPane, 500, 500);
		
		// Put the initial scene on the screen
		primaryStage.setScene(initialScene);
		primaryStage.setFullScreen(false);
		primaryStage.setMaximized(false);
		primaryStage.setTitle("TicTacToeFX!");
		primaryStage.show();
	}
	
	/**
	 * Constructs the grid of the game.
	 * Makes all labels clickable using
	 * lambdas for associating the
	 * events.
	 * 
	 * */
	private void buildGrid() {
		// Iniciar a interface
		for (int i = 1; i <= 3; ++i) {
			for (int j = 1; j <= 3; ++j) {
				Label l = new Label(String.valueOf(GameConstants.UNSET));
				l.setMinSize(100, 100);
				l.setAlignment(Pos.CENTER);
				l.setDisable(false);
				l.setStyle("-fx-background-color:#ccc; -fx-font-color:#FFF; -fx-font-size:30px;");
				// Take current position (necessary for lambda)
				int pos[] = {i, j};
				// For each label, links an event
				l.setOnMouseClicked((event)->{
					try {
						// Normal player
						// Play!
						if (!game.getGameState().hasWinner())
							l.setText(String.valueOf(game.play(pos[0] - 1,pos[1] - 1)));
						// After normal play!
						if (game.getGameState().hasWinner())
							paintWinner();
						else if (game.getGameState().isDraw())
							winnerText.setText("Empatou!");
						else
							currentPlayerText.setText("Vez de " + game.getGameState().getNextPlayer());
						
						game.toggleWhoPlays();

						// AI plays
						if (game.getGameMode() == GameControl.GameMode.P_V_AI)
						performAI();
						
					} catch(Exception e) {
						e.printStackTrace();
					}
				});
				gridPane.add(l, j, i);
			}
		}
	}
	
	/**
	 * AI Moves.
	 * */
	public void performAI() throws Exception{
		// AI player
		if (!game.getGameState().hasWinner()) {
			GameControl.Movement aiMov = game.getAiPlayer().getCurrentState().getBestNextMove();
			char justPlayed = game.play(aiMov.getPos()[0], aiMov.getPos()[1]);
			for (Node c : gridPane.getChildren()) {
				if (gridPane.getRowIndex(c) == aiMov.getPos()[0] + 1 && 
				    gridPane.getColumnIndex(c) == aiMov.getPos()[1] + 1) {
					((Label)c).setText(String.valueOf(justPlayed));
				}
			}
		}
		// After AI play!
		if (game.getGameState().hasWinner())
			paintWinner();
		else if (game.getGameState().isDraw())
			winnerText.setText("Empatou!");
		else
			currentPlayerText.setText("Vez de " + game.getGameState().getNextPlayer());		
	}

	/**
	 * If the current game has a winner,
	 * paints the line.
	 * 
	 * */
	private void paintWinner() {
		if (game.getGameState().hasWinner()) {
			winnerText.setText(game.getGameState().getNextPlayer() + MensagensHUD.WIN);
			
			for (Node c : gridPane.getChildren()) {
				if (game.getGameState().getStatus() == GameState.GameStatus.WIN_LINE) {
					if (gridPane.getRowIndex(c) - 1 == game.getGameState().getWinPosition())
						c.setStyle("-fx-background-color:green;-fx-font-color:#FFF; -fx-font-size:30px;");
					
				} else if (game.getGameState().getStatus() == GameState.GameStatus.WIN_COLUMN) {
					if (gridPane.getColumnIndex(c) - 1 == game.getGameState().getWinPosition())
						c.setStyle("-fx-background-color:green;-fx-font-color:#FFF; -fx-font-size:30px;");
				} else if (game.getGameState().getStatus() == GameState.GameStatus.WIN_DIAG) {
					if (game.getGameState().getWinPosition() == 0){
						if (gridPane.getRowIndex(c) == gridPane.getColumnIndex(c)) {
							c.setStyle("-fx-background-color:green;-fx-font-color:#FFF; -fx-font-size:30px;");
						}
					} else {
						if (gridPane.getRowIndex(c) + gridPane.getColumnIndex(c) == 4) {
							c.setStyle("-fx-background-color:green;-fx-font-color:#FFF; -fx-font-size:30px;");
						}									}
						
				}
			}
				
		}
	}
	
	/**
	 * Executes the game.
	 * */
	public static void main(String[] args) {
		launch(args);
	}
	
}
