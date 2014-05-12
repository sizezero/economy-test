package org.kleemann.economy_test.swing;

import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.util.HashMap;
import java.util.Map;

import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.KeyStroke;
import javax.swing.event.MouseInputAdapter;

import org.kleemann.economy_test.model.Desert;
import org.kleemann.economy_test.model.Forest;
import org.kleemann.economy_test.model.Mountain;
import org.kleemann.economy_test.model.Plain;
import org.kleemann.economy_test.model.Terrain;
import org.kleemann.economy_test.model.TerrainMap;

public class MapComponent extends JComponent {

	private static final int GAP_X = 1;
	private static final int GAP_Y = GAP_X;

	private static final int TERRAIN_DX = 20;
	private static final int TERRAIN_DY = TERRAIN_DX;

	private static final int CLICKS_PER_ZOOM = 1;

	private static final int PAN_PER_KEYSTROKE = 3;

	private final TerrainMap map;

	// map of model.Terrain class names to TerrainRender objects
	private final Map<String, TerrainRender> lookup;

	// map size in scale 1 pixels
	private final int gridWidth;
	private final int gridHeight;

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
		// map = TerrainMap.createRandom();
		map = TerrainMap.createFromFile();

		lookup = new HashMap<String, TerrainRender>();
		Class c = Desert.class;
		lookup.put(c.getName(), new TerrainRender(c));
		c = Forest.class;
		lookup.put(c.getName(), new TerrainRender(c));
		c = Plain.class;
		lookup.put(c.getName(), new TerrainRender(c));
		c = Mountain.class;
		lookup.put(c.getName(), new TerrainRender(c));

		gridWidth = (GAP_X + TERRAIN_DX) * map.getWidth();
		gridHeight = (GAP_Y + TERRAIN_DY) * map.getHeight();

		MouseListener m = new MouseListener();
		addMouseListener(m);
		addMouseMotionListener(m);
		addMouseWheelListener(m);

		getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0),
				"pan left");
		getActionMap().put("pan left", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				pan(-PAN_PER_KEYSTROKE, 0);
			}
		});

		getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0),
				"pan right");
		getActionMap().put("pan right", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				pan(PAN_PER_KEYSTROKE, 0);
			}
		});

		getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0), "pan up");
		getActionMap().put("pan up", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				pan(0, -PAN_PER_KEYSTROKE);
			}
		});

		getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0),
				"pan down");
		getActionMap().put("pan down", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				pan(0, PAN_PER_KEYSTROKE);
			}
		});

		getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_PAGE_UP, 0),
				"zoom out");
		getActionMap().put("zoom out", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				zoom(1);
			}
		});

		getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_PAGE_DOWN, 0),
				"zoom in");
		getActionMap().put("zoom in", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				zoom(-1);
			}
		});
	}

	@Override
	protected void paintComponent(Graphics g) {
		final int w = getWidth();
		final int h = getHeight();
		final float w2 = w / 2.0f;
		final float h2 = h / 2.0f;

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
			center_x = gridWidth / 2.0f;
			center_y = gridHeight / 2.0f;
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
		for (int x = 0; x < map.getWidth(); ++x) {
			for (int y = 0; y < map.getHeight(); ++y) {
				final float ux = -center_x + w2 + x * (TERRAIN_DX + GAP_X);
				final float uy = -center_y + h2 + y * (TERRAIN_DY + GAP_Y);
				final int sx = (int) (((ux - w2) * scale) + w2);
				final int sy = (int) (((uy - h2) * scale) + h2);
				Terrain t = map.get(x, y);
				TerrainRender tr = lookup.get(t.getClass().getName());
				tr.render(t, g, sx, sy, sdx, sdy);
			}
		}
		// g2d.dispose(); // clean up
	}

	private void pan(int offx, int offy) {
		center_x = center_x - offx / scale;
		center_y = center_y - offy / scale;
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
			pan(diffX, diffY);
			lastX = e.getX();
			lastY = e.getY();
		}

		@Override
		public void mouseWheelMoved(MouseWheelEvent e) {
			zoom(e.getWheelRotation());
		}
	}
}
