package environment;

import engine.Const;
import gl.UniformBuffer;
import loaders.Loader;
import maths.Vec3f;
import utils.ToBuffer;

public class Sun {

	private UniformBuffer uniformBuffer;
	private Vec3f direction;
	private float intensity;
	
	public Sun(Loader loader) {
		uniformBuffer = loader.ubos().generateUniformBuffer(20);
		
		direction = new Vec3f(-0.43761906f, -0.83625764f, 0.33039787f).getNormalized();
		intensity = 1.0f;
		
		uniformBuffer.update(0, ToBuffer.from(new float[] {direction.x, direction.y, direction.z, intensity, Const.RENDER_AMBIENT_LIGHT}));
	}
	
	public UniformBuffer getUniformBuffer() {
		return uniformBuffer;
	}
}
