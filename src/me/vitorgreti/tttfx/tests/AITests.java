package me.vitorgreti.tttfx.tests;

import me.vitorgreati.tttfx.control.AIMinMax;
import me.vitorgreati.tttfx.control.GameConstants;
import me.vitorgreati.tttfx.control.GameState;

public class AITests {

	private static AIMinMax ai = new AIMinMax(GameConstants.O);
	
	public static void main(String[] args) {
		
		char tab[][] = {{GameConstants.X,GameConstants.UNSET,GameConstants.O},
				        {GameConstants.X,GameConstants.UNSET,GameConstants.X},
				        {GameConstants.O,GameConstants.UNSET,GameConstants.UNSET}};
		GameState state = new GameState();
		state.setBoard(tab);
		state.setNextPlayer(GameConstants.O);
		AIMinMax.Node n = ai.find(state);
		if (n != null)
			System.out.println("Achei! \n" + n.getState() + "\nV="+n.getValue());
		System.out.println(n.childrenToString());
	}
	
	
}
