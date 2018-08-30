package models;

import gl.VertexArray;
import gl.VertexBuffer;
import loaders.Loader;

public class Model {
	
	private int[] vertexCount;
	private VertexArray[] vertexArray;
	private VertexBuffer[][] vertexBuffers;
	
	public Model(int[] vertexCount, VertexArray[] vertexArray, VertexBuffer[][] vertexBuffers) {
		this.vertexCount = vertexCount;
		this.vertexArray = vertexArray;
		this.vertexBuffers = vertexBuffers;
	}
	
	public void dispose(Loader loader) {
		for(int i = 0; i < vertexArray.length; i++) {
			for(VertexBuffer vertexBuffer:vertexBuffers[i]) {
				loader.vbos().disposeObject(vertexBuffer.getID());
			}
			loader.vaos().disposeObject(vertexArray[i].getID());
		}
	}

	public int[] getVertexCounts() {
		return vertexCount;
	}

	public VertexArray[] getVertexArrays() {
		return vertexArray;
	}
}
