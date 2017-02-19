package me.vitorgreati.tttfx.control;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * The Minimax Tree. It has nodes
 * that represent the possible states of
 * the game. Every node has a value for the player
 * and keeps the best next move after the
 * tree is entirely built in memory.
 * 
 * @author Vitor Greati.
 * */
public class AIMinMax {

	/** The root of the tree. */
	private Node rootMinMax = null;
	
	/** Who's the AI player? */
	private char me = GameConstants.X;
	
	/** Where is the current state of the game? */
	private Node currentState;
	
	/** Find helper. */
	public Node found;
	
	/**
	 * A node of the Minimax Tree.
	 * 
	 * @author Vitor Greati
	 * */
	public class Node {
		/** The state of the game in this node */
		private GameState state;
		/** The score of this node */
		private int value;
		/** What's the best next move for the AI player? */
		private GameControl.Movement bestNextMove;
		/** Possible moves from this state */
		private ArrayList<Node> children = new ArrayList<Node>();
		
		/**
		 * Constructor which takes an state.
		 * */
		public Node(GameState state) {
			this.setState(state);
		}
		
		/**
		 * Print children.
		 * 
		 * @return The string representation of the children.
		 * */
		public String childrenToString() {
			String res = "";
			for (Node c : getChildren()) {
				res += c.getState().toString() + "V = " + c.getValue() + "\n\n";
			}
			return res;
		}
		
		/**
		 * Getters and Setters.
		 * */
		
		public GameState getState() {
			return state;
		}
		
		public void setState(GameState state) {
			this.state = state;
		}

		public GameControl.Movement getBestNextMove() {
			return bestNextMove;
		}

		public void setBestNextMove(GameControl.Movement bestNextMove) {
			this.bestNextMove = bestNextMove;
		}

		public ArrayList<Node> getChildren() {
			return children;
		}

		public void setChildren(ArrayList<Node> children) {
			this.children = children;
		}

		public int getValue() {
			return value;
		}

		public void setValue(int value) {
			this.value = value;
		}
	}
	
	/**
	 * Constructor that builds the minimax tree in memory.
	 * */
	public AIMinMax(char me) {
		this.setMe(me);
		rootMinMax = new Node(new GameState());
		currentState = rootMinMax;
		buildChildren(rootMinMax);
	}
	
	/**
	 * Given a state (a node), it builds all states
	 * derived from it and give points to all of them,
	 * following the minimax logic. In fact,
	 * it builds the minimax tree.
	 * 
	 * @param root A GameState
	 * */
	private void buildChildren(Node root) {
		// Se houve ganhador
		if (root.getState().hasWinner()) {
			root.setValue(root.getState().getNextPlayer() == getMe() ? 10 : -10);
		// Se empatou
		} else if (root.getState().isDraw()) {
			root.setValue(0);
		// Se há jogadas para serem feitas
		} else {
			for (int i = 0; i < 3; ++i) {
				for (int j = 0; j < 3; ++j) {
					if (root.getState().getBoard()[i][j] == GameConstants.UNSET) {
						try {
							Node newNode = new Node(root.getState().makeState(i, j));
							root.getChildren().add(newNode);
							buildChildren(newNode);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
			}
			// Calcula o valor deste estado
			// Se a AI é a próxima a jogar, maximiza
			if (root.getState().getNextPlayer() == getMe()) {
				Node maxChild = root.getChildren()
						            .stream()
						            .max((n1,n2)->Integer.compare(n1.getValue(), n2.getValue()))
						            .get();
				root.setValue(maxChild.getValue());
				root.setBestNextMove(maxChild.getState().getMov());
			} 
			// Se o oponente da AI é o próximo a jogar, minimiza
			else {
				Node minChild = root.getChildren()
						            .stream()
						            .min((n1,n2)->Integer.compare(n1.getValue(), n2.getValue()))
						            .get();
				root.setValue(minChild.getValue());
				root.setBestNextMove(minChild.getState().getMov());
			}
		}
	}
	
	/**
	 * Updates the pointer that indicates the
	 * current game state inside the tree.
	 * 
	 * @param currentState Current state (new state)
	 * */
	public void updateState(GameState newCurrentState) {
		find(newCurrentState);
		currentState = found;
	}

	/**
	 * Given a state, just return a pointer
	 * to it inside the tree, so that all
	 * children can be accessed. It starts
	 * from the root of the tree.
	 * 
	 * @param state A GameState.
	 * @return The node having the game state.
	 * */
	public Node find(GameState state) {
		found = null;
		find(rootMinMax, state);
		return found;
	}
	
	/**
	 * Given a state, just return a pointer
	 * to it inside the tree, so the all
	 * children can be accessed. It
	 * starts from a given root.
	 * 
	 * @param root A node to start from.
	 * @param state A GameState.
	 * @return The node having the game state.
	 * */
	public void find(Node root, GameState state) {
		if (root.getState().equals(state)) {
			found = root;
		}
		else if (!root.getChildren().isEmpty())
			for (Node child : root.getChildren())
				find(child, state);
	}
	
	/**
	 * Reset AI player. Effectively, restart the pointer 
	 * used to keep track of the current state of
	 * the game.
	 * */
	public void reset() {
		this.currentState = rootMinMax;
	}
	
	/** Getters and setters. */
	public Node getCurrentState() {
		return currentState;
	}

	public void setCurrentState(Node currentState) {
		this.currentState = currentState;
	}

	public char getMe() {
		return me;
	}

	public void setMe(char me) {
		this.me = me;
	}
	
}
