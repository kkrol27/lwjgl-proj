package environment;

import engine.Const;
import gl.FrameBuffer;
import gl.Texture;
import loaders.Loader;
import maths.Vec2f;

public class Water {

	private Vec2f rippleOffset;
	private FrameBuffer reflection;
	private FrameBuffer refraction;
	private Texture normalMap;
	private Texture dudvMap;
	
	public Water(Loader loader) {
		this.rippleOffset = new Vec2f();
		reflection = new FrameBuffer(Const.WATER_REFLECTION_BUFFER_WIDTH, Const.WATER_REFLECTION_BUFFER_HEIGHT).asColorAttachment().withDepthBuffer();
		refraction = new FrameBuffer(Const.WATER_REFRACTION_BUFFER_WIDTH, Const.WATER_REFRACTION_BUFFER_HEIGHT).asColorAttachment().withDepthTexture();
		normalMap = loader.txts().load2DTexture("waternormals.png", false, 0.0f);
		dudvMap = loader.txts().load2DTexture("waterdudv.png", false, 0.0f);
	}
	
	public void primaryUpdate(float dt) {
		float displacement = Const.WATER_RIPPLE_SPEED * dt;
		rippleOffset.x += displacement;
		rippleOffset.y += displacement;
		rippleOffset.x %= 1.0;
		rippleOffset.y %= 1.0f;
	}
	
	public void bindReflectionBuffer() {
		reflection.bind();
	}
	
	public void bindRefractionBuffer() {
		refraction.bind();
	}
	
	public int getReflectionTexture() {
		return reflection.getColorBufferTexture();
	}
	
	public int getRefractionTexture() {
		return refraction.getColorBufferTexture();
	}
	
	public int getRefractionDepthTexture() {
		return refraction.getDepthBufferTexture();
	}
	
	public Vec2f getRippleOffset() {
		return rippleOffset;
	}
	
	public Texture getNormalMapTexture() {
		return normalMap;
	}
	
	public Texture getDuDvMapTexture() {
		return dudvMap;
	}
	
	public void dispose() {
		reflection.dispose();
		refraction.dispose();
	}
}
