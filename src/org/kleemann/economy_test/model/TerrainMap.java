package org.kleemann.economy_test.model;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;

public class TerrainMap {

	private static final int DX = 15;
	private static final int DY = 10;

	private Terrain[] ar;

	/**
	 * Creates a random map of the default size
	 */
	private TerrainMap() {
	}

	public static TerrainMap createRandom() {
		TerrainMap that = new TerrainMap();
		Random r = new Random();
		final int len = DX * DY;
		that.ar = new Terrain[len];
		int i = 0;
		for (int x = 0; x < DX; ++x) {
			for (int y = 0; y < DY; ++y) {
				int n = r.nextInt(4);
				if (n == 0) {
					that.ar[i] = new Desert(x, y);
				} else if (n == 1) {
					that.ar[i] = new Plain(x, y);
				} else if (n == 2) {
					that.ar[i] = new Forest(x, y);
				} else if (n == 3) {
					that.ar[i] = new Mountain(x, y);
				} else {
					throw new RuntimeException("random number out of range");
				}
				++i;
			}
		}
		return that;
	}

	/**
	 * Creates a map based on the following bitmap pattern:
	 * 
	 * ffff00 == desert
	 * 
	 * 00ff0c == plain
	 * 
	 * ea0000 == mountain
	 * 
	 * 024c00 == forest
	 */
	public static TerrainMap createFromFile() {
		BufferedImage img = null;
		try {
			img = ImageIO.read(TerrainMap.class
					.getResource("/resources/map1.png"));
		} catch (IOException ioEx) {
			ioEx.printStackTrace();
		}
		if (img.getWidth() < DX || img.getHeight() < DY) {
			throw new RuntimeException("map image is not large enough");
		}
		TerrainMap that = new TerrainMap();
		final int len = DX * DY;
		that.ar = new Terrain[len];
		int i = 0;
		for (int y = 0; y < DY; ++y) {
			for (int x = 0; x < DX; ++x) {
				int c = img.getRGB(x, y);
				switch (c & 0xffffff) {
				case 0xffff00:
					that.ar[i] = new Desert(x, y);
					break;
				case 0x00ff0c:
					that.ar[i] = new Plain(x, y);
					break;
				case 0xea0000:
					that.ar[i] = new Mountain(x, y);
					break;
				case 0x024c00:
					that.ar[i] = new Forest(x, y);
					break;
				default:
					throw new RuntimeException(String.format(
							"Unknown value at (%d,%d) %x", x, y, c));
				}
				++i;
			}
		}
		return that;
	}

	public int getWidth() {
		return DX;
	}

	public int getHeight() {
		return DY;
	}

	public Terrain get(int x, int y) {
		return ar[x + y * DX];
	}
}
