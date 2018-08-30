package gl;

import org.lwjgl.opengl.GL30;

import loaders.HandlerObject;

public class VertexArray extends HandlerObject {

	public VertexArray(int vertexArrayID) {
		super(vertexArrayID);
	}
	
	@Override
	public void dispose() {
		GL30.glDeleteVertexArrays(super.getID());
	}
}
