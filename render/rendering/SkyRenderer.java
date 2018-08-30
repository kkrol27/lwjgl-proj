package rendering;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import maths.Mat3f;
import maths.Mat4f;
import shaders.Shader;
import shaders.SkyShader;
import universe.Universe;

public class SkyRenderer {
	
	private SkyShader shader;
	
	public SkyRenderer(Mat4f projectionMatrix) {
		shader = new SkyShader();
		shader.start();
		shader.loadProjectionMatrix(projectionMatrix);
		Shader.stop();
	}
	
	public void dispose() {
		shader.dispose();
	}
	
	public void render(Universe universe, Mat3f viewMatrix) {
		shader.start();
		shader.loadViewSkyMatrix(viewMatrix);
		
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL13.GL_TEXTURE_CUBE_MAP, universe.getEnvironment().getSky().getDayCubeMap().getID());
		GL30.glBindVertexArray(universe.getEnvironment().getSky().getCubeModel().getVertexArrays()[0].getID());
		GL20.glEnableVertexAttribArray(0);
		GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, universe.getEnvironment().getSky().getCubeModel().getVertexCounts()[0]);
		GL20.glDisableVertexAttribArray(0);
		GL30.glBindVertexArray(0);
		Shader.stop();
	}
}
