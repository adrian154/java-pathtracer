package com.pathtracer;

public class RenderTask implements Runnable {
	
	public Renderer renderer;
	public int start;
	public int end;
	
	public RenderTask(Renderer renderer, int start, int end) {
		this.renderer = renderer;
		this.start = start;
		this.end = end;
	}
	
	public void run() {

		renderer.pathtracer.renderSection(renderer.output, start, end);
		
		renderer.finishedThreads++;
		renderer.checkFinish();
		
	}
	
}
