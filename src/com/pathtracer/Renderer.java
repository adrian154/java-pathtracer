package com.pathtracer;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Renderer {

	public static int numThreads = Runtime.getRuntime().availableProcessors();
	public static int finishedThreads;
	public static Output output;
	public static ExecutorService executorService;
	
	public static long startTime;
	
	/*
	 * Multithreaded render core.
	 */
	public static void startRender(Camera camera, Scene scene, Output output) {
		
		System.out.println("Rendering with settings primary-rays=" + Pathtracer.NUM_PRIMARY_RAYS + ", secondary-rays=" + Pathtracer.NUM_SECONDARY_RAYS + ", threads=" + Renderer.numThreads);
		
		/* Start timing */
		startTime = System.currentTimeMillis();
		
		/* Set up renderer fields */
		Renderer.finishedThreads = 0;
		Renderer.output = output;
		
		/* Set up thread pool. */
		executorService = Executors.newFixedThreadPool(numThreads);
		
		/* Divide work between threads. */
		int linesPerCore = output.width / numThreads;
		
		for(int i = 0 ; i < numThreads; i++) {
			int start = linesPerCore * i;
			int end = linesPerCore * (i + 1);
	
			executorService.execute(new RenderTask(camera, scene, output, start, end, i));
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
