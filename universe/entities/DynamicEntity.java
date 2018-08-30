package entities;

import maths.Vec3f;

public class DynamicEntity extends Entity {

	protected DynamicEntity(Object model) {
		super(model);
	}

	public void stepPosition(Vec3f ds) {
		super.getPosition().sum(ds);
	}
	
	public void stepRotation(Vec3f da) {
		super.getRotation().sum(da);
	}
	
	public void adjustScalar(float ds) {
		super.setScalar(super.getScalar() * ds);
	}
	
	public void update() {
		super.calculateModelMatrix();
	}
}
