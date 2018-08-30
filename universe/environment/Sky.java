package environment;

import gl.Texture;
import loaders.Loader;
import maths.Mat3f;
import maths.Vec3f;
import models.Model;

public class Sky {
	
	private static final float[] VERTICES = {
		    -1.0f,  1.0f, -1.0f,
		    -1.0f, -1.0f, -1.0f,
		     1.0f, -1.0f, -1.0f,
		     1.0f, -1.0f, -1.0f,
		     1.0f,  1.0f, -1.0f,
		    -1.0f,  1.0f, -1.0f,

		    -1.0f, -1.0f,  1.0f,
		    -1.0f, -1.0f, -1.0f,
		    -1.0f,  1.0f, -1.0f,
		    -1.0f,  1.0f, -1.0f,
		    -1.0f,  1.0f,  1.0f,
		    -1.0f, -1.0f,  1.0f,

		     1.0f, -1.0f, -1.0f,
		     1.0f, -1.0f,  1.0f,
		     1.0f,  1.0f,  1.0f,
		     1.0f,  1.0f,  1.0f,
		     1.0f,  1.0f, -1.0f,
		     1.0f, -1.0f, -1.0f,

		    -1.0f, -1.0f,  1.0f,
		    -1.0f,  1.0f,  1.0f,
		     1.0f,  1.0f,  1.0f,
		     1.0f,  1.0f,  1.0f,
		     1.0f, -1.0f,  1.0f,
		    -1.0f, -1.0f,  1.0f,

		    -1.0f,  1.0f, -1.0f,
		     1.0f,  1.0f, -1.0f,
		     1.0f,  1.0f,  1.0f,
		     1.0f,  1.0f,  1.0f,
		    -1.0f,  1.0f,  1.0f,
		    -1.0f,  1.0f, -1.0f,

		    -1.0f, -1.0f, -1.0f,
		    -1.0f, -1.0f,  1.0f,
		     1.0f, -1.0f, -1.0f,
		     1.0f, -1.0f, -1.0f,
		    -1.0f, -1.0f,  1.0f,
		     1.0f, -1.0f,  1.0f
	};
	
	private Model cube;
	private Vec3f sunlightDirection;
	private Texture dayCubeMap;
	
	public Sky(Loader loader) {
		cube = loader.mdls().loadModel(new float[][] {VERTICES}, new int[] {3});
		dayCubeMap = loader.txts().loadCubeMapTexture("standard");
		
		sunlightDirection = new Vec3f(0.0f, 0.0f, -1.0f);
		Mat3f rotate = new Mat3f().setXRotation(-0.96710175f);
		rotate.addYRotation(5.3631177f);
		rotate.transform(sunlightDirection);
	}
	
	public Model getCubeModel() {
		return cube;
	}
	
	public Texture getDayCubeMap() {
		return dayCubeMap;
	}
	
	public Vec3f getSunlightDirection() {
		return sunlightDirection;
	}
}
