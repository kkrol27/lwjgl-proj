package rendering;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import maths.Mat4f;
import maths.Vec4f;
import models.TexturedModel;
import shaders.PlayerShader;
import shaders.Shader;
import universe.Universe;

public class PlayerRenderer {
	
	private PlayerShader shader;

	public PlayerRenderer(Mat4f projectionMatrix) {
		shader = new PlayerShader();
		shader.start();
		shader.loadProjectionMatrix(projectionMatrix);
		shader.loadTexturesAndConstants();
		Shader.stop();
	}
	
	public void dispose() {
		shader.dispose();
	}
	
	public void render(Universe universe, Mat4f viewMatrix, Vec4f clipPlane) {
		shader.start();
		if(clipPlane != null) { shader.loadClipPlane(clipPlane); }
		shader.loadModelMatrix(universe.getPlayer().getModelMatrix());
		shader.loadViewMatrix(viewMatrix);
		
		for(int i = 0; i < ((TexturedModel) universe.getPlayer().getModel()).getModel().getVertexArrays().length; i++) {
			GL13.glActiveTexture(GL13.GL_TEXTURE0);
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, ((TexturedModel) universe.getPlayer().getModel()).getTextures()[i].getID());
			
			GL30.glBindVertexArray(((TexturedModel) universe.getPlayer().getModel()).getModel().getVertexArrays()[i].getID());
			GL20.glEnableVertexAttribArray(0);
			GL20.glEnableVertexAttribArray(1);
			GL20.glEnableVertexAttribArray(2);
			
			GL11.glDrawElements(GL11.GL_TRIANGLES, ((TexturedModel) universe.getPlayer().getModel()).getModel().getVertexCounts()[i], GL11.GL_UNSIGNED_INT, 0);
			
			GL20.glDisableVertexAttribArray(2);
			GL20.glDisableVertexAttribArray(1);
			GL20.glDisableVertexAttribArray(0);
			GL30.glBindVertexArray(0);
		}
		
		Shader.stop();
	}
}
