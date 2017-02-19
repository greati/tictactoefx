package me.vitorgreati.tttfx.control;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Represents the game control.
 * It has a unique gameState that's
 * updated according to the movements.
 * 
 * @author Vitor Greati
 * */

public class GameControl {
	
	/** Current state of the game. */
	private GameState gameState = new GameState();
	/** Game history. */
	private ArrayList<Movement> history = new ArrayList<Movement>();
	/** The AI player. */
	private AIMinMax aiPlayer = new AIMinMax(GameConstants.O);
	/** The game mode. */
	private GameMode gameMode;
	/** Player of the turn */
	private char whoPlays = GameConstants.X;
	
	/**
	 * Represents a movement in the game.
	 * 
	 * @author Vitor Greati.
	 * */
	public static class Movement {
		private char player;
		private int[] pos = new int[2];
		
		public Movement(char player, int i, int j) {
			this.setPlayer(player);
			this.getPos()[0] = i;
			this.getPos()[1] = j;
		}

		public int[] getPos() {
			return pos;
		}

		public void setPos(int[] pos) {
			this.pos = pos;
		}

		public char getPlayer() {
			return player;
		}

		public void setPlayer(char player) {
			this.player = player;
		}
	}
	
	/**
	 * Game modes.
	 * */
	public enum GameMode {
		P_V_P,
		P_V_AI
	}
	
	/**
	 * Basic constructor.
	 * */
	public GameControl(){}
	
	/**
	 * Set player char.
	 * */
	public void setAIChar(char playerChar) {
		aiPlayer.setMe(playerChar);
	}
	
	/**
	 * Starts the board and the history.
	 * 
	 * */
	public void reset() {
		gameState.reset();
		history.clear();
	}
	
	/**
	 * Toggle who plays.
	 * */
	public void toggleWhoPlays() {
		whoPlays = whoPlays == GameConstants.X ? GameConstants.O : GameConstants.X;
	}
	
	/**
	 * Makes a move, updating the state, the history
	 * and the AI.
	 * 
	 * @param i Line
	 * @param j Column
	 * @return The player.
	 * */
	public char play(int i, int j) throws Exception {
		char currentPlayer = gameState.getNextPlayer();
		gameState.updateThis(i, j);
		history.add(new Movement(currentPlayer, i, j));
		getAiPlayer().updateState(gameState);
		return currentPlayer;
	}

	/**
	 * Prints in the console the 
	 * current game state.
	 * */
	public void print() {
		System.out.println(gameState);
	}
	
	/** Getters and Setters */
	public GameState getGameState() {
		return this.gameState;
	}
	
	public ArrayList<Movement> getHistory() {
		return this.history;
	}

	public AIMinMax getAiPlayer() {
		return aiPlayer;
	}

	public void setAiPlayer(AIMinMax aiPlayer) {
		this.aiPlayer = aiPlayer;
	}

	public GameMode getGameMode() {
		return gameMode;
	}

	public void setGameMode(GameMode gameMode) {
		this.gameMode = gameMode;
	}

	public char getWhoPlays() {
		return whoPlays;
	}

	public void setWhoPlays(char whoPlays) {
		this.whoPlays = whoPlays;
	}
}
