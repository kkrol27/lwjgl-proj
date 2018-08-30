package shaders;

import engine.Const;
import gl.UniformBuffer;
import maths.Mat4f;
import maths.Vec2f;
import maths.Vec3f;
import maths.Vec4f;

public class WaterShader extends Shader {

	private static final String file = "/source/water";
	
	private int Sun;
	
	private int proj_matrix;
	private int view_matrix;
	private int fog_near;
	private int fog_delta;
	private int dudv_texture;
	private int normals_texture;
	private int refract_texture;
	private int reflect_texture;
	private int refract_depth;
	private int cam_position;
	private int ripple_offset;
	private int blend_depth;
	private int ripple_strength;
	private int near_plane;
	private int far_plane;
	private int clear_color;
	private int fresnel_damper;
	private int specular_reflectivity;
	private int specular_damper;
	
	public WaterShader() {
		super(file);
	}

	@Override
	protected void getAllUniformLocations() {
		Sun = super.getUniformBlockIndex("Sun");
		
		proj_matrix = super.getUniformVariableLocation("proj_matrix");
		view_matrix = super.getUniformVariableLocation("view_matrix");
		fog_near = super.getUniformVariableLocation("fog_near");
		fog_delta = super.getUniformVariableLocation("fog_delta");
		dudv_texture = super.getUniformVariableLocation("dudv_texture");
		normals_texture = super.getUniformVariableLocation("normals_texture");
		refract_texture = super.getUniformVariableLocation("refract_texture");
		reflect_texture = super.getUniformVariableLocation("reflect_texture");
		refract_depth = super.getUniformVariableLocation("refract_depth");
		cam_position = super.getUniformVariableLocation("cam_position");
		ripple_offset = super.getUniformVariableLocation("ripple_offset");
		blend_depth = super.getUniformVariableLocation("blend_depth");
		ripple_strength = super.getUniformVariableLocation("ripple_strength");
		near_plane = super.getUniformVariableLocation("near_plane");
		far_plane = super.getUniformVariableLocation("far_plane");
		clear_color = super.getUniformVariableLocation("clear_color");
		fresnel_damper = super.getUniformVariableLocation("fresnel_damper");
		specular_reflectivity = super.getUniformVariableLocation("specular_reflectivity");
		specular_damper = super.getUniformVariableLocation("specular_damper");
	}
	
	public void loadTexturesAndConstants() {
		super.loadFloat(fog_near, Const.RENDER_FOG_NEAR);
		super.loadFloat(fog_delta, Const.RENDER_FOG_DELTA);
		super.loadInt(dudv_texture, 0);
		super.loadInt(normals_texture, 1);
		super.loadInt(refract_texture, 2);
		super.loadInt(reflect_texture, 3);
		super.loadInt(refract_depth, 4);
		super.loadFloat(blend_depth, Const.WATER_BLEND_DEPTH);
		super.loadFloat(ripple_strength, Const.WATER_RIPPLE_STENGTH);
		super.loadFloat(near_plane, Const.RENDER_NEAR_PLANE);
		super.loadFloat(far_plane, Const.RENDER_FAR_PLANE);
		super.loadVector(clear_color, new Vec4f(Const.RENDER_COLOR_R, Const.RENDER_COLOR_G, Const.RENDER_COLOR_B, Const.RENDER_COLOR_A));
		super.loadFloat(fresnel_damper, Const.WATER_FRESNEL_DAMPER);
		super.loadFloat(specular_reflectivity, Const.WATER_REFLECTIVITY);
		super.loadFloat(specular_damper, Const.WATER_SPEC_DAMPER);
	}
	
	public void loadSunUniform(UniformBuffer uniformBuffer) {
		super.loadUniformBlockBinding(Sun, uniformBuffer.getBindingPoint());
	}
	
	public void loadProjectionMatrix(Mat4f projectionMatrix) {
		super.loadMat4(proj_matrix, projectionMatrix);
	}
	
	public void loadViewMatrix(Mat4f viewMatrix) {
		super.loadMat4(view_matrix, viewMatrix);
	}
	
	public void loadCameraPosition(Vec3f position) {
		super.loadVector(cam_position, position);
	}
	
	public void loadRippleOffset(Vec2f ripples) {
		super.loadVector(ripple_offset, ripples);
	}
}
