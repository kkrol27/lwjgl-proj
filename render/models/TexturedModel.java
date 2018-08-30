package models;

import gl.Texture;

public class TexturedModel {

	private Model model;
	private Texture[] texture;
	
	public TexturedModel(Model model, Texture[] texture) {
		this.model = model;
		this.texture = texture;
	}

	public Model getModel() {
		return model;
	}

	public Texture[] getTextures() {
		return texture;
	}
}
