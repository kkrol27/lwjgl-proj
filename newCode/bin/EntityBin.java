package bin;

import java.util.LinkedList;
import java.util.List;

import entities.Entity;

public class EntityBin {
	
	private Class<?> model;
	private LinkedList<Entity> entities;
	
	public EntityBin(Class<?> model) {
		this.model = model;
		this.entities = new LinkedList<Entity>();
	}
	
	public void clear() {
		this.entities.clear();
	}
	
	public Class<?> getModel() {
		return model;
	}
	
	public List<Entity> asList() {
		return entities;
	}
	
	public void put(Entity entity) {
		entities.add(entity);
	}
	
	public void put(EntityBin entityBin) {
		this.entities.addAll(entityBin.asList());
	}
}
