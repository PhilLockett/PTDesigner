/*  PTDesigner - a simple application to design a periodic table.
 *
 *  Copyright 2020 Philip Lockett.
 *
 *  This file is part of PTDesigner.
 *
 *  PTDesigner is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  PTDesigner is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with PTDesigner.  If not, see <https://www.gnu.org/licenses/>.
 */

/*
 * PTable is a class that is responsible for creating the Stage, drawing and 
 * redrawing the grid that represents the Periodic Table.
 */
package phillockett65.PTable.table;

import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import phillockett65.PTable.ChangeChecker;
import phillockett65.PTable.MainController;
import phillockett65.PTable.Model;
import phillockett65.PTable.elements.ElementConfig;
import phillockett65.PTable.elements.Elements;

public class Grid {
	private MainController main;

	private Cell[][] grid;

	/**
	 * Constructor.
	 * 
	 * @param mainController	- used to call the centralized controller.
	 */
	public Grid(MainController mainController) {
		main = mainController;
		initTable(main);
	}

	/**
	 * Initializes the table by constructing the cells, initializes some 
	 * globals and adds the Elements to the appropriate cells. 
	 */
	private void initTable(MainController main) {
		final int rows = main.getRows();
		final int cols = main.getCols();

		final int size = main.getTileSize();
		final Color back = main.getSubcategoryColour(0);
		grid = new Cell[rows][cols];
		for (int r = 0; r < rows; ++r)
			for (int c = 0; c < cols; ++c)
				grid[r][c] = new Cell(size, back);;

		final int ZFontSize = main.getZ().getSizeInt();
		final int symbolFontSize = main.getSymbol().getSizeInt();
		for (int i = Elements.firstKey(); i <= Elements.lastKey(); i = Elements.nextKey(i)) {
			if (!Elements.isKeyValid(i)) {
				continue;
			}
			ElementConfig e = Elements.element(i);
			int r;
			int c;
			if (e.getPeriod() != 0) {
				if (e.getGroup() != 0) {
					r = (e.getPeriod()-1);
					c = (e.getGroup()-1);
				} else {
					r = (e.getPeriod()+2);
					c = (e.getGroup32()-1);
				}
				Cell cell = grid[r][c];
				final Color foreCol = main.getStateColour(e);
				final Color backCol = main.getSubcategoryColour(e.getSubcategory());
				cell.setElement(e, foreCol, backCol, ZFontSize, symbolFontSize);
			}
		}

		setCellLocations();
	}

	public Cell getCell(int row, int col) {
		return grid[row][col];
	}

	public int getRows() {
		return grid.length;
	}

	public int getCols() {
		return grid[0].length;
	}

	/**
	 * Determines whether the given element is solid, liquid or gas at the 
	 * current temperature.
	 * 
	 * @param e the given element.
	 * @param temperature.
	 * @return the state.
	 */
	private int findState(ElementConfig e, int temperature) {
		final float melt = e.getMelt();
		final float boil = e.getBoil();
		final float temp = temperature;
		if ((melt == 0) && (boil == 0))
			return Model.UNDEFINED;

		if (temp < melt)
			return Model.SOLID;

		if (temp < boil)
			return Model.LIQUID;

		return Model.GAS;
	}

	/**
	 * Update the state for each element in the grid for the given temperature.
	 * 
	 * @param temp - the given temperature.
	 */
	public void updateState(int temp) {
//		System.out.println("Grid.updateState(" + temp + ")");

		final int rows = grid.length;
		final int cols = grid[0].length;

		for (int r = 0; r < rows; ++r) {
			for (int c = 0; c < cols; ++c) {
				Cell cell = grid[r][c];
				if (cell.isBlank())
					continue;

				cell.setState(findState(cell.getE(), temp));
				cell.setForegroundColour(main.getStateColour(cell.getE()));
				cell.updateForeground();
			}
		}
	}


	/**
	 * Sets the pixel location of each Cell based on the boarder size, tile 
	 * size and position in grid.
	 */
	private void setCellLocations() {
//		System.out.println("setCellLocations(" + rows + ", " + cols + ", " + step + ", " + border + ")");

		final int rows = main.getRows();
		final int cols = main.getCols();
		final int max = Math.max(rows, cols);
		int[] offsets = new int[max];

		final int step = main.getStepSize();
		int p = main.getBorderSize();
		for (int i = 0; i < max; ++i) {
			offsets[i] = p;
			p += step;
		}

		for (int r = 0; r < rows; ++r) {
			for (int c = 0; c < cols; ++c) {
				final Cell cell = grid[r][c];
				cell.setPosition(offsets[c], offsets[r], main.getZ(), main.getSymbol());
			}
		}
	}

	/**
	 * Updates the background of all Elements of the specified subcategory to 
	 * the latest colour.
	 * 
	 * @param subcategory- with updated colour.
	 * @param colour to update with.
	 */
	public void setSubcategoryColour(int subcategory, Color colour) {
//		System.out.println("setSubcategoryColour(subcategory = " + subcategory + ", " + colour.toString() + ")");

		final int rows = grid.length;
		final int cols = grid[0].length;

		for (int r = 0; r < rows; ++r) {
			for (int c = 0; c < cols; ++c) {
				final Cell cell = grid[r][c];
				if (cell.getSubcategory() == subcategory) {
					cell.setBackgroundColour(colour);
					cell.updateBackground();
				}
			}
		}
	}

	/**
	 * Updates the foreground of all Elements in the specified state to the 
	 * latest colour.
	 * 
	 * @param state with updated colour.
	 */
	public void setStateColour(int state, Color colour) {
//		System.out.println("setStateColour(state = " + state + ", " + colour.toString() + ")");

		final int rows = grid.length;
		final int cols = grid[0].length;

		for (int r = 0; r < rows; ++r) {
			for (int c = 0; c < cols; ++c) {
				final Cell cell = grid[r][c];
				if (cell.getState() == state) {
					cell.setForegroundColour(colour);
					cell.updateForeground();
				}
			}
		}
	}

	/**
	 * If the number of rows or columns is changed we create a new grid, move 
	 * all the Cells to the new grid and add new ones as necessary, then 
	 * update the cell locations. 
	 * 
	 * @param rowCkr - changes to the number of rows.
	 * @param colCkr - changes to the number of columns.
	 * @return true if changes were made, false otherwise.
	 */
	public boolean gridChange(ChangeChecker rowCkr, ChangeChecker colCkr) {
//		System.out.println("gridChange(rows = " + grid.length + " -> " + main.getRows() + ")");
//		System.out.println("gridChange(rows = " + grid[0].length + " -> " + main.getCols() + ")");

		if (!rowCkr.isChanged() && !colCkr.isChanged()) {
			return false;
		}

		final int rows = rowCkr.getNewValue();
		final int cols = colCkr.getNewValue();
		Cell[][] newGrid = new Cell[rows][cols];

		final int size = main.getTileSize();
		final Color back = main.getSubcategoryColour(0);

		int maxRow = rows;
		if (rowCkr.isIncreased())
			maxRow = rowCkr.getOldValue();
		for (int r = 0; r < maxRow; ++r) {
			for (int c = 0; c < cols; ++c) {
				if (c < colCkr.getOldValue())
					newGrid[r][c] = grid[r][c];
				else
					newGrid[r][c] = new Cell(size, back);
			}
		}

		// Add new rows of empty cells if necessary.
		if (rowCkr.isIncreased()) {
			for (int r = maxRow; r < rows; ++r)
				for (int c = 0; c < cols; ++c)
					newGrid[r][c] = new Cell(size, back);
		}

		grid = newGrid;
		setCellLocations();

		return true;
	}

	/**
	 * If the Tile or Border size is changed we move all the nodes to a new 
	 * Group, then use it to resize the window. 
	 */
	public void sizeChange(int size, int ZFontSize, int symbolFontSize) {
//		System.out.println("sizeChange(rows = " + main.getWidth() + " -> " + main.getHeight() + ")");

		final int rows = grid.length;
		final int cols = grid[0].length;

		for (int r = 0; r < rows; ++r) {
			for (int c = 0; c < cols; ++c) {
				Cell cell = grid[r][c];
				cell.setTileSize(size);
				cell.setFontSize(ZFontSize, symbolFontSize);
			}
		}
	}

	public boolean updateLayout(
			ChangeChecker rowCkr, ChangeChecker colCkr, 
			ChangeChecker tileCkr, ChangeChecker brdrCkr,
			ChangeChecker tempCkr,
			int ZFontSize, int symbolFontSize) {

		boolean gridChanged = false;
		boolean sizeChanged = false;

		if (rowCkr.isChanged())
			gridChanged = true;

		if (colCkr.isChanged())
			gridChanged = true;

		if (tileCkr.isChanged())
			sizeChanged = true;

		if (brdrCkr.isChanged())
			sizeChanged = true;

		final int tile = tileCkr.getNewValue();
		gridChanged = gridChange(rowCkr, colCkr);

		if (sizeChanged || gridChanged) {
			if (sizeChanged)
				sizeChange(tile, ZFontSize, symbolFontSize);

			setCellLocations();
		}

		if (tempCkr.isChanged())
			updateState(tempCkr.getNewValue());

		return (gridChanged || sizeChanged);
	}

	/**
	 * Reverses the order of the columns.
	 */
	public void flipColumns() {
//		System.out.println("Flip Columns.");

		final int rows = grid.length;
		final int cols = grid[0].length;

		for (int row = 0; row < rows; row++) {
			int e = cols - 1;
			for (int col = 0; col < (cols/2); col++, e--) {
				Cell temp = grid[row][col];
				grid[row][col] = grid[row][e];
				grid[row][e] = temp;
			}
		}

		setCellLocations();
	}

	/**
	 * Reverses the order of the rows.
	 */
	public void flipRows() {
//		System.out.println("Flip Rows.");

		final int rows = grid.length;
		int e = rows - 1;

		for (int row = 0; row < (rows/2); row++, e--) {
			Cell[] temp = grid[row];
			grid[row] = grid[e];
			grid[e] = temp;
		}

		setCellLocations();
	}

	/**
	 * Move the currently selected cells in the direction indicated.
	 * 
	 * @param selection of cells to move.
	 * @param code to indicate the direction to move the cells.
	 */
	public boolean moveSelection(Selection selection, KeyCode code) {

		Cell[] temp;
		final int topRow = selection.getTop();
		final int bottomRow = selection.getBottom();
		final int leftCol = selection.getLeft();
		final int rightCol = selection.getRight();

		switch (code) {
		case UP:
			temp = new Cell[rightCol+1];
			for (int c = leftCol; c <= rightCol; ++c)
				temp[c] = grid[topRow-1][c];

			for (int r = topRow; r <= bottomRow; ++r)
				for (int c = leftCol; c <= rightCol; ++c)
					grid[r-1][c] = grid[r][c];

			for (int c = leftCol; c <= rightCol; ++c)
				grid[bottomRow][c] = temp[c];

			break;

		case DOWN:
			temp = new Cell[rightCol+1];
			for (int c = leftCol; c <= rightCol; ++c)
				temp[c] = grid[bottomRow+1][c];

			for (int r = bottomRow; r >= topRow; --r)
				for (int c = leftCol; c <= rightCol; ++c)
					grid[r+1][c] = grid[r][c];

			for (int c = leftCol; c <= rightCol; ++c)
				grid[topRow][c] = temp[c];

			break;

		case LEFT:
			temp = new Cell[bottomRow+1];
			for (int r = topRow; r <= bottomRow; ++r)
				temp[r] = grid[r][leftCol-1];

			for (int r = topRow; r <= bottomRow; ++r)
				for (int c = leftCol; c <= rightCol; ++c)
					grid[r][c-1] = grid[r][c];

			for (int r = topRow; r <= bottomRow; ++r)
				grid[r][rightCol] = temp[r];

			break;

		case RIGHT:
			temp = new Cell[bottomRow+1];
			for (int r = topRow; r <= bottomRow; ++r)
				temp[r] = grid[r][rightCol+1];

			for (int r = topRow; r <= bottomRow; ++r)
				for (int c = rightCol; c >= leftCol; --c)
					grid[r][c+1] = grid[r][c];

			for (int r = topRow; r <= bottomRow; ++r)
				grid[r][leftCol] = temp[r];

			break;

		default:
//			System.out.println("Key handler (" + code.toString() + ", " + pressed + ")");
			return false;
		}

		setCellLocations();

		return true;
	}

}
