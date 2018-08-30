package shaders;

import maths.Mat3f;
import maths.Mat4f;

public class SkyShader extends Shader {

	private static final String file = "/source/sky";
	
	private int view_matrix;
	private int proj_matrix;
	private int texture;
	
	public SkyShader() {
		super(file);
	}

	@Override
	protected void getAllUniformLocations() {
		view_matrix = super.getUniformVariableLocation("view_matrix");
		proj_matrix = super.getUniformVariableLocation("proj_matrix");
		texture = super.getUniformVariableLocation("texture");
	}
	
	public void loadTexturesAndConstants() {
		super.loadInt(texture, 0);
	}
	
	public void loadProjectionMatrix(Mat4f projectionMatrix) {
		super.loadMat4(proj_matrix, projectionMatrix);
	}
	
	public void loadViewSkyMatrix(Mat3f viewMatrix) {
		super.loadMat3(view_matrix, viewMatrix);
	}
}
