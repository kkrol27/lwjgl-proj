package shaders;

import engine.Const;
import gl.UniformBuffer;
import maths.Mat4f;
import maths.Vec3f;
import maths.Vec4f;

public class TerrainLqShader extends Shader {

	private static final String file = "/source/terrain_lq";
	
	private int Sun;
	
	private int view_matrix;
	private int proj_matrix;
	private int texture_n;
	private int texture_r;
	private int texture_g;
	private int texture_b;
	private int fog_near;
	private int fog_delta;
	private int clip_plane;
	private int sun_direction;
	
	public TerrainLqShader() {
		super(file);
	}
	
	protected TerrainLqShader(String file) {
		super(file);
	}

	@Override
	protected void getAllUniformLocations() {
		Sun = super.getUniformBlockIndex("Sun");
		
		view_matrix = super.getUniformVariableLocation("view_matrix");
		proj_matrix = super.getUniformVariableLocation("proj_matrix");
		texture_n = super.getUniformVariableLocation("texture_n");
		texture_r = super.getUniformVariableLocation("texture_r");
		texture_g = super.getUniformVariableLocation("texture_g");
		texture_b = super.getUniformVariableLocation("texture_b");
		fog_near = super.getUniformVariableLocation("fog_near");
		fog_delta = super.getUniformVariableLocation("fog_delta");
		clip_plane = super.getUniformVariableLocation("clip_plane");
		sun_direction = super.getUniformVariableLocation("sun_direction");
	}
	
	public void loadTexturesAndConstants() {
		super.loadInt(texture_n, 0);
		super.loadInt(texture_r, 1);
		super.loadInt(texture_g, 2);
		super.loadInt(texture_b, 3);
		super.loadFloat(fog_near, Const.RENDER_FOG_NEAR);
		super.loadFloat(fog_delta, Const.RENDER_FOG_DELTA);
	}
	
	public void loadSunUniform(UniformBuffer uniformBuffer) {
		super.loadUniformBlockBinding(Sun, uniformBuffer.getBindingPoint());
	}
	
	public void loadViewMatrix(Mat4f viewMatrix) {
		super.loadMat4(view_matrix, viewMatrix);
	}
	
	public void loadProjectionMatrix(Mat4f projectionMatrix) {
		super.loadMat4(proj_matrix, projectionMatrix);
	}
	
	public void loadClipPlane(Vec4f clipPlane) {
		super.loadVector(clip_plane, clipPlane);
	}
	
	public void loadSunDirection(Vec3f direction) {
		super.loadVector(sun_direction, direction);
	}
}
