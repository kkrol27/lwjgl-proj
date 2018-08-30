package rendering;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import gl.UniformBuffer;
import maths.Mat4f;
import maths.Vec3f;
import maths.Vec4f;
import shaders.Shader;
import shaders.TerrainHqShader;
import shaders.TerrainLqShader;
import terrain.Terrain;
import terrain.TerrainTexturePack;
import universe.Universe;

public class TerrainRenderer {

	private TerrainHqShader shader_hq;
	private TerrainLqShader shader_lq;
	
	public TerrainRenderer(Mat4f projectionMatrix, UniformBuffer sun) {
		shader_hq = new TerrainHqShader();
		shader_hq.start();
		shader_hq.loadProjectionMatrix(projectionMatrix);
		shader_hq.loadTexturesAndConstants();
		
		shader_hq.loadSunUniform(sun);
		
		Shader.stop();
		shader_lq = new TerrainLqShader();
		shader_lq.start();
		shader_lq.loadProjectionMatrix(projectionMatrix);
		shader_lq.loadTexturesAndConstants();
		Shader.stop();
	}
	
	public void dispose() {
		shader_hq.dispose();
		shader_lq.dispose();
	}
	
	private void bindLowQualityTextures(TerrainTexturePack textures) {
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, textures.getTexture_().getID());
		GL13.glActiveTexture(GL13.GL_TEXTURE1);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, textures.getTextureR().getID());
		GL13.glActiveTexture(GL13.GL_TEXTURE2);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, textures.getTextureG().getID());
		GL13.glActiveTexture(GL13.GL_TEXTURE3);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, textures.getTextureB().getID());
	}
	
	private void bindHighQualityTextures(TerrainTexturePack textures) {
		bindLowQualityTextures(textures);
		GL13.glActiveTexture(GL13.GL_TEXTURE4);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, textures.getNormals_().getID());
		GL13.glActiveTexture(GL13.GL_TEXTURE5);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, textures.getNormalsR().getID());
		GL13.glActiveTexture(GL13.GL_TEXTURE6);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, textures.getNormalsG().getID());
		GL13.glActiveTexture(GL13.GL_TEXTURE7);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, textures.getNormalsB().getID());
	}
	
	private void bindLowQualityBufferData(int vertexArrayID) {
		GL30.glBindVertexArray(vertexArrayID);
		GL20.glEnableVertexAttribArray(0);
		GL20.glEnableVertexAttribArray(1);
		GL20.glEnableVertexAttribArray(2);
		GL20.glEnableVertexAttribArray(3);
		GL20.glEnableVertexAttribArray(4);
	}
	
	private void bindHighQualityBufferData(int vertexArrayID) {
		bindLowQualityBufferData(vertexArrayID);
		GL20.glEnableVertexAttribArray(5);
	}
	
	private void drawTerrain(Terrain terrain) {
		GL11.glDrawElements(GL11.GL_TRIANGLES, terrain.getModel().getVertexCounts()[0], GL11.GL_UNSIGNED_INT, 0);
	}
	
	private void disableLowQualityBufferDate() {
		GL20.glDisableVertexAttribArray(4);
    	GL20.glDisableVertexAttribArray(3);
        GL20.glDisableVertexAttribArray(2);
		GL20.glDisableVertexAttribArray(1);
		GL20.glDisableVertexAttribArray(0);
		GL30.glBindVertexArray(0);
	}
	
	private void disableHighQualityBufferData() {
		GL20.glDisableVertexAttribArray(5);
		disableLowQualityBufferDate();
	}
	
	private void renderHighQuality(Terrain[] terrains, TerrainTexturePack textures, Mat4f viewMatrix, Vec4f clipPlane, Vec3f sunDirection) {
		shader_hq.start();
		shader_hq.loadViewMatrix(viewMatrix);
		shader_hq.loadSunDirection(sunDirection);
		if(clipPlane != null) { shader_hq.loadClipPlane(clipPlane); }
		bindHighQualityTextures(textures);
		for(Terrain terrain:terrains) {
			bindHighQualityBufferData(terrain.getModel().getVertexArrays()[0].getID());
			drawTerrain(terrain);
	        disableHighQualityBufferData();
		}
		Shader.stop();
	}
	
	private void renderLowQuality(Terrain[] terrains, TerrainTexturePack textures, Mat4f viewMatrix, Vec4f clipPlane, Vec3f sunDirection) {
		shader_lq.start();
		shader_lq.loadViewMatrix(viewMatrix);
		
		if(clipPlane != null) { shader_lq.loadClipPlane(clipPlane); }
		bindLowQualityTextures(textures);
		for(Terrain terrain:terrains) {
			bindLowQualityBufferData(terrain.getModel().getVertexArrays()[0].getID());
			drawTerrain(terrain);
			disableLowQualityBufferDate();
		}
		Shader.stop();
	}
	
	public void renderHighQuality(Universe universe, Mat4f viewMatrix, Vec4f clipPlane) {
		renderHighQuality(universe.getWorld().getCurrentTerrain(), universe.getWorld().getTerrainTexturePack(), viewMatrix, clipPlane, universe.getEnvironment().getSky().getSunlightDirection());
	}
	
	public void renderLowQuality(Universe universe, Mat4f viewMatrix, Vec4f clipPlane) {
		renderLowQuality(universe.getWorld().getCurrentTerrain(), universe.getWorld().getTerrainTexturePack(), viewMatrix, clipPlane, universe.getEnvironment().getSky().getSunlightDirection());
	}
}
