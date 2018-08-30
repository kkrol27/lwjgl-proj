package entities;

import org.lwjgl.glfw.GLFW;

import engine.Const;
import glfw.GLFWHandle;
import models.TexturedModel;
import universe.World;

public class Player extends DynamicEntity {

	private Camera camera;
	
	public Player(TexturedModel model) {
		super(model);
		this.camera = new Camera(this);
	}
	
	public Camera getCamera() {
		return camera;
	}

	public void update(World world) {
		float moveStep = 0.0f;
		if(GLFWHandle.keyIsDown(GLFW.GLFW_KEY_W)) {
			moveStep += Const.PLAYER_MOVE_SPEED;
		}
		if(GLFWHandle.keyIsDown(GLFW.GLFW_KEY_S)) {
			moveStep -= Const.PLAYER_MOVE_SPEED;
		}
		float rotationStep = 0.0f;
		if(GLFWHandle.keyIsDown(GLFW.GLFW_KEY_A)) {
			rotationStep += Const.PLAYER_ROTATION_SPEED;
		}
		if(GLFWHandle.keyIsDown(GLFW.GLFW_KEY_D)) {
			rotationStep -= Const.PLAYER_ROTATION_SPEED;
		}
		
		super.getPosition().x += ((float) Math.sin(super.getRotation().y)) * moveStep;
		super.getPosition().z += ((float) Math.cos(super.getRotation().y)) * moveStep;
		super.getPosition().y = world.getHeightOfTerrain(super.getPosition().x, super.getPosition().z);
		
		super.getRotation().y += rotationStep;
		
		camera.update();
		super.update();
	}
}
