package bin;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import entities.Entity;

public class ModelBin<Model> {
	
	private Map<Model, EntityBin> entities;
	
	public ModelBin() {
		this.entities = new HashMap<Model, EntityBin>();
	}
	
	public void clear() {
		entities.clear();
	}
	
	public Set<Model> getModels() {
		return entities.keySet();
	}
	
	public EntityBin getBin(Model model) {
		return entities.get(model);
	}
	
	@SuppressWarnings("unchecked")
	public void put(Entity entity) {
		EntityBin bin = entities.get(entity.getModel());
		if(bin == null) {
			bin = new EntityBin(entity.getModel());
			entities.put((Model) entity.getModel(), bin);
		}
		bin.put(entity);
	}
	
	@SuppressWarnings("unchecked")
	public void put(EntityBin entityBin) {
		EntityBin bin = entities.get(entityBin.getModel());
		if(bin == null) {
			entities.put((Model) entityBin.getModel(), entityBin);
		} else {
			bin.put(entityBin);
			entities.put((Model) entityBin.getModel(), bin);
		}
	}
	
	public void put(ModelBin<Model> modelBin) {
		for(Model model:modelBin.getModels()) {
			put(modelBin.getBin(model));
		}
	}
}
