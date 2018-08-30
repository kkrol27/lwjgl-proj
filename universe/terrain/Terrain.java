package terrain;

import java.util.List;

import engine.Const;
import entities.Entity;
import maths.Vec2f;
import maths.Vec3f;
import models.Model;
import models.TransparentModel;

public class Terrain {
	
	private List<Entity> foliage;
	private float[][] terrain;
	private WaterTile water;
	private Model model;
	private int u;
	private int v;
	
	public Terrain(float[][] terrain, List<Entity> foliage, Model model, WaterTile water, int u, int v) {
		this.terrain = terrain;
		this.foliage = foliage;
		this.water = water;
		this.model = model;
		this.u = u;
		this.v = v;
	}
	
	public List<Entity> getFoliage() {
		return foliage;
	}
	
	public float getHeight(float u, float v) {
		return getTerrainHeight(u, v, terrain);
	}
	
	public WaterTile getWaterTile() {
		return water;
	}
	
	public Model getModel() {
		return model;
	}
	
	public int getU() {
		return u;
	}
	
	public int getV() {
		return v;
	}
	
	private static final float TILE_SIZE = ((float) Const.TERRAIN_GRID_SIZE) / ((float) Const.TERRAIN_GRID_RES - 1);
	
	public static float getTerrainHeight(float u, float v, float[][] heights) {
		int gridX = (int) Math.floor(u / TILE_SIZE);
		int gridZ = (int) Math.floor(v / TILE_SIZE);
		float xCoord = (u % TILE_SIZE) / TILE_SIZE;
		float zCoord = (v % TILE_SIZE) / TILE_SIZE;
		float answer;
		if (xCoord <= (1-zCoord)) {
			answer = barryCentric(new Vec3f(0, heights[gridX + 1][gridZ + 1], 0), new Vec3f(1,
							heights[gridX + 2][gridZ + 1], 0), new Vec3f(0,
							heights[gridX + 1][gridZ + 2], 1), new Vec2f(xCoord, zCoord));
		} else {
			answer = barryCentric(new Vec3f(1, heights[gridX + 2][gridZ + 1], 0), new Vec3f(1,
							heights[gridX + 2][gridZ + 2], 1), new Vec3f(0,
							heights[gridX + 1][gridZ + 2], 1), new Vec2f(xCoord, zCoord));
		}
		return answer;
	}
	
	private static float barryCentric(Vec3f p1, Vec3f p2, Vec3f p3, Vec2f pos) {
		float det = (p2.z - p3.z) * (p1.x - p3.x) + (p3.x - p2.x) * (p1.z - p3.z);
		float l1 = ((p2.z - p3.z) * (pos.x - p3.x) + (p3.x - p2.x) * (pos.y - p3.z)) / det;
		float l2 = ((p3.z - p1.z) * (pos.x - p3.x) + (p1.x - p3.x) * (pos.y - p3.z)) / det;
		float l3 = 1.0f - l1 - l2;
		return l1 * p1.y + l2 * p2.y + l3 * p3.y;
	}
}
