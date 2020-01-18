package com.pathtracer;

import javax.swing.JFrame;

public class LivePreviewFrame extends JFrame {

	public LivePreviewFrame() {
		super();
		
		this.add(new LivePreviewPanel());
		this.setResizable(false);
		this.pack();
	}
	
}
