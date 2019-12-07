package compSciPackage;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;

public class MatrixTesting extends Canvas{
	
	private static final double[][] squarePoints = new double[][] {{100, 200, 1}, {200, 200, 1}, {200, 300, 1}, {100, 300, 1}};
	
	private Matrix points;
	private Matrix transform;
	
	MatrixTesting() {
		this.points = new Matrix(squarePoints).transpose();
	}
	
	MatrixTesting(double deg, int x, int y) {
		double rad = Math.toRadians(deg);
		double[][] transformArr = {
				{Math.cos(rad), Math.sin(rad), x},
				{-Math.sin(rad), Math.cos(rad), y},
				{0, 0, 1}
				};
		this.transform = new Matrix(transformArr);
		this.points = new Matrix(squarePoints).transpose();
		this.points = transform.multiply(points);
		this.points.print();
		
	}
	
	public void paint(Graphics win) {
		int[] xPoints = new int[4];
		int[] yPoints = new int[4];
		xPoints = this.points.getRow(0);
		yPoints = this.points.getRow(1);

		win.setColor(Color.red);
		win.fillPolygon(xPoints, yPoints, 4);
	}
}
