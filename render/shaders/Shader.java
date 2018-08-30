package shaders;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL31;

import maths.Mat3f;
import maths.Mat4f;
import maths.Vec2f;
import maths.Vec3f;
import maths.Vec4f;

public abstract class Shader {

	private int fragmentShaderID;
	private int vertexShaderID;
	private int programID;
	
	protected Shader(String file) {
		vertexShaderID = loadShader(file + ".vsh", GL20.GL_VERTEX_SHADER);
		fragmentShaderID = loadShader(file + ".fsh", GL20.GL_FRAGMENT_SHADER);
		programID = GL20.glCreateProgram();
		GL20.glAttachShader(programID, vertexShaderID);
		GL20.glAttachShader(programID, fragmentShaderID);
		GL20.glLinkProgram(programID);
		GL20.glValidateProgram(programID);
		getAllUniformLocations();
	}
	
	protected abstract void getAllUniformLocations();
	
	public void start() {
		GL20.glUseProgram(programID);
	}
	
	public static void stop() {
		GL20.glUseProgram(0);
	}
	
	public void dispose() {
		Shader.stop();
		GL20.glDetachShader(programID, vertexShaderID);
		GL20.glDetachShader(programID, fragmentShaderID);
		GL20.glDeleteShader(vertexShaderID);
		GL20.glDeleteShader(fragmentShaderID);
		GL20.glDeleteProgram(programID);
	}
	
	private int loadShader(String file, int GL_CONSTANT) {
		String line;
		BufferedReader reader = null;
		StringBuilder shaderSource = new StringBuilder();
        try {
        	InputStream in = this.getClass().getResourceAsStream(file);
            reader = new BufferedReader(new InputStreamReader(in));
            while((line = reader.readLine()) != null) {
                shaderSource.append(line).append("\n");
            }
        } catch(IOException e) {
            e.printStackTrace();
            System.exit(-1);
        } finally {
        	if(reader != null) {
        		try {
					reader.close();
				} catch (IOException e) {
					
				}
        	}
        }
        int shaderID = GL20.glCreateShader(GL_CONSTANT);
        GL20.glShaderSource(shaderID, shaderSource);
        GL20.glCompileShader(shaderID);
        if(GL20.glGetShaderi(shaderID, GL20.GL_COMPILE_STATUS) == GL11.GL_FALSE) {
            System.out.println(GL20.glGetShaderInfoLog(shaderID, 500));
            System.err.println("Could not compile shader!");
            System.exit(-1);
        }
        return shaderID;
	}
	
	protected int getUniformVariableLocation(String uniformName) {
		return GL20.glGetUniformLocation(programID, uniformName);
	}
	
	protected int getUniformBlockIndex(String uniformName) {
		return GL31.glGetUniformBlockIndex(programID, uniformName);
	}
	
	protected void loadUniformBlockBinding(int location, int binding) {
		GL31.glUniformBlockBinding(programID, location, binding);
	}
	
	protected void loadFloat(int location, float value) {
		GL20.glUniform1f(location, value);
	}
	
	protected void loadInt(int location, int value) {
		GL20.glUniform1i(location, value);
	}
	
	protected void loadVector(int location, Vec2f vector) {
		GL20.glUniform2f(location, vector.x, vector.y);
	}
	
	protected void loadVector(int location, Vec3f vector) {
		GL20.glUniform3f(location, vector.x, vector.y, vector.z);
	}
	
	protected void loadVector(int location, Vec4f vector) {
		GL20.glUniform4f(location, vector.x, vector.y, vector.z, vector.w);
	}
	
	protected void loadBoolean(int location, boolean value) {
		if(value) {
			GL20.glUniform1f(location, 1);
		} else {
			GL20.glUniform1f(location, 0);
		}
	}
	
	protected void loadMat4(int location, Mat4f matrix) {
		GL20.glUniformMatrix4fv(location, false, matrix.getBuffer());
	}
	
	protected void loadMat3(int location, Mat3f matrix) {
		GL20.glUniformMatrix3fv(location, false, matrix.getBuffer());
	}
}
