package loaders;

public abstract class HandlerObject {

	private int objectID;
	
	protected HandlerObject(int objectID) {
		this.objectID = objectID;
	}
	
	public abstract void dispose();
	
	public int getID() {
		return objectID;
	}
}
