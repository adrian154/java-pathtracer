package com.pathtracer;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;
import javax.swing.Timer;

public class LivePreviewPanel extends JPanel implements ActionListener {

	public Timer timer;
	
	public LivePreviewPanel() {
		timer = new Timer(1 / 60, this);
		timer.start();
		this.setPreferredSize(new Dimension(Main.output.image.getWidth(), Main.output.image.getHeight()));
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(Main.output.image, 0, 0, this);
	}
	
	@Override
	public void actionPerformed(ActionEvent event) {
		repaint();	
	}
	
}
