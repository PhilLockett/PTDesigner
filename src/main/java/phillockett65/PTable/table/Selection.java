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
 * Selection is a simple class that captures the current user selection. The 
 * selection is the rectangle bounded by the "first" position and the current 
 * position.
 */
package phillockett65.PTable.table;

public class Selection {
	private int currentRow = 0;
	private int currentCol = 0;
	private int firstRow = 0;
	private int firstCol = 0;

	/**
	 * Set the current position.
	 * 
	 * @param row - current row.
	 * @param col - current column.
	 */
	public void setPosition(int row, int col) {
//		System.out.println("setPosition(row = " + row + ", col = " + col + ")");
		currentRow = row;
		currentCol = col;
	}

	/**
	 * Lock in the current position.
	 */
	public void saveCurrent() {
		firstRow = currentRow;
		firstCol = currentCol;
	}

	/**
	 * Get the left boundary of the current selection.
	 * 
	 * @return the left boundary of the current selection.
	 */
	public int getLeft() {
		return (firstCol < currentCol) ? firstCol : currentCol;
	}

	/**
	 * Get the right boundary of the current selection.
	 * 
	 * @return the right boundary of the current selection.
	 */
	public int getRight() {
		return (firstCol > currentCol) ? firstCol : currentCol;
	}

	/**
	 * Get the top boundary of the current selection.
	 * 
	 * @return the top boundary of the current selection.
	 */
	public int getTop() {
		return (firstRow < currentRow) ? firstRow : currentRow;
	}

	/**
	 * Get the bottom boundary of the current selection.
	 * 
	 * @return the bottom boundary of the current selection.
	 */
	public int getBottom() {
		return (firstRow > currentRow) ? firstRow : currentRow;
	}

	/**
	 * Get the row of the current position.
	 * 
	 * @return the row of the current position.
	 */
	public int getRow() {
		return currentRow;
	}

	/**
	 * Get the column of the current position.
	 * 
	 * @return the column of the current position.
	 */
	public int getCol() {
		return currentCol;
	}

	/**
	 * Decrement the row of the current position.
	 */
	public void positionUp() {
		currentRow--;
	}

	/**
	 * Increment the row of the current position.
	 */
	public void positionDown() {
		currentRow++;
	}

	/**
	 * Decrement the column of the current position.
	 */
	public void positionLeft() {
		currentCol--;
	}

	/**
	 * Increment the column of the current position.
	 */
	public void positionRight() {
		currentCol++;
	}

	/**
	 * Move current selection left.
	 */
	public void moveLeft() {
		firstCol--;
		currentCol--;
	}

	/**
	 * Move current selection right.
	 */
	public void moveRight() {
		firstCol++;
		currentCol++;
	}

	/**
	 * Move current selection up.
	 */
	public void moveUp() {
		firstRow--;
		currentRow--;
	}

	/**
	 * Move current selection down.
	 */
	public void moveDown() {
		firstRow++;
		currentRow++;
	}

	/**
	 * Change the selection to match the corresponding vertical flip.
	 * 
	 * @param cols - current column count.
	 */
	public void flipColumns(int cols) {
		cols--;
		currentCol = cols - currentCol;
		firstCol = cols - firstCol;
	}

	/**
	 * Change the selection to match the corresponding horizontal flip.
	 * 
	 * @param rows - current row count.
	 */
	public void flipRows(int rows) {
		rows--;
		currentRow = rows - currentRow;
		firstRow = rows - firstRow;
	}

	/**
	 * Crop the edges of the selection if they fall outside of a grid that has 
	 * been reduced in size.
	 * 
	 * @param rows - row count of the current grid. 
	 * @param cols - column count of the current grid.
	 * @return true if the selection was adjusted, false otherwise.
	 */
	public boolean cropSelection(int rows, int cols) {
		boolean change = false;

		rows--;
		if (currentRow > rows) {
			change = true;
			currentRow = rows;
		}
		if (firstRow > rows) {
			change = true;
			firstRow = rows;
		}

		cols--;
		if (currentCol > cols) {
			change = true;
			currentCol = cols;
		}
		if (firstCol > cols) {
			change = true;
			firstCol = cols;
		}

		return change;
	}

	/**
	 * Generate a string version.
	 */
	public String toString() {
		return "[" + getTop() + ", " + getLeft() + "] - [" + getBottom() + ", " + getRight() + "]";
	}

}
