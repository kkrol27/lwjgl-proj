package terrain;

import gl.Texture;
import loaders.Loader;

public class TerrainTexturePack {

	private Texture texture_;
	private Texture normals_;
	private Texture textureR;
	private Texture normalsR;
	private Texture textureG;
	private Texture normalsG;
	private Texture textureB;
	private Texture normalsB;
	
	public TerrainTexturePack(Loader loader) {
		texture_ = loader.txts().load2DTexture("dirtTxt.png", false, -0.5f);
		normals_ = loader.txts().load2DTexture("dirtNrm.png", false, 0.0f);
		
		textureR = loader.txts().load2DTexture("grassTxt.png", false, -0.5f);
		normalsR = loader.txts().load2DTexture("grassNrm.png", false, 0.0f);
		
		textureG = loader.txts().load2DTexture("mudTxt.png", false, -0.5f);
		normalsG = loader.txts().load2DTexture("mudNrm.png", false, 0.0f);
		
		textureB = loader.txts().load2DTexture("rockTxt.png", false, -0.5f);
		normalsB = loader.txts().load2DTexture("rockNrm.png", false, 0.0f);
		/*
		texture_ = loader.getTextureID("dirt_texture");
		normals_ = loader.getTextureID("dirt_normals");
		textureR = loader.getTextureID("grass_texture");
		normalsR = loader.getTextureID("grass_normals");
		textureG = loader.getTextureID("mud_texture");
		normalsG = loader.getTextureID("mud_normals");
		textureB = loader.getTextureID("rock_texture");
		normalsB = loader.getTextureID("rock_normals");
		*/
	}

	public Texture getTexture_() {
		return texture_;
	}

	public Texture getNormals_() {
		return normals_;
	}

	public Texture getTextureR() {
		return textureR;
	}

	public Texture getNormalsR() {
		return normalsR;
	}

	public Texture getTextureG() {
		return textureG;
	}

	public Texture getNormalsG() {
		return normalsG;
	}
	
	public Texture getTextureB() {
		return textureB;
	}

	public Texture getNormalsB() {
		return normalsB;
	}
}
