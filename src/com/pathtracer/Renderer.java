package com.pathtracer;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Renderer {

	public static int finishedThreads;
	public static Output output;
	
	/*
	 * Multithreaded render core.
	 */
	public static void render(Camera camera, Scene scene, Output output) {
		
		Renderer.finishedThreads = 0;
		Renderer.output = output;
		
		int numCPUCores = Runtime.getRuntime().availableProcessors();
		ExecutorService executorService = Executors.newFixedThreadPool(numCPUCores);
		
		int linesPerCore = output.width / numCPUCores;
		
		for(int i = 0 ; i < numCPUCores; i++) {
			int start = linesPerCore * i;
			int end = linesPerCore * (i + 1);
			
			System.out.println("start: " + start + ", end: " + end);
			
			executorService.execute(new Runnable() {

				@Override
				public void run() {
					Pathtracer.render(camera,  scene, output, start, end);
					Renderer.finishedThreads++;
					Renderer.checkFinish();
				}
				
			});
		}
		
	}
	
	public static void checkFinish() {
		if(finishedThreads == Runtime.getRuntime().availableProcessors()) {
			output.writeToFile("output_mt.png");
		}
	}
}
