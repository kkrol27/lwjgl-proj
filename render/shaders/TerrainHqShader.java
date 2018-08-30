package shaders;

public class TerrainHqShader extends TerrainLqShader {

	private static final String file = "/source/terrain_hq";
	
	private int normals_n;
	private int normals_r;
	private int normals_g;
	private int normals_b;
	
	public TerrainHqShader() {
		super(file);
	}
	
	@Override
	protected void getAllUniformLocations() {
		super.getAllUniformLocations();
		
		normals_n = super.getUniformVariableLocation("normals_n");
		normals_r = super.getUniformVariableLocation("normals_r");
		normals_g = super.getUniformVariableLocation("normals_g");
		normals_b = super.getUniformVariableLocation("normals_b");
	}
	
	@Override
	public void loadTexturesAndConstants() {
		super.loadTexturesAndConstants();
		super.loadInt(normals_n, 4);
		super.loadInt(normals_r, 5);
		super.loadInt(normals_g, 6);
		super.loadInt(normals_b, 7);
	}
	
	@Override
	public void dispose() {
		super.dispose();
	}
}
