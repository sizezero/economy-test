package org.kleemann.economy_test.model;

/**
 * <p>
 * This represents a single tile of terrain. Each tile is the same width and
 * height. Tiles are the lowest item on the board and so everything else exists
 * on top of tiles.
 * 
 * <p>
 * At first I thought that every object in the world including Terrain would be
 * a Thing but maybe that's not the best way to go. Let's start with Terrain
 * being a unique object.
 */
public abstract class Terrain {

	/**
	 * We will use integer locations so tiles have to be big enough to handle
	 * the granularity of object sizes within.
	 */
	public static final int DIM = 100;

	private final int row;
	private final int column;

	public Terrain(int column, int row) {
		this.column = column;
		this.row = row;
	}

	/**
	 * Each terrain has a unique row/column from 0 to the map size
	 */
	public int getRow() {
		return row;
	}

	/**
	 * Each terrain has a unique row/column from 0 to the map size
	 */
	public int getColumn() {
		return column;
	}

}
