package loaders;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import gl.VertexArray;
import gl.VertexBuffer;
import maths.Vec2f;
import maths.Vec3f;
import models.Model;

public class ModelHandler {
	
	private class SimpleModel {
		
		private int vertexCount;
		private VertexArray vertexArray;
		private VertexBuffer[] vertexBuffers;
		
		public SimpleModel(int vertexCount, VertexArray vertexArray, VertexBuffer[] vertexBuffers) {
			this.vertexCount = vertexCount;
			this.vertexArray = vertexArray;
			this.vertexBuffers = vertexBuffers;
		}

		public int getVertexCount() {
			return vertexCount;
		}

		public VertexArray getVertexArray() {
			return vertexArray;
		}

		public VertexBuffer[] getVertexBuffers() {
			return vertexBuffers;
		}
	}

	private Loader loader;
	private String path;
	
	public ModelHandler(Loader loader) {
		this.loader = loader;
	}
	
	public void setPath(String path) {
		this.path = path;
	}
	
	private SimpleModel loadSimpleModel(float[][] data, int[] dimensions) {
		VertexBuffer[] vertexBuffers = new VertexBuffer[data.length];
		VertexArray vertexArray = loader.vaos().generateVertexArray();
		
		for(int i = 0; i < data.length; i++) {
			vertexBuffers[i] = loader.vbos().storeDataInAttributeList(i, dimensions[i], data[i]);
		}
		loader.vaos().unbindVertexArray();
		
		return new SimpleModel(data[0].length / dimensions[0], vertexArray, vertexBuffers);
	}
	
	public Model loadModel(float[][] data, int[] dimensions) {
		SimpleModel simpleModel = loadSimpleModel(data, dimensions);
		return new Model(new int[] {simpleModel.getVertexCount()}, new VertexArray[] {simpleModel.getVertexArray()}, new VertexBuffer[][] {simpleModel.getVertexBuffers()});
	}
	
	private SimpleModel loadSimpleModel(float[][] data, int[] dimensions, int[] indices) {
		VertexBuffer[] vertexBuffers = new VertexBuffer[data.length + 1];
		VertexArray vertexArray = loader.vaos().generateVertexArray();
		
		vertexBuffers[data.length] = loader.vbos().storeIndexArray(indices);
		for(int i = 0; i < data.length; i++) {
			vertexBuffers[i] = loader.vbos().storeDataInAttributeList(i, dimensions[i], data[i]);
		}
		loader.vaos().unbindVertexArray();
		
		return new SimpleModel(indices.length, vertexArray, vertexBuffers);
	}
	
	public Model loadModel(float[][] data, int[] dimensions, int[] indices) {
		SimpleModel simpleModel = this.loadSimpleModel(data, dimensions, indices);
		return new Model(new int[] {simpleModel.getVertexCount()}, new VertexArray[] {simpleModel.getVertexArray()}, new VertexBuffer[][] {simpleModel.getVertexBuffers()});
	}
	
	private void processVertex(String[] vertex, List<Integer> indices, List<Vec2f> textures, List<Vec3f> normals, float[] textureArray, float[] normalArray) {
		int currentVertexPointer = Integer.parseInt(vertex[0]) - 1;
		indices.add(currentVertexPointer);
		
		Vec2f currentTexture = textures.get(Integer.parseInt(vertex[1]) - 1);
		textureArray[currentVertexPointer * 2] = currentTexture.x;
		textureArray[currentVertexPointer * 2 + 1] = 1 - currentTexture.y;
		
		Vec3f currentNorm = normals.get(Integer.parseInt(vertex[2]) - 1);
		normalArray[currentVertexPointer * 3] = currentNorm.x;
		normalArray[currentVertexPointer * 3 + 1] = currentNorm.y;
		normalArray[currentVertexPointer * 3 + 2] = currentNorm.z;
	}
	
	private SimpleModel processObjPortion(BufferedReader reader, List<Vec3f> vertices, List<Vec2f> textures, List<Vec3f> normals,  String line) throws IOException {
		List<Integer> indices = new ArrayList<Integer>();
		
		float[] verticesArray;
		float[] texturesArray = null;
		float[] normalsArray = null;
		int[] indicesArray;
		
		String[] lineSplit = null;
		do {
			lineSplit = line.split(" ");
			switch(lineSplit[0]) {
				case "v":
					vertices.add(new Vec3f(Float.parseFloat(lineSplit[1]), Float.parseFloat(lineSplit[2]), Float.parseFloat(lineSplit[3])));
					break;
				case "vt":
					textures.add(new Vec2f(Float.parseFloat(lineSplit[1]), Float.parseFloat(lineSplit[2])));
					break;
				case "vn":
					normals.add(new Vec3f(Float.parseFloat(lineSplit[1]), Float.parseFloat(lineSplit[2]), Float.parseFloat(lineSplit[3])));
					break;
			}
		} while(!(line = reader.readLine()).startsWith("f "));
		texturesArray = new float[vertices.size() * 2];
		normalsArray = new float[vertices.size() * 3];
		
		boolean flag = true;
		String[][] stringVertex = new String[3][];
		do {
			lineSplit = line.split(" ");
			stringVertex[0] = lineSplit[1].split("/");
			stringVertex[1] = lineSplit[2].split("/");
			stringVertex[2] = lineSplit[3].split("/");
			processVertex(stringVertex[0], indices, textures, normals, texturesArray, normalsArray);
			processVertex(stringVertex[1], indices, textures, normals, texturesArray, normalsArray);
			processVertex(stringVertex[2], indices, textures, normals, texturesArray, normalsArray);
			
			line = reader.readLine();
			if((line == null) || line.startsWith("o ")) {
				flag = false;
			}
		} while(flag);
		
		int vertexPointer = 0;
		indicesArray = new int[indices.size()];
		verticesArray = new float[vertices.size() * 3];		
		for(Vec3f vertex:vertices) {
			verticesArray[vertexPointer++] = vertex.x;
			verticesArray[vertexPointer++] = vertex.y;
			verticesArray[vertexPointer++] = vertex.z;
		}
		for(int i = 0; i < indices.size(); i++) {
			indicesArray[i] = indices.get(i);
		}
		
		return loadSimpleModel(new float[][] {verticesArray, normalsArray, texturesArray}, new int[] {3, 3, 2}, indicesArray);
	}
	
	public Model loadObjModel(String file) {
		List<Vec3f> vertices = new ArrayList<Vec3f>();
		List<Vec2f> textures = new ArrayList<Vec2f>();
		List<Vec3f> normals = new ArrayList<Vec3f>();
		
		String line = null;
		BufferedReader reader = null;
		List<SimpleModel> simpleModels = new ArrayList<SimpleModel>();
		try {
			InputStream in = this.getClass().getResourceAsStream(path + file);
			reader = new BufferedReader(new InputStreamReader(in));
			
			while((line = reader.readLine()) != null) {
				simpleModels.add(processObjPortion(reader, vertices, textures, normals, line));
			}
		} catch(IOException e) {
			e.printStackTrace();
		} finally {
			if(reader != null) {
				try {
					reader.close();
				} catch(IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		SimpleModel simpleModel = null;
		int[] vertexCounts = new int[simpleModels.size()];
		VertexArray[] vertexArrays = new VertexArray[simpleModels.size()];
		VertexBuffer[][] vertexBuffers = new VertexBuffer[simpleModels.size()][];
		for(int i = 0; i < simpleModels.size(); i++) {
			simpleModel = simpleModels.get(i);
			vertexCounts[i] = simpleModel.getVertexCount();
			vertexArrays[i] = simpleModel.getVertexArray();
			vertexBuffers[i] = simpleModel.getVertexBuffers();
		}
		
		return new Model(vertexCounts, vertexArrays, vertexBuffers);
	}
}
