package imageOperations;

import math.Matrix;
import threeDimensions.Texture;

public class KernelOperations {
	
	//TODO: stride
//	public Matrix convolve(Matrix m, Matrix kernel, int stride) {
//		if(!kernel.isSquare() || kernel.getRows() % 2 != 0 || kernel.getRows() > 2) throw new IllegalArgumentException("Kernel must be square and odd with a size greater than or equal to 3");
//		//Odd Image Matrix issues
//		int pad = kernel.getRows() / 2;
//		Matrix r = new Matrix(m.getRows() - 2*pad, m.getCols() - 2*pad);
//		float[] data = r.getData();
//		
//		//int oPos = 0;
//		//int nPos = 0;
//		
//		
//		for (int j = pad; j < m.getRows() - pad; j++, pos += m.getCols()) {
//			for (int i = pad; i < m.getCols() - pad; i++, pos++) {
//				
//				 data[pos] = 0;
//				 
//				for (int b = 0; b < kernel.getRows(); b++) {
//					for (int a = 0; a < kernel.getCols(); b++) {
//						data[pos] += 
//					}
//				}
//				
//			}
//		}
//		
//		return r;
//	}
	
	public Matrix convolve(Matrix m, Matrix kernel) {
		Matrix r = new Matrix(m.getRows(), m.getCols());
		float[] m_data = m.getData();
		float[] data = r.getData();
		float[] kernelData = kernel.getData();
		
		int pos = 0;
		for (int j = 0; j < m.getRows(); j++, pos += m.getCols()) {
			for (int i = 0; i < m.getCols(); i++, pos++) {
				data[pos] = 0;
				
				int kernelPos = 0;
				for (int b = -kernel.getRows()/2; b <= kernel.getRows()/2; b++, kernelPos += kernel.getCols()) {
					for (int a = -kernel.getCols(); a <= kernel.getCols()/2; a++, kernelPos++) {
						data[pos] += kernelData[kernelPos] * m_data[pos + b*m.getCols() + a];
					}
				}
				
			}
		}
	}

	public KernelOperations() {
	}

	public static void main(String[] args) {
		Texture tImg = Texture.loadImage("/Users/infinity/Desktop/SauronEye.png");
		Matrix img = Texture.toMatrix(tImg);
		
	}

}
