package gl;

import org.lwjgl.opengl.GL11;

import loaders.HandlerObject;

public class Texture extends HandlerObject {
	
	public Texture(int textureID) {
		super(textureID);
	}
	
	@Override
	public void dispose() {
		GL11.glDeleteTextures(super.getID());
	}
}
