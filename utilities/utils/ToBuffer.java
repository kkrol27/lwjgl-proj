package utils;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;

public class ToBuffer {
	
	private static FloatBuffer floatBuffer;
	private static IntBuffer intBuffer;
	private static ByteBuffer byteBuffer;

	public static FloatBuffer from(float[] data) {
		floatBuffer = BufferUtils.createFloatBuffer(data.length);
		floatBuffer.put(data);
		floatBuffer.flip();
		return floatBuffer;
	}
	
	public static IntBuffer from(int[] data) {
		intBuffer = BufferUtils.createIntBuffer(data.length);
		intBuffer.put(data);
		intBuffer.flip();
		return intBuffer;
	}
	
	public static ByteBuffer from(byte[] data) {
		byteBuffer = BufferUtils.createByteBuffer(data.length);
		byteBuffer.put(data);
		byteBuffer.flip();
		return byteBuffer;
	}
}
