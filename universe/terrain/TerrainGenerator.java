package terrain;

import java.util.Random;

import engine.Const;
import maths.Vec2f;

public class TerrainGenerator {
	
	private static long seed;
	private static long scalarA;
	private static long scalarB;
	
	public static void init() {
		Random rand = new Random();
		seed = rand.nextLong();
		completeInit(rand);
	}
	
	public static void init(long seed) {
		Random rand = new Random();
		TerrainGenerator.seed = seed;
		completeInit(rand);
	}
	
	public static void completeInit(Random rand) {
		rand.setSeed(seed);
		scalarA = 100000 + (rand.nextLong() % 900000);
		scalarB = 100000 + (rand.nextLong() % 900000);
	}
	
	private Random random;
	private float du;
	private float dv;
	
	public TerrainGenerator(float du, float dv) {
		this.random = new Random();
		this.du = du * Const.TERRAIN_GRID_SIZE;
		this.dv = dv * Const.TERRAIN_GRID_SIZE;
	}
	
	public float generateHeight(float u, float v) {
		float total = 0;
		float size = Const.TERRAIN_GRID_SIZE;
		float amplitude = Const.GENERATOR_AMPLITUDE;
		for(int i = 0; i < Const.GENERATOR_OCTAVES; i++) {
			total +=  amplitude * getInterpolatedNoise(u, v, size);
			amplitude *= Const.GENERATOR_ROUGHNESS;
			size /= 2;
		}
		return total;
	}
	
	private float getInterpolatedNoise(float u, float v, float ds){
		u /= ds;
		v /= ds;
        int iU = (int) Math.floor(u);
        int iV = (int) Math.floor(v);
        float fU = u - iU;
        float fV = v - iV;
        float v1 = getSmoothNoise(iU, iV, ds);
        float v2 = getSmoothNoise(iU + 1, iV, ds);
        float v3 = getSmoothNoise(iU, iV + 1, ds);
        float v4 = getSmoothNoise(iU + 1, iV + 1, ds);
        float i1 = interpolate(v1, v2, fU);
        float i2 = interpolate(v3, v4, fU);
        return interpolate(i1, i2, fV);
    }
	
	private float interpolate(float a, float b, float blend){
        double theta = blend * Math.PI;
        float f = (float)(1.0f - Math.cos(theta)) * 0.5f;
        return a * (1f - f) + b * f;
    }
	
	private float getSmoothNoise(int u, int v, float ds) {
		float c = (getNoise(u - 1, v - 1, ds) + getNoise(u + 1, v - 1, ds) + getNoise(u - 1, v + 1, ds) + getNoise(u + 1, v + 1, ds)) / 16f;
        float s = (getNoise(u - 1, v, ds) + getNoise(u + 1, v, ds) + getNoise(u, v - 1, ds) + getNoise(u, v + 1, ds)) / 8f;
        float m = (getNoise(u, v, ds)) / 4f;
        return c + s + m;
	}
	
	private float getNoise(int u, int v, float ds) {
		random.setSeed((long)(scalarA * ((long)(du + ds * u)) + scalarB * ((long)(dv + ds * v)) + seed));
		int[] k = new int[] {
				random.nextInt(10000),
				random.nextInt(10000),
				random.nextInt(10000),
				random.nextInt(10000)
		};
		float[] x = new float[] {
				random.nextFloat() + 0.5f,
				random.nextFloat() + 0.5f,
				random.nextFloat() + 0.5f
		};
		float rand = (k[0] + k[1]*x[0] + k[2]*x[1]*x[1] + k[3]*x[2]*x[2]*x[2]) % 1.0f;
		return (2.0f * rand) - 1.0f;
	}
	
	public Vec2f getFoliageLocation() {
		float u = Const.TERRAIN_GRID_SIZE * random.nextFloat();
		float v = Const.TERRAIN_GRID_SIZE * random.nextFloat();
		return new Vec2f(u, v);
	}
	
	public float getFoliageRotation() {
		return 2.0f * Const.MATH_PI * random.nextFloat();
	}
	
	public float getFoliageScale() {
		return random.nextFloat() + 1.0f;
	}
}
