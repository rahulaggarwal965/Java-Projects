package components;

import java.util.ArrayList;

import math.Maths;
import threeDimensions.Mesh;
import utils.BinaryFloatOperator;

public final class Primitive {
	
	//Disgusting
	public static Mesh extrude(int stacks, int slices, BinaryFloatOperator radius) {
		ArrayList<Float> positions = new ArrayList<Float>();
		ArrayList<Integer> indices = new ArrayList<Integer>();
		int index = 0;
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
					positions.add(r1*c0); positions.add(0.5f); positions.add(r1*s0);
					positions.add(0f); positions.add(0.5f); positions.add(0f);
					positions.add(r2*c1); positions.add(0.5f); positions.add(r2*s1);
					indices.add(index++); indices.add(index++); indices.add(index++);
				}
				
			}
		}
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
