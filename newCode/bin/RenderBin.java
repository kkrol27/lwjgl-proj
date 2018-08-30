package bin;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class RenderBin {
	
	private Map<Class<?>, ModelBin<?>> entities;
	
	public RenderBin() {
		this.entities = new HashMap<Class<?>, ModelBin<?>>();
	}
	
	public void clear() {
		entities.clear();
	}
	
	public Set<Class<?>> getTypes() {
		return entities.keySet();
	}
	
	@SuppressWarnings("unchecked")
	public <T> ModelBin<T> getModelBin(Class<T> type) {
		return (ModelBin<T>) entities.get(type);
	}
}
