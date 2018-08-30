package entities;

import maths.Vec3f;

public class StaticEntity extends Entity {

	public StaticEntity(Object model) {
		super(model);
	}

	public StaticEntity givePosition(Vec3f position) {
		super.setPosition(position);
		return this;
	}
	
	public StaticEntity giveRotation(Vec3f rotation) {
		super.setRotation(rotation);
		return this;
	}
	
	public StaticEntity giveScalar(float scalar) {
		super.setScalar(scalar);
		return this;
	}
	
	public StaticEntity finalizeModel() {
		super.calculateModelMatrix();
		return this;
	}
}
