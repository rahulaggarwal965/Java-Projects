package tests;

import gameEngine.GameEngine;
import gameEngine.IGameLogic;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			IGameLogic gameLogic = new Fractal();
			GameEngine gameEngine = new GameEngine(60, 60, 3, gameLogic, 800, 600, "Fractal", true);
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(-1);
		}
	}

}
