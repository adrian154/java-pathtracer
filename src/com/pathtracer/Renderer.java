package com.pathtracer;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Renderer {

	public int numThreads;
	public int finishedThreads;
	public ExecutorService executorService;
	
	public Pathtracer pathtracer;
	public Output output;
	
	public static long startTime;
	
	public Renderer(Pathtracer pathtracer, Output output) {
	
		this.pathtracer = pathtracer;
		this.output = output;
		
		this.numThreads = Runtime.getRuntime().availableProcessors();
		
	}
	
	public void startRender() {
	
		System.out.println("Rendering with settings primary-rays=" + this.pathtracer.numPrimaryRays + ", secondary-rays=" + this.pathtracer.numSecondaryRays + ", threads=" + this.numThreads);
		
		this.finishedThreads = 0;
		executorService = Executors.newFixedThreadPool(numThreads);
		
		int linesPerCore = output.width / numThreads;
		
		startTime = System.currentTimeMillis();
		
		for(int i = 0; i < numThreads; i++) {
			int start = linesPerCore * i;
			int end = linesPerCore * (i + 1);
			
			executorService.execute(new RenderTask(this, start, end));
		}
		
	}
	
	/*
	 * If all the threads are done, write result to image.
	 */
	public void checkFinish() {
		
		/* Check if all threads are done */
		if(finishedThreads == numThreads) {
			
			/* Write to file */
			if(pathtracer.progressive) {
				output.writeToFile(pathtracer.samples + ".png");
			}
			
			/* Check timing */
			long elapsed = System.currentTimeMillis() - startTime;
			System.out.println("Finished in " + elapsed + " milliseconds.");
			
			/* Shut down executor service */
			if(pathtracer.progressive) {
				this.startRender();
				pathtracer.samples++;
			} else {
				executorService.shutdown();
			}
			
		}
		
	}
	
}
