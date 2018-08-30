package terrain;

import engine.Const;
import loaders.Loader;
import models.Model;

public class WaterTile {
	
	private static final float[] TXT_COORDS = {
			0.0f, 0.0f,
			1.0f, 1.0f,
			1.0f, 0.0f,
			0.0f, 0.0f,
			0.0f, 1.0f,
			1.0f, 1.0f
	};
	
	private Model model;
	
	public WaterTile(Loader loader, int u, int v) {
		float x = Const.TERRAIN_GRID_SIZE * u;
		float z = Const.TERRAIN_GRID_SIZE * v;
		float[] vertices = new float[] {
				x, Const.WATER_LEVEL, z,
				x + Const.TERRAIN_GRID_SIZE, Const.WATER_LEVEL, z + Const.TERRAIN_GRID_SIZE,
				x + Const.TERRAIN_GRID_SIZE, Const.WATER_LEVEL, z,
				x, Const.WATER_LEVEL, z,
				x, Const.WATER_LEVEL, z + Const.TERRAIN_GRID_SIZE,
				x + Const.TERRAIN_GRID_SIZE, Const.WATER_LEVEL, z + Const.TERRAIN_GRID_SIZE
		};
		this.model = loader.mdls().loadModel(new float[][] {vertices, TXT_COORDS}, new int[] {3, 2});
	}
	
	public Model getModel() {
		return model;
	}
}
