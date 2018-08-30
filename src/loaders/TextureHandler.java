package loaders;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL14;
import org.lwjgl.opengl.GL30;

import de.matthiasmann.twl.utils.PNGDecoder;
import de.matthiasmann.twl.utils.PNGDecoder.Format;
import gl.Texture;

public class TextureHandler extends Handler {

	private String path;

	public TextureHandler() {
		super();
	}

	public void setPath(String path) {
		this.path = path;
	}

	private void loadTexturePixels(int target, String file) throws IOException {
		ByteBuffer buf = null;
		PNGDecoder data = null;
		InputStream input = null;
		try {
			input = this.getClass().getResourceAsStream(file);
			data = new PNGDecoder(input);

			buf = ByteBuffer.allocateDirect(4 * data.getWidth() * data.getHeight());
			data.decode(buf, 4 * data.getWidth(), Format.RGBA);
			buf.flip();
		} finally {
			if (input != null) {
				input.close();
			}
		}
		GL11.glTexImage2D(target, 0, GL11.GL_RGBA, data.getWidth(), data.getHeight(), 0, GL11.GL_RGBA,
				GL11.GL_UNSIGNED_BYTE, buf);
	}

	public Texture load2DTexture(String file, boolean isClamped, Float lodConstant) {
		int textureID = GL11.glGenTextures();
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureID);
		GL11.glPixelStorei(GL11.GL_UNPACK_ALIGNMENT, GL11.GL_TRUE);

		try {
			loadTexturePixels(GL11.GL_TEXTURE_2D, path + file);
		} catch (IOException e) {
			e.printStackTrace();
			GL11.glDeleteTextures(textureID);
			return null;
		}

		if (lodConstant != null) {
			GL30.glGenerateMipmap(GL11.GL_TEXTURE_2D);
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR_MIPMAP_LINEAR);
			GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL14.GL_TEXTURE_LOD_BIAS, lodConstant);
		} else {
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
		}

		if (isClamped) {
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL12.GL_CLAMP_TO_EDGE);
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE);
		}

		Texture texture = new Texture(textureID);
		super.addObject(textureID, texture);
		return texture;
	}

	public Texture loadCubeMapTexture(String file) {
		int textureID = GL11.glGenTextures();
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL13.GL_TEXTURE_CUBE_MAP, textureID);

		try {
			for (int i = 0; i < 6; i++) {
				loadTexturePixels(GL13.GL_TEXTURE_CUBE_MAP_POSITIVE_X + i, path + file + i + ".png");
			}
		} catch (IOException e) {
			e.printStackTrace();
			GL11.glDeleteTextures(textureID);
			return null;
		}

		GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
		GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
		GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP, GL11.GL_TEXTURE_WRAP_S, GL12.GL_CLAMP_TO_EDGE);
		GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP, GL11.GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE);

		Texture texture = new Texture(textureID);
		super.addObject(textureID, texture);
		return texture;
	}
}
