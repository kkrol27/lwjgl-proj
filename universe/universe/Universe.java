package universe;

import entities.Player;
import loaders.Loader;
import terrain.TerrainGenerator;

public class Universe implements Runnable {
	
	private volatile boolean shouldQuit;
	private Thread secondaryThread;
	
	private Environment environment;
	private Player player;
	private World world;
	
	public Universe(Loader loader) {
		
		player = new Player(loader.loadTexturedModel("player.mdl"));
		environment = new Environment(loader);
		world = new World(loader);
		
		TerrainGenerator.init();
		this.shouldQuit = false;
		secondaryThread =  new Thread(this, "secondaryThread");
		secondaryThread.start();	
	}
	
	public void update(Loader loader) {
		player.update(world);
		world.primaryUpdate(loader);
		environment.primaryUpdate();
	}
	
	public Environment getEnvironment() {
		return environment;
	}
	
	public Player getPlayer() {
		return player;
	}
	
	public World getWorld() {
		return world;
	}
	
	public void dispose() {
		shouldQuit = true;
		try {
			secondaryThread.join();
		} catch(InterruptedException e) {
			e.printStackTrace();
		}
		world.dispose();
		environment.dipose();
	}

	@Override
	public void run() {
		do {
			world.secondaryUpdate(player);
			try {
				Thread.sleep(500);
			} catch(InterruptedException e) {
				
			}
		} while(!shouldQuit);
	}
}
