package me.vitorgreati.tttfx.control;

import java.util.Arrays;

import me.vitorgreati.tttfx.control.GameControl.Movement;

/**
 * Representa um estado do jogo.
 * 
 * @author Vitor Greati
 * */
public class GameState {
	
	/** Board's snapshot */
	private char[][] board = new char[3][3];
	
	/** State's status */
	private GameStatus status = GameStatus.STOPPED;
	
	/** Player in this state */
	private char nextPlayer = GameConstants.X;
	
	/** Line/Column/Diag in which the player has won the game (if it's the case) */
	private int winPosition = -1;
	
	/** Moviment that generated this state */
	private GameControl.Movement mov;
	
	/** Constants for representing the status */
	public enum GameStatus {
		WIN_COLUMN,
		WIN_LINE,
		WIN_DIAG,
		DRAW,
		STARTED,
		STOPPED,
		PAUSED
	}
	
	/**
	 * Basic constructor.
	 * */
	public GameState() {
		reset();
	}
	
	/**
	 * Reset the state: board, player and status
	 * */
	public void reset() {
		for (int i = 0; i < 3; ++i)
			for (int j = 0; j < 3; ++j)
				board[i][j] = GameConstants.UNSET;
		this.nextPlayer = GameConstants.X;
		status = GameStatus.STOPPED;
		winPosition = -1;
		mov = null;
	}
	
	
	/**
	 * Update this state, trying to play in the
	 * position (i,j). If well succeeded,
	 * updates algo the player in this state.
	 * 
	 * @param i Line of the position to be occupied.
	 * @param j Column of the position to be occupied.
	 * */
	public void updateThis(int i, int j) throws Exception {
		// Can't put in this cell
		if (board[i][j] != GameConstants.UNSET)
			throw new Exception("Esta casa já foi escolhida.");
		// Free cell
		board[i][j] = getNextPlayer();
		mov = new GameControl.Movement(getNextPlayer(), i, j);
		updateStatus();
		if (!hasWinner())
			setNextPlayer(GameConstants.getOtherPlayer(getNextPlayer()));
	}
	
	/**
	 * Creates a new object GameState from a movement in
	 * this state. Updates the board and the
	 * player of the new state, return it.
	 * 
	 * @param i Line of the position to be occupied.
	 * @param j Column of the position to be occupied.
	 * */
	public GameState makeState(int i, int j) throws Exception {
		// Can't put in this cell
		if (board[i][j] != GameConstants.UNSET)
			throw new Exception("Esta casa já foi escolhida.");
		// Create a new state
		GameState newState = new GameState();
		// Make a copy of the board
		char[][] newBoard = new char[3][3];
		for (int k = 0; k < 3; ++k)
			newBoard[k] = Arrays.copyOf(getBoard()[k], 3);
		newBoard[i][j] = nextPlayer;
		newState.setNextPlayer(nextPlayer);
		newState.setBoard(newBoard);
		newState.setMov(new Movement(nextPlayer, i, j));
		newState.updateStatus();
		if (!newState.hasWinner())
			newState.setNextPlayer(GameConstants.getOtherPlayer(nextPlayer));
		return newState;
	}
	
	/**
	 * Given the current board, return and set the status of
	 * the state.
	 * 
	 * @return The GameStatus for that board
	 * */
	public GameStatus updateStatus() {
		// Checar se player ganhou em linha ou coluna
		boolean ganhou;
		// Checar se player ganhou em diagonal (principal / secundária)
		boolean diagonal[] = {true, true};
		// Variáveis de loop
		int i, j;
		// Loop de checagem
		for (i = 0; i < 3; ++i) {
			// Horizontal
			ganhou = true;
			for (j = 0; j < 3; ++j)
				ganhou &= board[i][j] == getNextPlayer();
			if (ganhou) {
				status = GameStatus.WIN_LINE;
				setWinPosition(i);
				return status;
			}
			// Vertical
			ganhou = true;
			for (j = 0; j < 3; ++j)
				ganhou &= board[j][i] == getNextPlayer();
			if (ganhou) {
				status = GameStatus.WIN_COLUMN;
				setWinPosition(i);
				return status;
			}
			// Diagonal principal
			diagonal[0] &= board[i][i] == getNextPlayer();
			diagonal[1] &= board[i][2-i] == getNextPlayer();
		}
		if (diagonal[0] || diagonal[1]) {
			status = GameStatus.WIN_DIAG;
			if (diagonal[0]) setWinPosition(0);
			if (diagonal[1]) setWinPosition(1);
			return status;
		}
		setWinPosition(-1);
		return status;
	}
	
	/**
	 * Count how many empty spaces
	 * there exist in the board.
	 * 
	 * @return Number of empty spaces.
	 * */
	public int countEmptySpots() {
		int c = 0;
		for (int i = 0; i < 3; ++i)
			for (int j = 0; j < 3; ++j)
				if (board[i][j] == GameConstants.UNSET) c++;
		return c;
	}
	
	/**
	 * Generates a string representation of the object.
	 * 
	 * @return String representation of the state.
	 * */
	@Override
	public String toString() {
		String boardstr = "";
		for (int i = 0; i < 3; ++i) {
			for (int j = 0; j < 3; ++j)
				boardstr += board[i][j];
			boardstr += "\n";
		}
		return boardstr;
	}
	
	
	/**
	 * Indicates if the state has 
	 * a winner.
	 * 
	 * @return There is a winner.
	 * */
	public boolean hasWinner() {
		return (status == GameState.GameStatus.WIN_COLUMN || 
				status == GameState.GameStatus.WIN_LINE ||
				status == GameState.GameStatus.WIN_DIAG);
	}
	
	/**
	 * Indicates if the game is draw.
	 * 
	 * @return It's draw.
	 * */
	public boolean isDraw() {		
		return !hasWinner() && countEmptySpots() == 0;
	}
	
	/**
	 * Equals method for comparing two states.
	 * Consider only the board's configuration.
	 * 
	 * @param other The other state.
	 * @return Equal.
	 * */
	@Override
	public boolean equals(Object other){
		if (!(other instanceof GameState))
			return false;
		for (int i = 0; i < 3; ++i)
			for (int j = 0; j < 3; ++j)
				if (getBoard()[i][j] != ((GameState) other).getBoard()[i][j])
					return false;
		return true;
	}
	
	/**
	 * Getters and setters.
	 * */
	public char[][] getBoard(){
		return this.board;
	}
	
	public void setBoard(char[][] tabState) {
		this.board = tabState;
	}

	public GameStatus getStatus() {
		return status;
	}

	public void setStatus(GameStatus status) {
		this.status = status;
	}

	public char getNextPlayer() {
		return nextPlayer;
	}

	public void setNextPlayer(char nextPlayer) {
		this.nextPlayer = nextPlayer;
	}

	public int getWinPosition() {
		return winPosition;
	}

	public void setWinPosition(int winPosition) {
		this.winPosition = winPosition;
	}

	public GameControl.Movement getMov() {
		return mov;
	}

	public void setMov(GameControl.Movement mov) {
		this.mov = mov;
	}
	
}
