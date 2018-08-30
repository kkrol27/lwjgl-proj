package shaders;

import maths.Mat4f;
import maths.Vec4f;

public class TransparentShader extends Shader {
	
	private static final String file = "/source/transparent";
	
	private int modl_matrix;
	private int view_matrix;
	private int proj_matrix;
	private int clip_plane;

	public TransparentShader() {
		super(file);
	}

	@Override
	protected void getAllUniformLocations() {
		modl_matrix = super.getUniformVariableLocation("modl_matrix");
		view_matrix = super.getUniformVariableLocation("view_matrix");
		proj_matrix = super.getUniformVariableLocation("proj_matrix");
		clip_plane = super.getUniformVariableLocation("clip_plane");
	}
	
	public void loadProjectionMatrix(Mat4f projectionMatrix) {
		super.loadMat4(proj_matrix, projectionMatrix);
	}
	
	public void loadViewMatrix(Mat4f viewMatrix) {
		super.loadMat4(view_matrix, viewMatrix);
	}
	
	public void loadModelMatrix(Mat4f modelMatrix) {
		super.loadMat4(modl_matrix, modelMatrix);
	}
	
	public void loadClipPlane(Vec4f clipPlane) {
		super.loadVector(clip_plane, clipPlane);
	}
}
