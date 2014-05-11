package org.kleemann.economy_test;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

import javax.swing.JComponent;
import javax.swing.event.MouseInputAdapter;

public class MapComponent extends JComponent {

	private static final int GAP_X = 1;
	private static final int GAP_Y = GAP_X;

	private static final int GRID_DX = 15;
	private static final int GRID_DY = 10;

	private static final int TERRAIN_DX = 20;
	private static final int TERRAIN_DY = TERRAIN_DX;

	// map size in scale 1 pixels
	private static final int MAP_DX = (GAP_X + TERRAIN_DX) * GRID_DX;
	private static final int MAP_DY = (GAP_Y + TERRAIN_DY) * GRID_DY;

	private static final int CLICKS_PER_ZOOM = 1;

	private boolean centerIsUnset = true;
	// center is the scale 1.0 map coordinates that exist at the center of the
	// screen
	private float center_x = 0;
	private float center_y = 0;
	private float scale = 1.0f;

	// cached width and height values
	private int oldWidth = 0;
	private int oldHeight = 0;

	// positive = zoom in (larger map)
	// negative = zoom out (smaller map)
	private int scaleClicks = 0;

	public MapComponent() {
		MouseListener m = new MouseListener();
		addMouseListener(m);
		addMouseMotionListener(m);
		addMouseWheelListener(m);
	}

	protected void paintComponent(Graphics g) {
		final int w = getWidth();
		final int h = getHeight();
		final float w2 = w/2.0f;
		final float h2 = h/2.0f;

		// determine scale based on clicks
		if (scaleClicks == 0) {
			scale = 1.0f;
		} else {
			if (scaleClicks > 0) {
				scale = ((float) scaleClicks * CLICKS_PER_ZOOM)
						/ CLICKS_PER_ZOOM;
			} else { // scaleClicks < 0
				// -1 is required so that 0 and -1 don't have the same scale
				scale = 1.0f / -(((scaleClicks - 1.0f) * CLICKS_PER_ZOOM) / CLICKS_PER_ZOOM);
			}
		}
		System.out.println("scale " + scale);

		// not sure if there is a JComponent callback that reliably tells us
		// that the width and height have changed
		if (centerIsUnset || oldWidth != w || oldHeight != h) {
			center_x = MAP_DX / 2.0f;
			center_y = MAP_DY / 2.0f;
			centerIsUnset = false;
			oldWidth = w;
			oldHeight = h;
		}

		if (isOpaque()) { // paint background
			g.setColor(getBackground());
			g.fillRect(0, 0, w, h);
		}

		final int sdx = (int) (TERRAIN_DX * scale);
		final int sdy = (int) (TERRAIN_DY * scale);

		// Graphics2D g2d = (Graphics2D) g.create();
		g.setColor(getForeground());
		for (int x = 0; x < GRID_DX; ++x) {
			for (int y = 0; y < GRID_DY; ++y) {
				final float ux = - center_x + w2 + x * (TERRAIN_DX + GAP_X);
				final float uy = - center_y + h2 + y * (TERRAIN_DY + GAP_Y);
				final int sx = (int) (((ux - w2) * scale) + w2);
				final int sy = (int) (((uy - h2) * scale) + h2);
				g.fillRect(sx, sy, sdx, sdy);
			}
		}
		// g2d.dispose(); // clean up
	}

	private void shift(int offx, int offy) {
		center_x = center_x - offx/scale;
		center_y = center_y - offy/scale;
		System.out.println("off " + offx + " " + offy);
		System.out.println("center " + center_x + " " + center_y);
		repaint();
	}

	private void zoom(int clicks) {
		scaleClicks += clicks;
		System.out.println("scaleclicks " + scaleClicks);
		repaint();
	}

	private class MouseListener extends MouseInputAdapter {
		private int lastX = 0;
		private int lastY = 0;

		@Override
		public void mousePressed(MouseEvent e) {
			lastX = e.getX();
			lastY = e.getY();
		}

		@Override
		public void mouseDragged(MouseEvent e) {
			final int diffX = e.getX() - lastX;
			final int diffY = e.getY() - lastY;
			shift(diffX, diffY);
			lastX = e.getX();
			lastY = e.getY();
		}

		@Override
		public void mouseWheelMoved(MouseWheelEvent e) {
			zoom(e.getWheelRotation());
		}
	}
}
