package com.pathtracer;

import java.awt.image.BufferedImage;

import javax.swing.JFrame;

/*
 * Live preview JFrame. Contains LivePreviewPanel.
 */
public class LivePreviewFrame extends JFrame {

	public LivePreviewFrame(BufferedImage image, int scale) {
		super();
		
		this.add(new LivePreviewPanel(image, scale));
		this.setResizable(false);
		this.pack();
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
}
