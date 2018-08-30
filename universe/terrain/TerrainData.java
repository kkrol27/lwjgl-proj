package terrain;

import java.util.ArrayList;
import java.util.List;

import engine.Const;
import entities.Entity;
import entities.StaticEntity;
import loaders.Loader;
import maths.Vec2f;
import maths.Vec3f;
import models.TransparentModel;

public class TerrainData implements Runnable {
	
	private static final int VERTEX_COUNT = Const.TERRAIN_GRID_RES * Const.TERRAIN_GRID_RES;
	private static final float STEEP_BLEND_DELTA = Const.GENERATOR_STEEP_BLEND_HIGH - Const.GENERATOR_STEEP_BLEND_LOW,
							   SHALLOW_BLEND_DELTA = Const.GENERATOR_SHALLOW_BLEND_HIGH - Const.GENERATOR_SHALLOW_BLEND_LOW,
							   WATER_BLEND_LOW = Const.WATER_LEVEL + Const.GENERATOR_WATER_BLEND_OFFSET,
							   WATER_BLEND_HIGH = WATER_BLEND_LOW + Const.GENERATOR_WATER_BLEND_DELTA;
	
	private Thread thread;
	
	private TransparentModel[] foliageModels;
	private List<Entity> foliage;
	private float[][] heights;
	private float[][] normalsArray;
	private float[] textureCoords;
	private float[] vertices;
	private float[] binormals;
	private float[] tangents;
	private float[] normals;
	private float[] colors;
	private int[] indices;
	private int u;
	private int v;
	
	public TerrainData(int u, int v, TransparentModel[] foliageModels) {
		this.foliageModels = foliageModels;
		this.foliage = new ArrayList<Entity>();
		this.u = u;
		this.v = v;
		thread = new Thread(this, "terrain" + u + ":" + v);
		thread.start();
	}
	
	public Thread getThread() {
		return thread;
	}
	
	public Terrain loadTerrain(Loader loader) {
		return new Terrain(heights, foliage, loader.mdls().loadModel(new float[][] {vertices, normals, textureCoords, colors, tangents, binormals}, new int[] {3, 3, 2, 3, 2, 2}, indices), new WaterTile(loader, u, v), u, v);
	}

	@Override
	public void run() {
		TerrainGenerator generator = new TerrainGenerator(u, v);
		
		this.heights = new float[Const.TERRAIN_GRID_RES + 2][Const.TERRAIN_GRID_RES + 2];
		for(int i = 0; i < Const.TERRAIN_GRID_RES + 2; i++) {
			float tu = ((float)(Const.TERRAIN_GRID_SIZE * (i))) / ((float)(Const.TERRAIN_GRID_RES - 1));
			for(int j = 0; j < Const.TERRAIN_GRID_RES + 2; j++) {
				float tv = ((float)(Const.TERRAIN_GRID_SIZE * (j))) / ((float)(Const.TERRAIN_GRID_RES - 1));
				this.heights[i][j] = generator.generateHeight(tu, tv);
			}
		}
		
		float r, g, b;
		int r2Pointer = 0;
		int r3Pointer = 0;
		Vec3f normal, tangent, binormal;
		this.colors = new float[3 * VERTEX_COUNT];
		this.normals = new float[3 * VERTEX_COUNT];
		this.tangents = new float[3 * VERTEX_COUNT];
		this.binormals = new float[3 * VERTEX_COUNT];
		this.vertices = new float[this.normals.length];
		this.textureCoords = new float[2 * VERTEX_COUNT];
		this.normalsArray = new float[Const.TERRAIN_GRID_RES][Const.TERRAIN_GRID_RES];
		for(int i = 0; i < Const.TERRAIN_GRID_RES; i++) {
			float tu = ((float) i) / ((float)(Const.TERRAIN_GRID_RES - 1));
			for(int j = 0; j < Const.TERRAIN_GRID_RES; j++) {
				float tv = ((float) j) / ((float)(Const.TERRAIN_GRID_RES - 1));
				this.textureCoords[r2Pointer] = tu * Const.TERRAIN_TEXTURE_TILE_FACTOR;
				this.textureCoords[r2Pointer + 1] = tv * Const.TERRAIN_TEXTURE_TILE_FACTOR;
				
				this.vertices[r3Pointer] = (tu + this.u)*Const.TERRAIN_GRID_SIZE;
				this.vertices[r3Pointer + 1] = heights[i + 1][j + 1];
				this.vertices[r3Pointer + 2] = (tv + this.v)*Const.TERRAIN_GRID_SIZE;
				
				normal = new Vec3f(heights[i][j+1] - heights[i+2][j+1], 1.0f, heights[i+1][j] - heights[i+1][j+2]);
				
				tangent = new Vec3f(Const.TERRAIN_PRIMTIVE_LENGTH, normal.x, 0.0f).getNormalized();
				this.tangents[r2Pointer]     = tangent.x;
				this.tangents[r2Pointer + 1] = tangent.y;
				
				binormal = new Vec3f(0.0f, normal.z, Const.TERRAIN_PRIMTIVE_LENGTH).getNormalized();
				this.tangents[r2Pointer]     = binormal.y;
				this.tangents[r2Pointer + 1] = binormal.z;
				
				normal.normalize();
				this.normalsArray[i][j] = normal.y;
				this.normals[r3Pointer]     = normal.x;
				this.normals[r3Pointer + 1] = normal.y;
				this.normals[r3Pointer + 2] = normal.z;
				
				
				
				r = 0.0f;
				if(this.normalsArray[i][j] > Const.GENERATOR_SHALLOW_BLEND_LOW) {
					r = 1.0f;
					if(this.normalsArray[i][j] < Const.GENERATOR_SHALLOW_BLEND_HIGH) {
						r *= (this.normalsArray[i][j] - Const.GENERATOR_SHALLOW_BLEND_LOW) / SHALLOW_BLEND_DELTA;
					}
				}
				g = 0.0f;
				if(this.heights[i][j] < WATER_BLEND_HIGH) {
					g = 1.0f;
					if(this.heights[i][j] > WATER_BLEND_LOW) {
						g *= (this.heights[i][j] - WATER_BLEND_LOW) / Const.GENERATOR_WATER_BLEND_DELTA;
					}
				}
				b = 0.0f;
				if(this.normalsArray[i][j] < Const.GENERATOR_STEEP_BLEND_HIGH) {
					b = 1.0f;
					if(this.normalsArray[i][j] > Const.GENERATOR_STEEP_BLEND_LOW) {
						b *= (this.normalsArray[i][j] - Const.GENERATOR_STEEP_BLEND_LOW) / STEEP_BLEND_DELTA;
					}
				}
				colors[r3Pointer]     = r;
				colors[r3Pointer + 1] = g;
				colors[r3Pointer + 2] = b;
				
				r3Pointer += 3;
				r2Pointer += 2;
			}
		}
		
		r3Pointer = 0;
		this.indices = new int[6 * (VERTEX_COUNT - (2 * Const.TERRAIN_GRID_RES) + 1)];
		for(int i = 0; i < Const.TERRAIN_GRID_RES - 1; i++) {
			for(int j = 0; j < Const.TERRAIN_GRID_RES - 1; j++) {
				int tl = (i * Const.TERRAIN_GRID_RES) + j;
				int tr = tl + 1;
				int bl = ((i + 1) * Const.TERRAIN_GRID_RES) + j;
				int br = bl + 1;
				this.indices[r3Pointer++] = tr;
				this.indices[r3Pointer++] = bl;
				this.indices[r3Pointer++] = tl;
				this.indices[r3Pointer++] = br;
				this.indices[r3Pointer++] = bl;
				this.indices[r3Pointer++] = tr;
			}
		}
		
		float height;
		float scalar;
		Vec2f location;
		float rotation;
		for(TransparentModel model:foliageModels) {
			for(int i = 0; i < 5; i++ ) {
				location = generator.getFoliageLocation();
				height = Terrain.getTerrainHeight(location.x, location.y, heights);
				if(height > Const.WATER_LEVEL) {
					scalar = generator.getFoliageScale();
					rotation = generator.getFoliageRotation();
					foliage.add(new StaticEntity(model).givePosition(new Vec3f(location.x, height, location.y)).giveRotation(new Vec3f(0.0f, rotation, 0.0f)).finalizeModel());
				}
			}
		}
	}
}
