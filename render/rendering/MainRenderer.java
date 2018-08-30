package rendering;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;

import engine.Const;
import entities.Camera;
import environment.Water;
import gl.FrameBuffer;
import maths.Mat4f;
import maths.Vec4f;
import universe.Universe;

public class MainRenderer {
	
	private static final Vec4f REFRACT_CLIP = new Vec4f(0.0f, -1.0f, 0.0f, Const.WATER_LEVEL + 0.15f);
	private static final Vec4f REFLECT_CLIP = new Vec4f(0.0f, 1.0f, 0.0f, -Const.WATER_LEVEL);
	
	private static final float ASPECT_RATIO = ((float) Const.WINDOW_WIDTH) / ((float) Const.WINDOW_HEIGHT);
	
	private static Mat4f calculateProjectionMatrix() {
		float scale = (float) ((1.0f / Math.tan(Const.RENDER_FOV)));
		float frustum_length = Const.RENDER_NEAR_PLANE - Const.RENDER_FAR_PLANE;
		return new Mat4f().setMatrix(
				new Vec4f(scale / ASPECT_RATIO, 0.0f, 0.0f, 0.0f),
				new Vec4f(0.0f, scale, 0.0f, 0.0f),
				new Vec4f(0.0f, 0.0f, (Const.RENDER_FAR_PLANE + Const.RENDER_NEAR_PLANE) / frustum_length, -1.0f),
				new Vec4f(0.0f, 0.0f, 2.0f * Const.RENDER_NEAR_PLANE * Const.RENDER_FAR_PLANE / frustum_length, 0.0f)
			);
	}
	
	private TerrainRenderer terrainRenderer;
	private PlayerRenderer playerRenderer;
	private WaterRenderer waterRenderer;
	private SkyRenderer skyRenderer;

	public MainRenderer(Universe universe) {
		Mat4f projectionMatrix = calculateProjectionMatrix();
		
		terrainRenderer = new TerrainRenderer(projectionMatrix, universe.getEnvironment().getSun().getUniformBuffer());
		playerRenderer = new PlayerRenderer(projectionMatrix);
		waterRenderer = new WaterRenderer(universe, projectionMatrix);
		skyRenderer = new SkyRenderer(projectionMatrix);
		
		GL11.glClearColor(Const.RENDER_COLOR_R, Const.RENDER_COLOR_G, Const.RENDER_COLOR_B, Const.RENDER_COLOR_A);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glDepthFunc(GL11.GL_LEQUAL);
		GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glCullFace(GL11.GL_BACK);
	}
	
	public void dispose() {
		terrainRenderer.dispose();
		playerRenderer.dispose();
		waterRenderer.dispose();
		skyRenderer.dispose();
	}
	
	private void flushBuffers() {
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
	}
	
	private void enableClipping() {
		GL11.glEnable(GL30.GL_CLIP_DISTANCE0);
	}
	
	private void disableClipping() {
		GL11.glDisable(GL30.GL_CLIP_DISTANCE0);
	}
	
	private void enableBlending() {
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
	}
	
	private void disableBlending() {
		GL11.glDisable(GL11.GL_BLEND);
	}
	
	public void render(Universe universe) {
		Camera camera = universe.getPlayer().getCamera();
		Water water = universe.getEnvironment().getWater();
		
		enableClipping();
		
		water.bindRefractionBuffer();
		flushBuffers();
		enableBlending();
		playerRenderer.render(universe, camera.getViewFullMatrix(), REFRACT_CLIP);
		terrainRenderer.renderLowQuality(universe, camera.getViewFullMatrix(), REFRACT_CLIP);
		disableBlending();
		
		water.bindReflectionBuffer();
		flushBuffers();
		skyRenderer.render(universe, camera.getReflectSkyMatrix());
		enableBlending();
		terrainRenderer.renderLowQuality(universe, camera.getReflectFullMatrix(), REFLECT_CLIP);
		playerRenderer.render(universe, camera.getReflectFullMatrix(), REFLECT_CLIP);
		disableBlending();
		
		disableClipping();
		
		FrameBuffer.unbind();
		flushBuffers();
		skyRenderer.render(universe, camera.getViewSkyMatrix());
		enableBlending();
		terrainRenderer.renderHighQuality(universe, camera.getViewFullMatrix(), null);
		playerRenderer.render(universe, camera.getViewFullMatrix(), null);
		waterRenderer.render(universe, camera.getViewFullMatrix());
		disableBlending();
	}
}
