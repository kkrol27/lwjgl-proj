package glfw;

import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;

import static org.lwjgl.glfw.GLFW.*;

import static engine.Const.*;

public class GLFWHandle {

	private static long window;
	private static long lastFrameTime;
	private static long lastFrameDelta;
	private static double mouseX;
	private static double mouseY;
	private static double dx;
	private static double dy;
	private static int[] width;
	private static int[] height;
	private static double[] x;
	private static double[] y;
	
	public static void init() {
		if(!glfwInit()) {
			throw new IllegalStateException("Couldn't initialize glfw");
		}
		
		glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, WINDOW_GL_MAJOR);
		glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, WINDOW_GL_MINOR);
		glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
		glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GLFW_TRUE);
		glfwWindowHint(GLFW_DEPTH_BITS, WINDOW_DEPTH_BITS);
		glfwWindowHint(GLFW_REFRESH_RATE, WINDOW_REFRESH);
		glfwWindowHint(GLFW_RESIZABLE, GLFW_FALSE);
		glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
		
		//glfwWindowHint(GLFW_COCOA_RETINA_FRAMEBUFFER, GLFW_TRUE);
		
		window = glfwCreateWindow(WINDOW_WIDTH, WINDOW_HEIGHT, "LWJGL GLFW Window", 0, 0);
		if(window == 0) {
			throw new IllegalStateException("Couldn't create window");
		}
		
		GLFWVidMode videoMode = glfwGetVideoMode(glfwGetPrimaryMonitor());
		glfwSetWindowPos(window, (videoMode.width() - WINDOW_WIDTH) / 2, (videoMode.height() - WINDOW_HEIGHT) / 2);
		
		glfwMakeContextCurrent(window);
		glfwSwapInterval(WINDOW_GL_VSYNC);
		GL.createCapabilities(true);
		glfwShowWindow(window);
		glfwPollEvents();
		
		lastFrameTime = System.currentTimeMillis();
		lastFrameDelta = 1000l / WINDOW_REFRESH;
		
		x = new double[1];
		y = new double[1];
		glfwGetCursorPos(window, x, y);
		mouseX = x[0];
		mouseY = y[0];
		dx = 0.0d;
		dy = 0.0d;
		
		width = new int[1];
		height = new int[1];
		glfwGetFramebufferSize(window, width, height);
	}
	
	public static void dispose() {
		glfwTerminate();
	}
	
	public static boolean update() {
		glfwSwapBuffers(window);
		glfwPollEvents();
		updateMouse();
		
		lastFrameDelta = System.currentTimeMillis() - lastFrameTime;
		lastFrameTime = System.currentTimeMillis();
		
		return glfwWindowShouldClose(window);
	}
	
	public static long getLastFrameDelta() {
		return lastFrameDelta;
	}
	
	private static void updateMouse() {
		glfwGetCursorPos(window, x, y);
		dx = x[0] - mouseX;
		dy = y[0] - mouseY;
		mouseX = x[0];
		mouseY = y[0];
	}
	
	public static int getFrameWidth() {
		return width[0];
	}
	
	public static int getFrameHeight() {
		return height[0];
	}
	
	public static boolean keyIsDown(int GLFW_KEY) {
		return glfwGetKey(window, GLFW_KEY) == GLFW_PRESS;
	}
	
	public static boolean mouseButtonIsDown(int GLFW_BUTTON) {
		return glfwGetMouseButton(window, GLFW_BUTTON) == GLFW_PRESS;
	}
	
	public static float getMouseX() {
		return (float) mouseX;
	}
	
	public static float getMouseY() {
		return (float) mouseY;
	}
	
	public static float getMouseDX() {
		return (float) dx;
	}
	
	public static float getMouseDY() {
		return (float) dy;
	}
}
