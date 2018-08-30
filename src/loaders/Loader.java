package loaders;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import gl.Texture;
import models.Model;
import models.TexturedModel;
import models.TransparentModel;

public class Loader {

	private TextureHandler textureHandler;
	private VertexBufferHandler vertexBufferHandler;
	private UniformBufferHandler uniformBufferHandler;
	private VertexArrayHandler vertexArrayHandler;
	private ModelHandler modelHandler;
	private String path;
	
	public Loader() {
		this.textureHandler = new TextureHandler();
		this.vertexBufferHandler = new VertexBufferHandler();
		this.uniformBufferHandler = new UniformBufferHandler();
		this.vertexArrayHandler = new VertexArrayHandler();
		this.modelHandler = new ModelHandler(this);
	}
	
	public void dispose() {
		textureHandler.dispose();
		vertexBufferHandler.dispose();
		uniformBufferHandler.dispose();
		vertexArrayHandler.dispose();
	}
	
	public void setPath(String path) {
		this.path = path;
	}
	
	public TextureHandler txts() {
		return textureHandler;
	}
	
	public VertexBufferHandler vbos() {
		return vertexBufferHandler;
	}
	
	public UniformBufferHandler ubos() {
		return uniformBufferHandler;
	}
	
	public VertexArrayHandler vaos() {
		return vertexArrayHandler;
	}
	
	public ModelHandler mdls() {
		return modelHandler;
	}
	
	public TexturedModel loadTexturedModel(String modelFile) {
		String file = null;
		String[] textureStrings = null;
		BufferedReader reader = null;
		try {
			InputStream in = this.getClass().getResourceAsStream(path + modelFile);
			reader = new BufferedReader(new InputStreamReader(in));
			file = reader.readLine();
			textureStrings = reader.readLine().split(" ");
		} catch(IOException e) {
			e.printStackTrace();
			System.exit(-1);
		} finally {
			if(reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		Model model = mdls().loadObjModel(file);
		Texture[] textures = new Texture[textureStrings.length];
		for(int i = 0; i < textureStrings.length; i++) {
			textures[i] = txts().load2DTexture(textureStrings[i], false, 0.0f);
		}
		return new TexturedModel(model, textures);
	}
	
	public TransparentModel loadTransparentModel(String modelFile) {
		TexturedModel model = loadTexturedModel(modelFile);
		return new TransparentModel(model.getModel(), model.getTextures());
	}
}
