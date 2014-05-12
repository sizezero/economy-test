package org.kleemann.economy_test.model;

import java.util.Random;

public class TerrainMap {

	private static final int DX = 15;
	private static final int DY = 10;

	private final Terrain[] ar;

	/**
	 * Creates a random map of the default size
	 */
	public TerrainMap() {
		Random r = new Random();
		final int len = DX * DY;
		ar = new Terrain[len];
		int i = 0;
		for (int x = 0; x < DX; ++x) {
			for (int y = 0; y < DY; ++y) {
				int n = r.nextInt(4);
				if (n == 0) {
					ar[i] = new Desert(x, y);
				} else if (n == 1) {
					ar[i] = new Plain(x, y);
				} else if (n == 2) {
					ar[i] = new Forest(x, y);
				} else if (n == 3) {
					ar[i] = new Mountain(x, y);
				} else {
					throw new RuntimeException("random number out of range");
				}
				++i;
			}
		}
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
