package universe;

import engine.Const;
import environment.Sky;
import environment.Sun;
import environment.Water;
import glfw.GLFWHandle;
import loaders.Loader;

public class Environment {

	private long currentTime;
	private long deltaTime;
	
	private Sky sky;
	private Sun sun;
	private Water water;
	
	public Environment(Loader loader) {
		currentTime = 0l;
		deltaTime = 1000l / Const.WINDOW_REFRESH;
		
		sky = new Sky(loader);
		sun = new Sun(loader);
		water = new Water(loader);
	}
	
	public void primaryUpdate() {
		currentTime += (deltaTime = GLFWHandle.getLastFrameDelta());
		water.primaryUpdate(getFrameTime());
	}
	
	public float getFrameTime() {
		return ((float) deltaTime) / 1000.0f;
	}
	
	public long getCurrentTime() {
		return currentTime;
	}
	
	public Sky getSky() {
		return sky;
	}
	
	public Sun getSun() {
		return sun;
	}
	
	public Water getWater() {
		return water;
	}
	
	public void dipose() {
		water.dispose();
	}
}
