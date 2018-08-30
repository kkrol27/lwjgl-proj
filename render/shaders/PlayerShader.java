package shaders;

import maths.Mat4f;
import maths.Vec4f;

public class PlayerShader extends Shader {
	
	private static final String file = "/source/player";
	
	private int modl_matrix;
	private int view_matrix;
	private int proj_matrix;
	private int texture_0;
	private int clip_plane;

	public PlayerShader() {
		super(file);
	}

	@Override
	protected void getAllUniformLocations() {
		modl_matrix = super.getUniformVariableLocation("modl_matrix");
		view_matrix = super.getUniformVariableLocation("view_matrix");
		proj_matrix = super.getUniformVariableLocation("proj_matrix");
		texture_0 = super.getUniformVariableLocation("texture_0");
		clip_plane = super.getUniformVariableLocation("clip_plane");
	}
	
	public void loadTexturesAndConstants() {
		super.loadInt(texture_0, 0);
	}
	
	public void loadModelMatrix(Mat4f modelMatrix) {
		super.loadMat4(modl_matrix, modelMatrix);
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
}
