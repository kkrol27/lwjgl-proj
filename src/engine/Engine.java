package engine;

import glfw.GLFWHandle;
import loaders.Loader;
import rendering.MainRenderer;
import universe.Universe;

public class Engine {

	public static void main(String[] args) {
		Engine engine = null;
		try {
			engine = new Engine();
			engine.run();
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			if(engine != null) {
				engine.dispose();
			}
		}
	}
	
	private MainRenderer mainRenderer;
	private Universe universe;
	private Loader loader;
	
	private Engine() {
		GLFWHandle.init();
		
		loader = new Loader();
		loader.setPath("/mdl/");
		loader.txts().setPath("/txt/");
		loader.mdls().setPath("/obj/");
		
		universe = new Universe(loader);
		
		mainRenderer = new MainRenderer(universe);
	}
	
	private void run() {
		do {
			universe.update(loader);
			mainRenderer.render(universe);
		} while(!GLFWHandle.update());
	}
	
	private void dispose() {
		universe.dispose();
		mainRenderer.dispose();
		loader.dispose();
		GLFWHandle.dispose();
	}
}
