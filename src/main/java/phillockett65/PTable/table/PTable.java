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

import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import phillockett65.PTable.ChangeChecker;
import phillockett65.PTable.MainController;
import phillockett65.PTable.Model;
import phillockett65.PTable.elements.ElementConfig;
import phillockett65.PTable.elements.Elements;

public class PTable extends Stage {
	
	private KeyHandler keyEventHandler;

	private MainController main;
	private Group group;
	private Scene scene;

	private final String title;

	private Cell[][] grid;
	private Selection selection;
	private boolean gridChanged = false;

	private Quantifier quantities;


	/**
	 * Constructor.
	 * 
	 * @param mainController	- used to call the centralized controller.
	 * @param title				- string displayed as the heading of the Stage.
	 */
	public PTable(MainController mainController, String title) {
//		System.out.println("PTable constructed: " + title);

		this.title = title;
		setTitle(title);
		resizableProperty().setValue(false);
		setOnCloseRequest(e -> Platform.exit());

		keyEventHandler = new KeyHandler(this);
		selection = keyEventHandler.getSelection();
		this.addEventHandler(KeyEvent.KEY_PRESSED, keyEventHandler);
		this.addEventHandler(KeyEvent.KEY_RELEASED, keyEventHandler);

		main = mainController;
		group = new Group();

		initTable();
		drawTable();

		scene = new Scene(group, main.getWidth(), main.getHeight());
		setScene(scene);
		show();
	}


/************************************************************************
 * Table drawing support section.
 */

	/**
	 * Initializes the table by constructing the cells, initializes some 
	 * globals and adds the Elements to the appropriate cells. 
	 */
	private void initTable() {
		final int rows = main.getRows();
		final int cols = main.getCols();
		boolean cellHighlighted = false;

		grid = new Cell[rows][cols];
		for (int r = 0; r < rows; ++r)
			for (int c = 0; c < cols; ++c)
				grid[r][c] = new Cell(main, group);

		setCellLocations();
		quantities = new Quantifier(grid);
		keyEventHandler.setGrid(grid);

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
				cell.setE(e);
				cell.setState(findState(e));
				if (cellHighlighted == false) {
					cellHighlighted = true;
					selection.setPosition(r, c);
					cell.setSelected(true);
					main.setSelected(cell);
				}
			}
		}
	}

	/**
	 * Determines whether the given element is solid, liquid or gas at the 
	 * current temperature.
	 * 
	 * @param e the given element.
	 * @return the state.
	 */
	private int findState(ElementConfig e) {
		final float melt = e.getMelt();
		final float boil = e.getBoil();
		final float temp = main.getTemp();
		if ((melt == 0) && (boil == 0))
			return Model.UNDEFINED;

		if (temp < melt)
			return Model.SOLID;

		if (temp < boil)
			return Model.LIQUID;

		return Model.GAS;
	}

	/**
	 * Update the state for each element in the grid.
	 */
	public void updateState() {
//		System.out.println("updateState()");

		final int rows = grid.length;
		final int cols = grid[0].length;

		for (int r = 0; r < rows; ++r) {
			for (int c = 0; c < cols; ++c) {
				Cell cell = grid[r][c];
				if (cell.isBlank())
					continue;

				cell.setState(findState(cell.getE()));
			}
		}
	}

	/**
	 * Draws all the cells in the current grid.
	 */
	public void drawTable() {
//		System.out.println("drawTable()");

		if (gridChanged) {
			gridChanged = false;

			setCellLocations();
		}

		final int rows = grid.length;
		final int cols = grid[0].length;

		for (int r = 0; r < rows; ++r) {
			for (int c = 0; c < cols; ++c) {
				Cell cell = grid[r][c];
				cell.drawBackground();
				cell.drawZ();
				cell.drawSymbol();
			}
		}
	}

	/**
	 * Sets the pixel location of each Cell based on the boarder size, tile 
	 * size and position in grid.
	 */
	private void setCellLocations() {
//		System.out.println("setCellPosition()");

		final int border = main.getBorderSize();
		final int step = main.getStepSize();
		final int rows = grid.length;
		final int cols = grid[0].length;

		int[] rowOffsets = new int[rows];
		int[] colOffsets = new int[cols];

		int p = border;
		for (int i = 0; i < rows; ++i) {
			rowOffsets[i] = p;
			p += step;
		}
		p = border;
		for (int i = 0; i < cols; ++i) {
			colOffsets[i] = p;
			p += step;
		}

		for (int r = 0; r < rows; ++r) {
			for (int c = 0; c < cols; ++c) {
				final Cell cell = grid[r][c];
				cell.setPosition(colOffsets[c], rowOffsets[r]);
			}
		}
	}

	/**
	 * Updates the background of all Elements of the specified subcategory to 
	 * the latest colour.
	 * 
	 * @param subcategory with updated colour.
	 */
	public void setSubcategoryColour(int subcategory) {
//		System.out.println("setSubcategoryColour(subcategory = " + subcategory + ")");

		final int rows = grid.length;
		final int cols = grid[0].length;

		for (int r = 0; r < rows; ++r) {
			for (int c = 0; c < cols; ++c) {
				final Cell cell = grid[r][c];
				if (cell.getSubcategory() == subcategory)
					cell.drawBackground();
			}
		}
	}

	/**
	 * Updates the foreground of all Elements in the specified state to the 
	 * latest colour.
	 * 
	 * @param state with updated colour.
	 */
	public void setStateColour(int state) {
//		System.out.println("setStateColour(state = " + state + ")");

		final int rows = grid.length;
		final int cols = grid[0].length;

		for (int r = 0; r < rows; ++r) {
			for (int c = 0; c < cols; ++c) {
				final Cell cell = grid[r][c];
				if (cell.getState() == state) {
					cell.drawZ();
					cell.drawSymbol();
				}
			}
		}
	}

	/**
	 * If the number of rows or columns is changed we create a new grid, move 
	 * all the Cells to the new grid and add new ones as necessary, move all 
	 * the nodes to a new Group, then use it to resize the window.
	 */
	public void gridChange() {
//		System.out.println("gridChange(rows = " + grid.length + " -> " + main.getRows() + ")");
//		System.out.println("gridChange(rows = " + grid[0].length + " -> " + main.getCols() + ")");

		gridChanged = true;
		ChangeChecker rowCkr = new ChangeChecker(grid.length, main.getRows());
		ChangeChecker colCkr = new ChangeChecker(grid[0].length, main.getCols());

		if (!rowCkr.isChanged() && !colCkr.isChanged()) {
			return;
		}

		Cell[][] newGrid = new Cell[rowCkr.getNewValue()][colCkr.getNewValue()];
		Group newGroup = new Group();

		int maxRow = rowCkr.getNewValue();
		if (rowCkr.isIncreased())
			maxRow = rowCkr.getOldValue();
		for (int r = 0; r < maxRow; ++r) {
			for (int c = 0; c < colCkr.getNewValue(); ++c) {
				if (c < colCkr.getOldValue()) {
					newGrid[r][c] = grid[r][c];
					newGrid[r][c].setGroup(newGroup);
				}
				else {
					newGrid[r][c] = new Cell(main, newGroup);
				}
			}
		}

		// Add new rows of empty cells if necessary.
		if (rowCkr.isIncreased()) {
			for (int r = maxRow; r < rowCkr.getNewValue(); ++r) {
				for (int c = 0; c < colCkr.getNewValue(); ++c) {
					newGrid[r][c] = new Cell(main, newGroup);
				}
			}
		}

		// Adjust highlighted cells, if necessary.
		if ((rowCkr.isDecreased()) || (colCkr.isDecreased())) {
			if (selection.cropSelection(rowCkr.getNewValue(), colCkr.getNewValue())) {
				highlightSelectedCells(true);
			}
		}

		grid = newGrid;
		quantities.setGrid(grid);
		keyEventHandler.setGrid(grid);
		group = newGroup;
		scene = new Scene(group, main.getWidth(), main.getHeight());
		setScene(scene);
	}

	/**
	 * If the Tile or Border size is changed we move all the nodes to a new 
	 * Group, then use it to resize the window. However, if gridChange() has 
	 * just been called, it will already have done this, so we can abort.
	 */
	public void sizeChange() {
//		System.out.println("sizeChange(rows = " + main.getWidth() + " -> " + main.getHeight() + ")");

		// If we've executed gridChange(), we don't need to rebuild the Group, 
		// so abort.
		if (gridChanged) {
			return;
		}

		gridChanged = true;
		final int rows = grid.length;
		final int cols = grid[0].length;
		Group newGroup = new Group();

		for (int r = 0; r < rows; ++r) {
			for (int c = 0; c < cols; ++c) {
				grid[r][c].setGroup(newGroup);
			}
		}

		group = newGroup;
		scene = new Scene(group, main.getWidth(), main.getHeight());
		setScene(scene);
	}

	/**
	 * Reverses the order of the columns, then repositions the cells and 
	 * redraws the grid.
	 */
	public void flipColumns() {
//		System.out.println("Flip Columns.");

		selection.flipColumns(grid[0].length);

		for (int row = 0; row < grid.length; row++) {
			int length = grid[row].length;
			int e = length - 1;
			for (int col = 0; col < (length/2); col++, e--) {
				Cell temp = grid[row][col];
				grid[row][col] = grid[row][e];
				grid[row][e] = temp;
			}
		}

		gridChanged = true;	// Ensure that we update the cell positions.
		drawTable();
	}

	/**
	 * Reverses the order of the rows, then repositions the cells and 
	 * redraws the grid.
	 */
	public void flipRows() {
//		System.out.println("Flip Rows.");

		int e = grid.length - 1;
		selection.flipRows(grid.length);

		for (int row = 0; row < (grid.length/2); row++, e--) {
			Cell[] temp = grid[row];
			grid[row] = grid[e];
			grid[e] = temp;
		}

		gridChanged = true;	// Ensure that we update the cell positions.
		drawTable();
	}


	/**
	 * Make the the current quantities accessible.
	 * 
	 * @return the current quantities.
	 */
	public Quantities getQuantities() {
		return quantities.getQuantities();
	}


/************************************************************************
 * Key handling support code.
 */

	/**
	 * Allow the KeyHandler to update the window title to indicate the 
	 * Shift/Control key press sate.
	 * 
	 * @param action - String to indicate the current user action.
	 */
	public void augmentTitle(String action) {
		setTitle(title + action);
	}

	/**
	 * Highlight/unhighlight the currently selected cells.
	 * 
	 * @param value	true if cell is to be highlighted, false if highlighting 
	 * 				is to be removed.
	 */
	public void highlightSelectedCells(boolean value) {
//		System.out.println("setSelected(" + value + ")");

		final int topRow = selection.getTop();
		final int bottomRow = selection.getBottom();
		final int leftCol = selection.getLeft();
		final int rightCol = selection.getRight();

		// Highlight sub grid.
		for (int r = topRow; r <= bottomRow; ++r) {
			for (int c = leftCol; c <= rightCol; ++c) {
				grid[r][c].setSelected(value);
			}
		}

		// Update the Details tab display.
		final int row = selection.getRow();
		final int col = selection.getCol();
		Cell cell = grid[row][col];
		if (!cell.isBlank())
			main.setSelected(cell);
	}

	/**
	 * Move the currently selected cells in the direction indicated.
	 * 
	 * @param code to indicate the direction to move the cells.
	 */
	public void moveSelection(KeyCode code) {

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
			return;
		}

		gridChanged = true;
		drawTable();
		quantities.clear();
	}

}
