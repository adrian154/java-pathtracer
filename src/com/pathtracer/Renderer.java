package com.pathtracer;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Renderer {

	public static int numThreads;
	public static int finishedThreads;
	public static Output output;
	public static ExecutorService executorService;
	
	public static long startTime;
	
	/*
	 * Multithreaded render core.
	 */
	public static void render(Camera camera, Scene scene, Output output) {
		
		/* Start timing */
		startTime = System.currentTimeMillis();
		
		/* Set up renderer fields */
		Renderer.numThreads = Runtime.getRuntime().availableProcessors();
		Renderer.finishedThreads = 0;
		Renderer.output = output;
		
		/* Set up thread pool. */
		executorService = Executors.newFixedThreadPool(numThreads);
		
		/* Divide work between threads. */
		int linesPerCore = output.width / numThreads;
		
		for(int i = 0 ; i < numThreads; i++) {
			int start = linesPerCore * i;
			int end = linesPerCore * (i + 1);
	
			executorService.execute(new Runnable() {

				@Override
				public void run() {
					
					/* Render assigned section */
					Pathtracer.renderSection(camera,  scene, output, start, end);
					
					/* Check if all other threads are finished. */
					Renderer.finishedThreads++;
					Renderer.checkFinish();
					
				}
				
			});
		}
		
	}
	
	/*
	 * If all the threads are done, write result to image.
	 */
	public static void checkFinish() {
		
		/* Check if all threads are done */
		if(finishedThreads == numThreads) {
			
			/* Write to file */
			output.writeToFile("output.png");
			
			/* Check timing */
			long elapsed = System.currentTimeMillis() - startTime;
			System.out.println("------------------- FINISHED IN " + elapsed + " MILLISECONDS -------------------");

			/* Shut down executor service */
			Renderer.executorService.shutdown();
			
		}
	}
	
}
