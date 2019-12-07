package pong;

import gameEngine.GameEngine;
import gameEngine.IGameLogic;

public class Main {
	public static void main(String[] args) {
		try {
			IGameLogic gameLogic = new RefactoredPong();
			GameEngine gameEngine = new GameEngine(60, 60, 3, gameLogic, 800, 800, "Refactored Pong", true);
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(-1);
		}
	}
}
