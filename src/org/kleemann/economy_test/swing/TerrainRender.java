package org.kleemann.economy_test.swing;

import java.awt.Color;
import java.awt.Graphics;

import org.kleemann.economy_test.model.Terrain;

/**
 * This class is capable of rendering a terrain model object
 */
public class TerrainRender {

	private Color color;

	public TerrainRender(Class clazz) {
		final String n = clazz.getName();
		if (n.equals("org.kleemann.economy_test.model.Desert")) {
			color = Color.YELLOW;
		} else if (n.equals("org.kleemann.economy_test.model.Forest")) {
			color = Color.GREEN.darker();
		} else if (n.equals("org.kleemann.economy_test.model.Plain")) {
			color = Color.GREEN.brighter();
		} else if (n.equals("org.kleemann.economy_test.model.Mountain")) {
			color = Color.RED.darker();
		} else {
			throw new RuntimeException("Unkown Terrain");
		}
	}

	public void render(Terrain t, Graphics g, int x, int y, int dx, int dy) {
		// terrain is currently not used
		g.setColor(color);
		g.fillRect(x, y, dx, dy);
	}

}
