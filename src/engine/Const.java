package engine;

public class Const {
	
	/*
	 * MATH constants
	 */
	public static final float	MATH_PI = (float) Math.PI;

	/*
	 * WINDOW constants
	 */
	public static final int		WINDOW_WIDTH = 1280,
								WINDOW_HEIGHT = 720,
								WINDOW_REFRESH = 60,
								WINDOW_DEPTH_BITS = 24,
								WINDOW_GL_VSYNC = 1,
								WINDOW_GL_MAJOR = 4,
								WINDOW_GL_MINOR = 1;

	/*
	 * RENDER constants
	 */
	public static final float 	RENDER_COLOR_R = 0.2f,
			  					RENDER_COLOR_G = 0.3f,
			  					RENDER_COLOR_B = 0.4f,
			  					RENDER_COLOR_A = 1.0f,
			  					RENDER_AMBIENT_LIGHT = 0.25f,
			  					RENDER_FOV = MATH_PI / 6.0f,
			  					RENDER_NEAR_PLANE = 0.1f,
			  					RENDER_FAR_PLANE = 340.0f,
			  					RENDER_FOG_DELTA = 30.0f,
			  					RENDER_FOG_NEAR = RENDER_FAR_PLANE - RENDER_FOG_DELTA;

	/*
	 * CAMERA constants
	 */
	public static final float	CAMERA_RADIUS = 15.0f,
								CAMERA_PITCH = - MATH_PI / 4.0f,
								CAMERA_YAW = MATH_PI,
								CAMERA_ORBIT_SPEED = 0.01f,
								CAMERA_ZOOM_SPEED = 0.1f,
								CAMERA_VERTICAL_OFFSET = 10.0f;

	/*
	 * PLAYER constants
	 */
	public static final float 	PLAYER_MOVE_SPEED = 1.0f,
								PLAYER_ROTATION_SPEED = 0.1f;

	/*
	 * TERRAIN constants
	 */
	public static final int 	TERRAIN_GRID_RES = 32;
	public static final float 	TERRAIN_GRID_SIZE = 45.0f,
			  					TERRAIN_TEXTURE_TILE_FACTOR = 4.0f,
			  					TERRAIN_NORMAL_MAPPING_DAMPER = 1.5f,
			  					TERRAIN_PRIMTIVE_LENGTH = TERRAIN_GRID_SIZE / ((float)TERRAIN_GRID_RES);

	/*
	 * GENERATOR constants
	 */
	public static final float 	GENERATOR_AMPLITUDE = 32.5f,
			  					GENERATOR_ROUGHNESS = 0.40f,
			  					GENERATOR_STEEP_BLEND_HIGH = 0.625f,
			  					GENERATOR_STEEP_BLEND_LOW = 0.525f,
			  					GENERATOR_SHALLOW_BLEND_HIGH = 0.825f,
			  					GENERATOR_SHALLOW_BLEND_LOW = 0.775f,
			  					GENERATOR_WATER_BLEND_OFFSET = 1.0f,
			  					GENERATOR_WATER_BLEND_DELTA = 1.0f;
	public static final int 	GENERATOR_OCTAVES = 5;

	/*
	 * WATER constants
	 */
	public static final float	WATER_LEVEL = -1.5f,
								WATER_RIPPLE_SPEED = 0.075f,
								WATER_BLEND_DEPTH = 12.5f,
								WATER_RIPPLE_STENGTH = 0.05f,
								WATER_FRESNEL_DAMPER = 0.6f,
								WATER_REFLECTIVITY = 1.5f,
								WATER_SPEC_DAMPER = 17.5f;
	public static final int		WATER_REFRACTION_BUFFER_WIDTH = 1280,
								WATER_REFRACTION_BUFFER_HEIGHT = 720,
								WATER_REFLECTION_BUFFER_WIDTH = 640,
								WATER_REFLECTION_BUFFER_HEIGHT = 360;

	/*
	 * WORLD constants
	 */
	public static final int 	WORLD_LOAD_DISTANCE = 7,
								WORLD_DROP_DISTANCE = 8;
	public static final long 	WORLD_UPDATE_PAUSE = 250;
}
