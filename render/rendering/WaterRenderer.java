package rendering;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import maths.Mat4f;
import shaders.Shader;
import shaders.WaterShader;
import terrain.WaterTile;
import universe.Universe;

public class WaterRenderer {

	private WaterShader shader;
	
	public WaterRenderer(Universe universe, Mat4f projectionMatrix) {
		shader = new WaterShader();
		shader.start();
		shader.loadProjectionMatrix(projectionMatrix);
		shader.loadTexturesAndConstants();
		shader.loadSunUniform(universe.getEnvironment().getSun().getUniformBuffer());
		Shader.stop();
	}
	
	public void render(Universe universe, Mat4f viewMatrix) {
		shader.start();
		shader.loadViewMatrix(viewMatrix);
		shader.loadCameraPosition(universe.getPlayer().getCamera().getPosition());
		shader.loadRippleOffset(universe.getEnvironment().getWater().getRippleOffset());
		
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, universe.getEnvironment().getWater().getDuDvMapTexture().getID());
		GL13.glActiveTexture(GL13.GL_TEXTURE1);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, universe.getEnvironment().getWater().getNormalMapTexture().getID());
		GL13.glActiveTexture(GL13.GL_TEXTURE2);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, universe.getEnvironment().getWater().getRefractionTexture());
		GL13.glActiveTexture(GL13.GL_TEXTURE3);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, universe.getEnvironment().getWater().getReflectionTexture());
		GL13.glActiveTexture(GL13.GL_TEXTURE4);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, universe.getEnvironment().getWater().getRefractionDepthTexture());
		
		for (WaterTile tile:universe.getWorld().getCurrentWaterTile()) {
			GL30.glBindVertexArray(tile.getModel().getVertexArrays()[0].getID());
			GL20.glEnableVertexAttribArray(0);
			GL20.glEnableVertexAttribArray(1);
			GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, tile.getModel().getVertexCounts()[0]);
			GL20.glDisableVertexAttribArray(1);
			GL20.glDisableVertexAttribArray(0);
			GL30.glBindVertexArray(0);
		}
		
		Shader.stop();
	}
	
	public void dispose() {
		shader.dispose();
	}
}
