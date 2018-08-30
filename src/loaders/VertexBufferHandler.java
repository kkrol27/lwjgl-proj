package loaders;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;

import gl.VertexBuffer;
import utils.ToBuffer;

public class VertexBufferHandler extends Handler {
	
	public VertexBufferHandler() {
		super();
	}
	
	public VertexBuffer storeIndexArray(int[] indices) {
		int vertexBufferID = GL15.glGenBuffers();
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vertexBufferID);
		GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, ToBuffer.from(indices), GL15.GL_STATIC_DRAW);
		
		VertexBuffer vertexBuffer = new VertexBuffer(vertexBufferID);
		super.addObject(vertexBufferID, vertexBuffer);
		return vertexBuffer;
	}
	
	public VertexBuffer storeDataInAttributeList(int attrib, int dimensions, float[] data) {
		int vertexBufferID = GL15.glGenBuffers();
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vertexBufferID);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, ToBuffer.from(data), GL15.GL_STATIC_DRAW);
		GL20.glVertexAttribPointer(attrib, dimensions, GL11.GL_FLOAT, false, 0, 0);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
		
		VertexBuffer vertexBuffer = new VertexBuffer(vertexBufferID);
		super.addObject(vertexBufferID, vertexBuffer);
		return vertexBuffer;
	}
}
