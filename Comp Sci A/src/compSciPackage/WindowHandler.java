package compSciPackage;

import javax.swing.JFrame;

public class WindowHandler {
	
	public static void setWindowProperties(JFrame frame, int width, int height, boolean visible, String title, int closeOperation, boolean center) {
		frame.setSize(width, height);
		frame.setTitle(title);
		frame.setVisible(visible);
		frame.setDefaultCloseOperation(closeOperation);
		if(center) {
			frame.setLocationRelativeTo(null);
		}
	}
	
	public static void main(String[] args) {
		JFrame window = new JFrame();
		setWindowProperties(window, 1000, 800, true, "The Soviet Union", JFrame.EXIT_ON_CLOSE, true);
		//FlagDisplay flagDisplay  = new FlagDisplay(600, 300);
		//MatrixTesting matrixTesting = new MatrixTesting(45, 0, 0);
		CarDisplay carDisplay = new CarDisplay(4);
		window.add(carDisplay);
		window.setVisible(true);
		
		Thread t = new Thread(carDisplay);
		t.start();
	}

}
