package gl;

import org.lwjgl.opengl.GL15;

import loaders.HandlerObject;

public class VertexBuffer extends HandlerObject {
	
	public VertexBuffer(int vertexBufferID) {
		super(vertexBufferID);
	}
	
	@Override
	public void dispose() {
		GL15.glDeleteBuffers(super.getID());
	}
}
