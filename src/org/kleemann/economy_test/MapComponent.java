package org.kleemann.economy_test;

import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JComponent;

public class MapComponent extends JComponent {

	private static final int BORDER_X = 5;
	private static final int BORDER_Y = BORDER_X;

	private static final int GAP_X = 1;
	private static final int GAP_Y = GAP_X;
	
	private static final int GRID_DX = 15;
	private static final int GRID_DY = 10;

	private static final int TERRAIN_DX = 20;
	private static final int TERRAIN_DY = TERRAIN_DX;

	protected void paintComponent(Graphics g) {
        if (isOpaque()) { //paint background
            g.setColor(getBackground());
            g.fillRect(0, 0, getWidth(), getHeight());
        }

        Graphics2D g2d = (Graphics2D)g.create();
        g.setColor(getForeground());
        for (int x=0 ; x<GRID_DX ; ++x) {
            for (int y=0 ; y<GRID_DY ; ++y) {
            	g2d.fillRect(
            			BORDER_X + x*(TERRAIN_DX+GAP_X), 
            			BORDER_Y + y*(TERRAIN_DY+GAP_Y), 
            			TERRAIN_DX, TERRAIN_DY);
            }
        }
        g2d.dispose(); //clean up
    }
}
