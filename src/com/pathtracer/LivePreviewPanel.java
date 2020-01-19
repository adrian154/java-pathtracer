package com.pathtracer;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;
import javax.swing.Timer;

/*
 * Live preview panel. Draws image as it is being rendered.
 */
public class LivePreviewPanel extends JPanel implements ActionListener {

	public Timer timer;
	public BufferedImage image;
	public int outputScale;
	
	public LivePreviewPanel(BufferedImage image, int outputScale) {
		this.image = image;
		this.outputScale = outputScale;
		
		timer = new Timer(1 / 60, this);
		timer.start();
		this.setPreferredSize(new Dimension(image.getWidth() * outputScale, image.getHeight() * outputScale));
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(image.getScaledInstance(image.getWidth() * outputScale, image.getHeight() * outputScale, Image.SCALE_FAST), 0, 0, this);
	}
	
	@Override
	public void actionPerformed(ActionEvent event) {
		repaint();	
	}
	
}
