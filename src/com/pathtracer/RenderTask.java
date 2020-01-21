package com.pathtracer;

public class RenderTask implements Runnable {
	
	public Camera camera;
	public Scene scene;
	public Output output;
	public int start;
	public int end;
	public int threadID;
	
	public RenderTask(Camera camera, Scene scene, Output output, int start, int end, int threadID) {
		this.camera = camera;
		this.scene = scene;
		this.output = output;
		this.start = start;
		this.end = end;
		this.threadID = threadID;
	}
	
	public void run() {
		
		System.out.println("Started render task on thread " + threadID);
		Pathtracer.renderSection(camera, scene, output, start, end);
		System.out.println("Finished render task on thread " + threadID);
		
		Renderer.finishedThreads++;
		Renderer.checkFinish();
		
	}
	
}
