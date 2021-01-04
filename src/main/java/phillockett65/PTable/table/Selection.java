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
	private int rowCount;
	private int colCount;
	private int currentRow = 0;
	private int currentCol = 0;
	private int firstRow = 0;
	private int firstCol = 0;

	/**
	 * Constructor. Sets the max limits.
	 * 
	 * @param rows	- Row count.
	 * @param cols	- Column count.
	 */
	public Selection(int rows, int cols) {
		rowCount = rows;
		colCount = cols;
	}

	/**
	 * Set row count and adjust selection to fit.
	 * 
	 * @param rows	- Row count.
	 */
	public void setRows(int rows) {
		rowCount = rows;
		if (currentRow >= rowCount)
			currentRow = rowCount-1;
		if (firstRow >= rowCount)
			firstRow = rowCount-1;
	}

	/**
	 * Set column count and adjust selection to fit.
	 * 
	 * @param cols	- Column count.
	 */
	public void setCols(int cols) {
		colCount = cols;
		if (currentCol >= colCount)
			currentCol = colCount-1;
		if (firstCol >= colCount)
			firstCol = colCount-1;
	}

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
	 * Determine if the current position can be moved up.
	 * 
	 * @return true if the current position can be moved, false otherwise.
	 */
	public boolean isPositionUp() {
		return (currentRow > 0);
	}

	/**
	 * Decrement the row of the current position.
	 */
	public void positionUp() {
		currentRow--;
	}

	/**
	 * Determine if the current position can be moved down.
	 * 
	 * @return true if the current position can be moved, false otherwise.
	 */
	public boolean isPositionDown() {
		return (currentRow < rowCount-1);
	}

	/**
	 * Increment the row of the current position.
	 */
	public void positionDown() {
		currentRow++;
	}

	/**
	 * Determine if the current position can be moved left.
	 * 
	 * @return true if the current position can be moved, false otherwise.
	 */
	public boolean isPositionLeft() {
		return (currentCol > 0);
	}

	/**
	 * Decrement the column of the current position.
	 */
	public void positionLeft() {
		currentCol--;
	}

	/**
	 * Determine if the current position can be moved right.
	 * 
	 * @return true if the current position can be moved, false otherwise.
	 */
	public boolean isPositionRight() {
		return (currentCol < colCount-1);
	}

	/**
	 * Increment the column of the current position.
	 */
	public void positionRight() {
		currentCol++;
	}

	/**
	 * Determine if the current selection can be moved up.
	 * 
	 * @return true if the current selection can be moved, false otherwise.
	 */
	public boolean isMoveUp() {
		return (getBottom() > 0);
	}

	/**
	 * Move current selection up.
	 */
	public void moveUp() {
		firstRow--;
		currentRow--;
	}

	/**
	 * Determine if the current selection can be moved down.
	 * 
	 * @return true if the current selection can be moved, false otherwise.
	 */
	public boolean isMoveDown() {
		return (getBottom() < rowCount-1);
	}

	/**
	 * Move current selection down.
	 */
	public void moveDown() {
		firstRow++;
		currentRow++;
	}

	/**
	 * Determine if the current selection can be moved left.
	 * 
	 * @return true if the current selection can be moved, false otherwise.
	 */
	public boolean isMoveLeft() {
		return (getLeft() > 0);
	}

	/**
	 * Move current selection left.
	 */
	public void moveLeft() {
		firstCol--;
		currentCol--;
	}

	/**
	 * Determine if the current selection can be moved right.
	 * 
	 * @return true if the current selection can be moved, false otherwise.
	 */
	public boolean isMoveRight() {
		return (getRight() < colCount-1);
	}

	/**
	 * Move current selection right.
	 */
	public void moveRight() {
		firstCol++;
		currentCol++;
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
	 * Generate a string version.
	 */
	public String toString() {
		return "[" + getTop() + ", " + getLeft() + "] - [" + getBottom() + ", " + getRight() + "]";
	}

}
