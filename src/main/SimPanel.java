package main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

public class SimPanel extends JPanel {

	private Sim sim;
	private Dimension size;
	
	public SimPanel(Sim sim) {
		this.sim = sim;
		
		setPanelSize();
		this.setBackground(Color.DARK_GRAY);
	}
	
	// public methods
	
	public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g; // Cast to Graphics2D
        sim.render(g2d); // Pass Graphics2D object to render method
    }
	
	public Sim getSim() {
		return sim;
	}
	
	// private methods
	
	private void setPanelSize() {
		size = new Dimension(Sim.SIM_WIDTH, Sim.SIM_HEIGHT);
		setMinimumSize(size);
		setMaximumSize(size);
		setPreferredSize(size);
	}
	
}
