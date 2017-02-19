package me.vitorgreati.tttfx.control;

/**
 * Stores the constants used in the game.
 * 
 * @author Vitor Greati
 * */
public class GameConstants {
	
	/** Constants to represents the players. */
	public static final char X = 'x';
	public static final char O = 'o';
	
	/** Represents the empty spot. */
	public static final char UNSET = ' '; 
	
	/**
	 * Returns the enemy of a player.
	 * 
	 * @param currentPlayer Current player.
	 * @return The other player.
	 * */
	public static char getOtherPlayer(char currentPlayer) {
		return currentPlayer == GameConstants.X ? GameConstants.O : GameConstants.X;
	}
}
