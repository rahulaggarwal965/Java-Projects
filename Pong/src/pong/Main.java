package pong;

import gameEngine.GameEngine;
import gameEngine.GameLogic;

public class Main {
	public static void main(String[] args) {
		try {
			GameLogic gameLogic = new Pong();
			GameEngine gameEngine = new GameEngine(60, 60, 3, gameLogic, 800, 800, "Refactored Pong");
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(-1);
		}
	}
}
