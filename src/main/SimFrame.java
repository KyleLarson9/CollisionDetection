package main;

import java.awt.Color;

import javax.swing.JFrame;

public class SimFrame extends JFrame {

	public SimFrame(SimPanel panel) {
		this.add(panel); 
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(false);
		this.setBackground(Color.LIGHT_GRAY);
		this.pack();
		this.setLocationRelativeTo(null);
		this.setVisible(true);
	}
	
}
