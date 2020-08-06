package threeDimensions;

import java.util.ArrayList;

import math.Maths;
import math.Matrix;
import math.Vec4;
import utils.BinaryFloatOperator;

public class Primitive {
	
	private ArrayList<Float> positions = new ArrayList<Float>();
	private ArrayList<Integer> indices = new ArrayList<Integer>();
	
	private Matrix translation = Matrix.Identity(4);
	private Matrix rotation = Matrix.Identity(4) ;
	private Matrix scale = Matrix.Identity(4);
	
	private int index = 0;
	
	public Primitive rotate(float x, float y, float z) {
		this.rotation =  Matrix.rotationZ(4, z).multiply(Matrix.rotationY(4, y)).multiply(Matrix.rotationX(4, x));
		return this;
	}
	
	public Primitive translate(float x, float y, float z) {
		this.translation = Matrix.Translation(4, x, y, z);
		return this;
	}
	
	public Primitive scale(float x, float y, float z) {
		this.scale = Matrix.Scale(4, x, y, z);
		return this;
	}
	
	private void transform(ArrayList<Float> positions, Matrix transform, int startIndex, int endIndex) {
			for (int i = startIndex; i < endIndex; i += 3) {
				Vec4 position =  new Vec4(positions.get(i), positions.get(i + 1), positions.get(i + 2), 1);
				transform.multiply(position);
				positions.set(i, position.x); positions.set(i + 1, position.y); positions.set(i + 2, position.z);
		}
	}
	
	//Disgusting
	public Primitive extrude(int stacks, int slices, BinaryFloatOperator radius) {
		int startIndex = this.index;
		float h = -0.5f, dh = 1f/stacks;
		for (int j = 0; j < stacks; j++, h += dh) {
			float theta = 0, dTheta = Maths.PI2/slices;
			for (int i = 0; i < slices; i++, theta += dTheta) {
				
				float r0 = radius.get(theta, h); //Bottom Left
				float r1 = radius.get(theta, h + dh); //Top Left
				float r2 = radius.get(theta + dTheta, h + dh); //Top Right
				float r3 = radius.get(theta + dTheta, h); //Bottom Right
				
				float s0 = (float) Math.sin(theta), 
						s1 = (float) Math.sin(theta + dTheta), 
						c0 = (float) Math.cos(theta),
						c1 = (float) Math.cos(theta + dTheta);
				
				if(j == 0) {
					positions.add(r3*c1); positions.add(-0.5f); positions.add(r3*s1);
					positions.add(0f); positions.add(-0.5f); positions.add(0f);
					positions.add(r0*c0); positions.add(-0.5f); positions.add(r0*s0);
					indices.add(index++); indices.add(index++); indices.add(index++);
				}
				
				positions.add(r0*c0); positions.add(h); positions.add(r0*s0);
				positions.add(r1*c0); positions.add(h+dh); positions.add(r1*s0);
				positions.add(r2*c1); positions.add(h+dh); positions.add(r2*s1);
				positions.add(r3*c1); positions.add(h); positions.add(r3*s1);
				indices.add(index); indices.add(index + 1); indices.add(index + 3);
				indices.add(index + 1); indices.add(index + 2); indices.add(index + 3);
				index += 4;
				
				if(j == stacks - 1) {
					positions.add(r2*c1); positions.add(0.5f); positions.add(r2*s1);
					positions.add(0f); positions.add(0.5f); positions.add(0f);
					positions.add(r1*c0); positions.add(0.5f); positions.add(r1*s0);
					indices.add(index++); indices.add(index++); indices.add(index++);
				}
				
			}
		}
		
		Matrix transform = this.translation.multiply(this.rotation).multiply(this.scale);
		this.transform(this.positions, transform, startIndex*3, this.index*3);
		
		return this;
	}
	
	//disgusting
	public Primitive quadCorners(float x0, float y0, float x1, float y1, float x2, float y2, float x3, float y3, float h) {
		int startIndex = this.index;
		float half = h/2;
		
		//Front
		this.positions.add(x0); this.positions.add(y0); this.positions.add(-half);
		this.positions.add(x1); this.positions.add(y1); this.positions.add(-half);
		this.positions.add(x2); this.positions.add(y2); this.positions.add(-half);
		this.positions.add(x3); this.positions.add(y3); this.positions.add(-half);
		this.indices.add(index); this.indices.add(index + 1); this.indices.add(index + 3);
		this.indices.add(index + 1); this.indices.add(index + 2); this.indices.add(index + 3);
		this.index += 4;
		
		
		//Left
		this.positions.add(x1); this.positions.add(y1); this.positions.add(half);
		this.positions.add(x1); this.positions.add(y1); this.positions.add(-half);
		this.positions.add(x0); this.positions.add(y0); this.positions.add(-half);
		this.positions.add(x0); this.positions.add(y0); this.positions.add(half);
		this.indices.add(index); this.indices.add(index + 1); this.indices.add(index + 3);
		this.indices.add(index + 1); this.indices.add(index + 2); this.indices.add(index + 3);
		this.index += 4;
		
		//Right
		this.positions.add(x3); this.positions.add(y3); this.positions.add(half);
		this.positions.add(x3); this.positions.add(y3); this.positions.add(-half);
		this.positions.add(x2); this.positions.add(y2); this.positions.add(-half);
		this.positions.add(x2); this.positions.add(y2); this.positions.add(half);
		this.indices.add(index); this.indices.add(index + 1); this.indices.add(index + 3);
		this.indices.add(index + 1); this.indices.add(index + 2); this.indices.add(index + 3);
		this.index += 4;
	
		//Top
		this.positions.add(x1); this.positions.add(y1); this.positions.add(-half);
		this.positions.add(x1); this.positions.add(y1); this.positions.add(half);
		this.positions.add(x2); this.positions.add(y2); this.positions.add(half);
		this.positions.add(x2); this.positions.add(y2); this.positions.add(-half);
		this.indices.add(index); this.indices.add(index + 1); this.indices.add(index + 3);
		this.indices.add(index + 1); this.indices.add(index + 2); this.indices.add(index + 3);
		this.index += 4;
		
		//Bottom
		this.positions.add(x0); this.positions.add(y0); this.positions.add(half);
		this.positions.add(x0); this.positions.add(y0); this.positions.add(-half);
		this.positions.add(x3); this.positions.add(y3); this.positions.add(-half);
		this.positions.add(x3); this.positions.add(y3); this.positions.add(half);
		this.indices.add(index); this.indices.add(index + 1); this.indices.add(index + 3);
		this.indices.add(index + 1); this.indices.add(index + 2); this.indices.add(index + 3);
		this.index += 4;
		
		//Bottom
		this.positions.add(x3); this.positions.add(y3); this.positions.add(half);
		this.positions.add(x2); this.positions.add(y2); this.positions.add(half);
		this.positions.add(x1); this.positions.add(y1); this.positions.add(half);
		this.positions.add(x0); this.positions.add(y0); this.positions.add(half);
		this.indices.add(index); this.indices.add(index + 1); this.indices.add(index + 3);
		this.indices.add(index + 1); this.indices.add(index + 2); this.indices.add(index + 3);
		this.index += 4;
		
		Matrix transform = this.translation.multiply(this.rotation).multiply(this.scale);
		this.transform(this.positions, transform, startIndex*3, this.index*3);
		
		return this;
	}

	public Mesh resolve() {
		float[] pos = new float[positions.size()];
		int[] ind = new int[indices.size()];
		for (int i = 0; i < pos.length; i++) {
			pos[i] = positions.get(i);
		}
		for (int i = 0; i < ind.length; i++) {
			ind[i] = indices.get(i);
		}
		
		return new Mesh(ind, new int[] {3}, pos);
	}
	
}
