package neuralNetwork;

public class NeuralMatrix {
	private int rows, cols;
	private double[][] data;
	
	public NeuralMatrix(int rows, int cols) {
		this.rows = rows;
		this.cols = cols;
		this.data = new double[rows][cols];
	}
	
	public NeuralMatrix(double[][] data) {
		this.rows = data.length;
		this.cols = data[0].length;
		this.data = new double[this.rows][this.cols];
		for (int i = 0; i < this.rows; i++) {
			for (int j = 0; j < this.cols; j++) {
				this.data[i][j] = data[i][j];
			}
		}
	}
	
	//Copy Constructor
	public NeuralMatrix(NeuralMatrix nm) {
		this(nm.data);
	}
	
	//Returns a Vector, Example: [5, 4, 3, 2, 1] -> [[5], [4], [3], [2], [1]]
	public static NeuralMatrix vector(double[] array) {
		NeuralMatrix r = new NeuralMatrix(array.length, 1);
		for (int i = 0; i < r.data.length; i++) {
			r.data[i][0] = array[i];
		}
		return r;
	}
	
	//Input must be a vector, Example: [[5], [4], [3], [2], [1]] -> [5, 4, 3, 2, 1]
	public static double[] fromVector(NeuralMatrix m) {
		if(m.cols != 1) throw new RuntimeException("Input is not a Vector");
		double[] r = new double[m.rows];
		for (int i = 0; i < r.length; i++) {
			r[i] = m.data[i][0];
		}
		return r;
	}

	//Element-wise addition
	public void add(NeuralMatrix m) {
		if(this.rows != m.rows || this.cols != m.cols) {
			throw new RuntimeException("Columns and Rows do not match.");
		}
		for (int i = 0; i < this.rows; i++) {
			for (int j = 0; j < this.cols; j++) {
				this.data[i][j] += m.data[i][j];
			}
		}
	}
	
	//Scalar addition
	public void add(double d) {
		for (int i = 0; i < this.rows; i++) {
			for (int j = 0; j < this.cols; j++) {
				this.data[i][j] += d;
			}
		}
	}
	
	//Element-wise subtraction
	public void subtract(NeuralMatrix m) {
		if(this.rows != m.rows || this.cols != m.cols) {
			throw new RuntimeException("Columns and Rows do not match.");
		}
		for (int i = 0; i < this.rows; i++) {
			for (int j = 0; j < this.cols; j++) {
				this.data[i][j] -= m.data[i][j];
			}
		}
	}
	
	//Scalar Subtraction
	public void subtract(double d) {
		for (int i = 0; i < this.rows; i++) {
			for (int j = 0; j < this.cols; j++) {
				this.data[i][j] -= d;
			}
		}
	}
	
	//Randomize for Initialization
	public void randomize() {
		for (int i = 0; i < this.rows; i++) {
			for (int j = 0; j < this.cols; j++) {
				this.data[i][j] = Math.random() * 2 - 1; //TODO: Experiment with different randomizations
			}
		}
	}
	
	//Flip along diagonal
	public static NeuralMatrix transpose(NeuralMatrix m) {
		NeuralMatrix t = new NeuralMatrix(m.cols, m.rows);
		for (int i = 0; i < m.rows; i++) {
			for (int j = 0; j < m.cols; j++) {
				t.data[j][i] = m.data[i][j];
			}
		}
		return t;
	}
	
	//Element-wise multiplication
	public void multiply(NeuralMatrix m) {
		if(this.rows != m.rows || this.cols != m.cols) {
			throw new RuntimeException("Columns and Rows do not match.");
		}
		for (int i = 0; i < this.rows; i++) {
			for (int j = 0; j < this.cols; j++) {
				this.data[i][j] *= m.data[i][j];
			}
		}
	}
	
	//Scalar multiplication
	public void multiply(double d) {
		for (int i = 0; i < this.rows; i++) {
			for (int j = 0; j < this.cols; j++) {
				this.data[i][j] *= d;
			}
		}
	}
	
	//Matrix Multiplication
	public static NeuralMatrix multiply(NeuralMatrix m1, NeuralMatrix m2) {
		if(m1.cols != m2.rows) {
			throw new RuntimeException("Columns and Rows no not match");
		}
		NeuralMatrix r = new NeuralMatrix(m1.rows, m2.cols);
		for (int i = 0; i < r.rows; i++) {
			for (int j = 0; j < r.cols; j++) {
				for (int k = 0; k < m1.cols; k++) {
					r.data[i][j] += (m1.data[i][k] * m2.data[k][j]);
				}
			}
		}
		return r;
	}
	
	public void map(ActivationFunction a) {
		for (int i = 0; i < this.rows; i++) {
			for (int j = 0; j < this.cols; j++) {
				this.data[i][j] = a.func(this.data[i][j]);
			}
		}
	}
	
	public void map(MutateFunction m) {
		for (int i = 0; i < this.rows; i++) {
			for (int j = 0; j < this.cols; j++) {
				this.data[i][j] = m.func(this.data[i][j]);
			}
		}
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
