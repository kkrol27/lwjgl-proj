package loaders;

import org.lwjgl.opengl.GL30;

import gl.VertexArray;

public class VertexArrayHandler extends Handler {

	public VertexArrayHandler() {
		super();
	}
	
	public VertexArray generateVertexArray() {
		int vertexArrayID = GL30.glGenVertexArrays();
		GL30.glBindVertexArray(vertexArrayID);
		
		VertexArray vertexArray = new VertexArray(vertexArrayID);
		super.addObject(vertexArrayID, vertexArray);
		return vertexArray;
	}
	
	public void unbindVertexArray() {
		GL30.glBindVertexArray(0);
	}
}
