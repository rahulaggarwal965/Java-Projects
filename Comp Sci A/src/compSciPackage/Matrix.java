package compSciPackage;

public class Matrix {
	private int rows;
	private int cols;
	private double[][] data;
			
	public Matrix(int rows, int cols) {
		this.rows = rows;
		this.cols = cols;
		this.data = new double[rows][cols];
	}
	
	public Matrix(double[][] data) {
		this.rows = data.length;
		this.cols = data[0].length;
		this.data = new double[this.rows][this.cols];
		for (int i = 0; i < this.rows; i++) {
			for (int j = 0; j < this.cols; j++) {
				this.data[i][j] = data[i][j];
			}
		}
	}
	
	public int[] getRow(int i) {
		int[] row = new int[this.cols];
		for(int j = 0; j < row.length; j++) {
			row[j] = (int) this.data[i][j];
		}
		return row;
	}
	
	public Matrix(Matrix m) {
		this(m.data);
	}
	
	public Matrix transpose() {
		Matrix t = new Matrix(this.cols, this.rows);
		for (int i = 0; i < this.rows; i++) {
			for (int j = 0; j < this.cols; j++) {
				t.data[j][i] = this.data[i][j];
			}
		}
		return t;
	}
	
	public Matrix scalarAdd(double d) {
		Matrix r = new Matrix(this.rows, this.cols);
		for (int i = 0; i < this.rows; i++) {
			for (int j = 0; j < this.cols; j++) {
				r.data[i][j] = this.data[i][j] + d;
			}
		}
		return r;
	}
	
	public Matrix add(Matrix m) {
		if(this.rows != m.rows || this.cols != m.cols) {
			throw new RuntimeException("Columns and Rows do not match.");
		}
		Matrix r = new Matrix(this.rows, this.cols);
		for (int i = 0; i < this.rows; i++) {
			for (int j = 0; j < this.cols; j++) {
				r.data[i][j] = this.data[i][j] + m.data[i][j];
			}
		}
		return r;
	}
	
	public Matrix scalarSubtract(double d) {
		Matrix r = new Matrix(this.rows, this.cols);
		for (int i = 0; i < this.rows; i++) {
			for (int j = 0; j < this.cols; j++) {
				r.data[i][j] = this.data[i][j] - d;
			}
		}
		return r;
	}
	
	public Matrix subtract(Matrix m) {
		if(this.rows != m.rows || this.cols != m.cols) {
			throw new RuntimeException("Columns and Rows do not match.");
		}
		Matrix r = new Matrix(this.rows, this.cols);
		for (int i = 0; i < this.rows; i++) {
			for (int j = 0; j < this.cols; j++) {
				r.data[i][j] = this.data[i][j] - m.data[i][j];
			}
		}
		return r;
	}
	
	public Matrix scalarMultiply(double d) {
		Matrix r = new Matrix(this.rows, this.cols);
		for (int i = 0; i < this.rows; i++) {
			for (int j = 0; j < this.cols; j++) {
				r.data[i][j] = this.data[i][j] * d;
			}
		}
		return r;
	}
	
	public Matrix multiply(Matrix m) {
		if(this.cols != m.rows) {
			throw new RuntimeException("Columns and Rows no not match");
		}
		Matrix r = new Matrix(this.rows, m.cols);
		for (int i = 0; i < r.rows; i++) {
			for (int j = 0; j < r.cols; j++) {
				for (int k = 0; k < this.cols; k++) {
					r.data[i][j] += (this.data[i][k] * m.data[k][j]);
				}
			}
		}
		return r;
	}
	
	public void print() {
		for (int i = 0; i < this.rows; i++) {
			for (int j = 0; j < this.cols; j++) {
				System.out.printf("%9.4f ", this.data[i][j]);
			}
			System.out.println();
		}
	}
}
