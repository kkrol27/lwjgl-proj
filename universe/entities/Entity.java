package entities;

import maths.Mat4f;
import maths.Vec3f;

public abstract class Entity {

	private Object model;
	private Mat4f modelMatrix;
	private Vec3f position;
	private Vec3f rotation;
	private float scalar;
	
	protected Entity(Object model) {
		this.model = model;
		this.modelMatrix = new Mat4f().setIdentity();
		this.position = new Vec3f();
		this.rotation = new Vec3f();
		this.scalar = 1.0f;
	}

	public Object getModel() {
		return model;
	}

	public Mat4f getModelMatrix() {
		return modelMatrix;
	}

	public Vec3f getPosition() {
		return position;
	}

	protected void setPosition(Vec3f position) {
		this.position = position;
	}

	protected Vec3f getRotation() {
		return rotation;
	}

	protected void setRotation(Vec3f rotation) {
		this.rotation = rotation;
	}
	
	protected float getScalar() {
		return scalar;
	}
	
	protected void setScalar(float scalar) {
		this.scalar = scalar;
	}
	
	protected void calculateModelMatrix() {
		modelMatrix = new Mat4f().setZRotation(rotation.z);
		modelMatrix.addXRotation(rotation.x);
		modelMatrix.addYRotation(rotation.y);
		modelMatrix.addTranslation(position);
		modelMatrix.scale(scalar);
	}
}
