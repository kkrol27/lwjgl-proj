package entities;

import org.lwjgl.glfw.GLFW;

import engine.Const;
import glfw.GLFWHandle;
import maths.Mat3f;
import maths.Mat4f;
import maths.Vec3f;

public class Camera {
	
	private Player player;
	private Mat4f viewFullMatrix;
	private Mat3f viewSkyMatrix;
	private Mat4f reflectFullMatrix;
	private Mat3f reflectSkyMatrix;
	private Vec3f position;
	private float radius;
	private float pitch;
	private float yaw;
	
	public Camera(Player player) {
		this.player = player;
		this.radius = Const.CAMERA_RADIUS;
		this.pitch = Const.CAMERA_PITCH;
		this.yaw = Const.CAMERA_YAW;
		this.position = new Vec3f();
	}
	
	public void update() {
		if(GLFWHandle.mouseButtonIsDown(GLFW.GLFW_MOUSE_BUTTON_LEFT)) {
			pitch -= GLFWHandle.getMouseDY() * Const.CAMERA_ORBIT_SPEED;
			yaw -= GLFWHandle.getMouseDX() * Const.CAMERA_ORBIT_SPEED;
		}
		if(GLFWHandle.mouseButtonIsDown(GLFW.GLFW_MOUSE_BUTTON_RIGHT)) {
			radius += GLFWHandle.getMouseDY() * Const.CAMERA_ZOOM_SPEED;
		}
		
		float netYaw = player.getRotation().y + yaw;
		
		float dr = (float)(radius * Math.cos(pitch));
		float dy = (float)(radius * Math.sin(pitch));
		float dx = (float)(-dr * Math.sin(netYaw));
		float dz = (float)(-dr * Math.cos(netYaw)); 
		
		position.x = player.getPosition().x - dx;
		position.y = player.getPosition().y + Const.CAMERA_VERTICAL_OFFSET - dy;
		position.z =  player.getPosition().z - dz;
		
		position.scaledBy(-1.0f);
		
		viewSkyMatrix = new Mat3f().setYRotation(-netYaw);
		viewSkyMatrix.addXRotation(-pitch);
		
		viewFullMatrix = new Mat4f().setTranslation(position);
		new Mat4f().setMatrix(viewSkyMatrix).transform(viewFullMatrix);
		
		reflectSkyMatrix = new Mat3f().setYRotation(-netYaw);
		reflectSkyMatrix.addXRotation(pitch);
		
		reflectFullMatrix = new Mat4f().setTranslation(new Vec3f(position.x, position.y - 2.0f*(Const.WATER_LEVEL + position.y), position.z));
		new Mat4f().setMatrix(reflectSkyMatrix).transform(reflectFullMatrix);
		
		position.scaledBy(-1.0f);
	}
	
	public Mat4f getViewFullMatrix() {
		return viewFullMatrix;
	}
	
	public Mat3f getViewSkyMatrix() {
		return viewSkyMatrix;
	}

	public Mat4f getReflectFullMatrix() {
		return reflectFullMatrix;
	}

	public Mat3f getReflectSkyMatrix() {
		return reflectSkyMatrix;
	}
	
	public Vec3f getPosition() {
		return position;
	}
}
