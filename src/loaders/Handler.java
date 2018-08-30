package loaders;

import java.util.HashMap;
import java.util.Map;

/**
 * Generic data structure for objects generated via glGen* methods. Stores said
 * objects in a hash map based on their id value. These stored objects are
 * wrapped in the abstract HandlerObject class.
 * 
 * @author Kyle Krol
 */
public abstract class Handler {
	
	/**
	 * Map storing all of the {@code HandlerObject} objects according to their
	 * id
	 */
	private Map<Integer, HandlerObject> objects;
	
	public Handler() {
		this.objects = new HashMap<Integer, HandlerObject>();
	}
	
	/**
	 * Disposes all of the objects stored in this {@code Handler}
	 */
	public void dispose() {
		for(Integer key:objects.keySet()) {
			objects.get(key).dispose();
		}
	}
	
	/**
	 * Store a {@code HandlerObject} in this {@code Handler} according to its
	 * {@code key}
	 * 
	 * @param key
	 * 		key to the given object
	 * @param object
	 * 		object to be stored
	 */
	protected void addObject(Integer key, HandlerObject object) {
		objects.put(key, object);
	}
	
	/**
	 * Dispose the object and remove it from this {@code Handler} linked to the
	 * specified {@code key}.
	 * 
	 * @param key
	 * 		key for the object to be disposed
	 */
	public void disposeObject(Integer key) {
		objects.remove(key).dispose();
	}
}
