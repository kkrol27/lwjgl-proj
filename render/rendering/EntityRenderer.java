package rendering;

import maths.Mat4f;
import shaders.Shader;
import shaders.TransparentShader;
import universe.Universe;

public class EntityRenderer {
	
	private TransparentShader transparentShader;

	public EntityRenderer(Universe universe, Mat4f projectionMatrix) {
		transparentShader = new TransparentShader();
		transparentShader.start();
		transparentShader.loadProjectionMatrix(projectionMatrix);
		Shader.stop();
	}
	
	public void dispose() {
		transparentShader.dispose();
	}
	
	public void render(Universe universe) {
		transparentShader.start();
		
		
		
		Shader.stop();
	}
}
