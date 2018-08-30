package universe;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

import engine.Const;
import entities.Entity;
import entities.Player;
import loaders.Loader;
import models.TransparentModel;
import terrain.Terrain;
import terrain.TerrainData;
import terrain.TerrainTexturePack;
import terrain.WaterTile;

public class World {
	
	private static final int WORLD_FILLED_ARRAY_DIM = 2 * Const.WORLD_LOAD_DISTANCE + 1;
	
	private ConcurrentLinkedQueue<TerrainData> terrainFinalizeQueue;
	private ConcurrentLinkedQueue<Terrain> terrainDisposerQueue;
	private ConcurrentLinkedQueue<Terrain> terrainList;
	
	private TerrainTexturePack terrainTexturePack;
	private List<Entity> currentFoliage;
	private WaterTile[] currentWaterTile;
	private Terrain[] currentTerrain;
	
	private TransparentModel[] foliageModels;
	private List<TerrainSpace> terrainSpaces;
	private List<TerrainData> dataLoading;
	private boolean[][] spacesFilled;
	private List<Terrain> toRemove;

	public World(Loader loader) {
		this.terrainFinalizeQueue = new ConcurrentLinkedQueue<TerrainData>();
		this.terrainDisposerQueue = new ConcurrentLinkedQueue<Terrain>();
		this.terrainList = new ConcurrentLinkedQueue<Terrain>();
		
		this.terrainTexturePack = new TerrainTexturePack(loader);
		this.currentFoliage = new ArrayList<Entity>();
		this.currentWaterTile = new WaterTile[0];
		this.currentTerrain = new Terrain[0];
		
		this.terrainSpaces = new ArrayList<TerrainSpace>();
		this.dataLoading = new ArrayList<TerrainData>();
		this.toRemove = new ArrayList<Terrain>();
		
		this.foliageModels = new TransparentModel[] {
				loader.loadTransparentModel("pine.mdl")
		};
	}
	
	public void dispose() {
		for(TerrainData terrainData:dataLoading) {
			try {
				terrainData.getThread().join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	public float getHeightOfTerrain(float x, float z) {
		int u = (int) Math.floor(x / Const.TERRAIN_GRID_SIZE);
		int v = (int) Math.floor(z / Const.TERRAIN_GRID_SIZE);
		float s = x - u*Const.TERRAIN_GRID_SIZE;
		float t = z - v*Const.TERRAIN_GRID_SIZE;
		for(Terrain terrain:getCurrentTerrain()) {
			if(terrain.getU() == u && terrain.getV() == v) {
				return terrain.getHeight(s, t);
			}
		}
		return 0.0f;
	}
	
	public TerrainTexturePack getTerrainTexturePack() {
		return terrainTexturePack;
	}
	
	public List<Entity> getCurrentFoliage() {
		return currentFoliage;
	}
	
	public WaterTile[] getCurrentWaterTile() {
		return currentWaterTile;
	}
	
	public Terrain[] getCurrentTerrain() {
		return currentTerrain;
	}
	
	public void primaryUpdate(Loader loader) {
		TerrainData terrainData = null;
		if((terrainData = terrainFinalizeQueue.poll()) != null) {
			terrainList.offer(terrainData.loadTerrain(loader));
		}
		Terrain terrain = null;
		if((terrain = terrainDisposerQueue.poll()) != null) {
			terrain.getModel().dispose(loader);
			terrain.getWaterTile().getModel().dispose(loader);
		}
		currentTerrain = terrainList.toArray(new Terrain[0]);
		currentWaterTile = new WaterTile[currentTerrain.length];
		for(int i = 0; i < currentWaterTile.length; i++) {
			currentWaterTile[i] = currentTerrain[i].getWaterTile();
		}
		currentFoliage.clear();
		for(Terrain t:currentTerrain) {
			currentFoliage.addAll(t.getFoliage());
		}
	}
	
	public void secondaryUpdate(Player player) {
		this.spacesFilled = new boolean[WORLD_FILLED_ARRAY_DIM][WORLD_FILLED_ARRAY_DIM];
		int u = (int) Math.floor(player.getPosition().x / ((float) Const.TERRAIN_GRID_SIZE));
		int v = (int) Math.floor(player.getPosition().z / ((float) Const.TERRAIN_GRID_SIZE));
		
		int i, j, nu, nv;
		for(Terrain terrain:terrainList) {
			i = terrain.getU() - u;
			j = terrain.getV() - v;
			if(Math.abs(i) <= Const.WORLD_LOAD_DISTANCE && Math.abs(j) <= Const.WORLD_LOAD_DISTANCE) {
				spacesFilled[i + Const.WORLD_LOAD_DISTANCE][j + Const.WORLD_LOAD_DISTANCE] = true;
			} else if(Math.abs(i) > Const.WORLD_DROP_DISTANCE || Math.abs(j) > Const.WORLD_DROP_DISTANCE) {
				this.removeTerrainTile(terrain.getU(), terrain.getV());
				this.toRemove.add(terrain);
			}
		}
		for(i = 0; i < spacesFilled.length; i++) {
			for(j = 0; j < spacesFilled.length; j++) {
				if(!spacesFilled[i][j]) {
					nu = i - Const.WORLD_LOAD_DISTANCE + u;
					nv = j - Const.WORLD_LOAD_DISTANCE + v;
					if(!this.terrainTileAccountedFor(nu, nv)) {
						this.dataLoading.add(new TerrainData(nu, nv, foliageModels));
						terrainSpaces.add(new TerrainSpace(nu, nv));
					}
				}
			}
		}
		this.terrainList.removeAll(toRemove);
		this.terrainDisposerQueue.addAll(toRemove);
		toRemove.clear();
		
		TerrainData terrainData = null;
		for(Iterator<TerrainData> iterator = dataLoading.iterator(); iterator.hasNext(); ) {
			terrainData = iterator.next();
			if(!terrainData.getThread().isAlive()) {
				terrainFinalizeQueue.offer(terrainData);
				iterator.remove();
			}
		}
	}
	
	private boolean terrainTileAccountedFor(int u, int v) {
		for(TerrainSpace space:terrainSpaces) {
			if(space.equals(u, v)) {
				return true;
			}
		}
		return false;
	}
	
	private void removeTerrainTile(int u, int v) {
		for(Iterator<TerrainSpace> iterator = terrainSpaces.iterator(); iterator.hasNext(); ) {
			if(iterator.next().equals(u, v)) {
				iterator.remove();
				return;
			}
		}
	}
	
	private class TerrainSpace {
		
		private int u;
		private int v;
		
		public TerrainSpace(int u, int v) {
			this.u = u;
			this.v = v;
		}
		
		public boolean equals(int u, int v) {
			return ((u == this.u) && (v == this.v));
		}
	}
}
