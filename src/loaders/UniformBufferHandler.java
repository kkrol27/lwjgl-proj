package loaders;

import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL31;

import gl.UniformBuffer;

public class UniformBufferHandler extends Handler {
	
	private int nextBindingPoint;

	public UniformBufferHandler() {
		super();
		this.nextBindingPoint = 0;
	}
	
	public UniformBuffer generateUniformBuffer(int size) {
		int uniformBufferID = GL15.glGenBuffers();
		GL15.glBindBuffer(GL31.GL_UNIFORM_BUFFER, uniformBufferID);
		GL15.glBufferData(GL31.GL_UNIFORM_BUFFER, size, GL15.GL_DYNAMIC_DRAW);
		GL15.glBindBuffer(GL31.GL_UNIFORM_BUFFER, 0);
		GL30.glBindBufferRange(GL31.GL_UNIFORM_BUFFER, 0, uniformBufferID, 0, size);
		
		UniformBuffer uniformBuffer = new UniformBuffer(uniformBufferID, nextBindingPoint++);
		super.addObject(uniformBufferID, uniformBuffer);
		return uniformBuffer;
	}
}
