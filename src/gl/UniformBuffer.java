package gl;

import java.nio.FloatBuffer;

import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL31;

import loaders.HandlerObject;

public class UniformBuffer extends HandlerObject {
	
	private int bindingPoint;

	public UniformBuffer(int uniformBufferID, int bindingPoint) {
		super(uniformBufferID);
		this.bindingPoint = bindingPoint;
	}
	
	@Override
	public void dispose() {
		GL15.glDeleteBuffers(super.getID());
	}
	
	public int getBindingPoint() {
		return bindingPoint;
	}
	
	public void update(int offset, FloatBuffer data) {
		GL15.glBindBuffer(GL31.GL_UNIFORM_BUFFER, super.getID());
		GL15.glBufferSubData(GL31.GL_UNIFORM_BUFFER, offset, data);
		GL15.glBindBuffer(GL31.GL_UNIFORM_BUFFER, 0);
	}
}
