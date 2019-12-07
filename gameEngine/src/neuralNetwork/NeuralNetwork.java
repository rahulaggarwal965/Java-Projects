package neuralNetwork;

public class NeuralNetwork {
	
	private int nLayers;
	private NeuralMatrix[] weights;
	private NeuralMatrix[] biases;
	private ActivationFunction a;
	
	public NeuralNetwork(int[] shape, ActivationFunction a) {
		this.nLayers = shape.length - 1;
		this.weights = new NeuralMatrix[nLayers];
		this.biases = new NeuralMatrix[nLayers];
		for (int i = 0; i < nLayers; i++) {
			weights[i] = new NeuralMatrix(i+ 1, i);
			weights[i].randomize();
			biases[i] = new NeuralMatrix(i+1, 1); //Vector
			biases[i].randomize();
		}
		
		//Set Activation Function and Learning Rate (LR is for Gradient Descent)
		this.a = a;
	}
	
	//Copy Constructor
	public NeuralNetwork(NeuralNetwork n) {
		this.nLayers = n.nLayers;
		this.weights = new NeuralMatrix[nLayers];
		this.biases = new NeuralMatrix[nLayers];
		for (int i = 0; i < nLayers; i++) {
			this.weights[i] = new NeuralMatrix(n.weights[i]);
			this.biases[i] = new NeuralMatrix(n.biases[i]);
		}
		
		this.a = n.a;
	}
	
	public double[] predict(double[] inputs) {
		
		NeuralMatrix outputs = NeuralMatrix.vector(inputs);
		for (int i = 0; i < nLayers; i++) {
			outputs = NeuralMatrix.multiply(this.weights[i], outputs);
			outputs.add(this.biases[i]);
			outputs.map(a);
		}
		return NeuralMatrix.fromVector(outputs);
		
	}
	
	public void mutate(MutateFunction m) {
		for (int i = 0; i < nLayers; i++) {
			this.weights[i].map(m);
			this.biases[i].map(m);
		}
	}
}
