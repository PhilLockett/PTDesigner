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
 * Cell is a class that is responsible for drawing the tile that represents 
 * the chemical element on the table. 
 * 
 */
package phillockett65.PTable.table;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import phillockett65.PTable.elements.ElementConfig;

public class Cell {

	private ElementConfig e;

	private Color backCol;
	private Color foreCol;
	private int state;
	private boolean selected = false;

	private Rectangle back;
	private Text Z;
	private Text symbol;

	public Rectangle getBack() {
		return back;
	}

	public Text getZ() {
		return Z;
	}

	public Text getSymbol() {
		return symbol;
	}

	/**
	 * Constructor.
	 * 
	 * @param mainController through which control is centralized.
	 * @param group	- representing the objects that are used to display 
	 *				  the PTable.
	 */
	public Cell(int size, Color colour) {
		back = new Rectangle();
		setBackground(size, colour);
	}

	/**
	 * Set the ElementConfig for this cell, include colours and font sizes.
	 * 
	 * @param e the Element to associate with the cell.
	 * @param foreCol - foreground colour, determined by the state .
	 * @param backCol - background colour, determined by the subcategory.
	 * @param ZFontSize - Atomic Weight font size.
	 * @param symbolFontSize - Symbol font size.
	 */
	public void setElement(ElementConfig e, Color foreCol, Color backCol, int ZFontSize, int symbolFontSize) {
		this.e = e;

		setBackgroundColour(backCol);
		updateBackground();

		Z = new Text(String.valueOf(e.getZ()));
		Z.setFont(Font.font("arial", FontWeight.NORMAL, FontPosture.REGULAR, ZFontSize));

		symbol = new Text(e.getSymbol());
		symbol.setFont(Font.font("arial", FontWeight.BOLD, FontPosture.REGULAR, symbolFontSize));

		setForegroundColour(foreCol);
		updateForeground();
	}

	/**
	 * Get the associated element of this cell.
	 * 
	 * @return the associated element.
	 */
	public ElementConfig getE() {
		return e;
	}

	/**
	 * Check if the cell is considered blank, i. e. it does not have an 
	 * Element associated with it.
	 * 
	 * @return true if there is no Element associated with the cell.
	 */
	public boolean isBlank() {
		return (e == null);
	}

	/**
	 * Get the state (solid, liquid or gas) of the associated element at the 
	 * current temperature. The state is used to set the foreground colour.
	 * 
	 * @return the state.
	 */
	public int getState() {
//		System.out.println("getState(" + state + ")");
		return state;
	}

	/**
	 * Set the state (solid, liquid or gas) of the associated element at the 
	 * current temperature. This is called whenever the temperature is changed.
	 * 
	 * @param state (solid, liquid or gas).
	 */
	public void setState(int state) {
//		System.out.println("setState(" + state + ")");
		this.state = state;
	}

	/**
	 * Get the Subcategory of the associated Element of the cell.
	 * 
	 * @return the Subcategory of the associated Element.
	 */
	public int getSubcategory() {
		if (isBlank())
			return 0;

		return e.getSubcategory();
	}

	/**
	 * Set the coordinates of the tile origin in pixels. Assumes that tile 
	 * size has already been set as needed.
	 * 
	 * @param x coordinate of tile position.
	 * @param y coordinate of tile position.
	 * @param ZDesc			- Atomic Weight font descriptor.
	 * @param symbolDesc	- Symbol font descriptor.
	 */
	public void setPosition(int x, int y, Desc ZDesc, Desc symbolDesc) {
		back.setX(x);
		back.setY(y);

		if (isBlank())
			return;

		final int tileSize = (int)back.getHeight();
		final int width = (int)(Z.getLayoutBounds().getWidth());
		int px = x + tileSize - width;
		int py = y + ZDesc.getDy();
		Z.setX(px); 
		Z.setY(py);

		px = x + symbolDesc.getDx();
		py = y + symbolDesc.getDy();
		symbol.setX(px); 
		symbol.setY(py);
}

	public void setTileSize(int size) {
//		System.out.println("setTileSize(" + size + ")");

		back.setWidth(size);
		back.setHeight(size);
	}

	public void setBackgroundColour(Color backCol) {
//		System.out.println("setBackgroundColour(" + backCol.toString() + ")");
		this.backCol = backCol;
	}

	public void updateBackground() {
//		System.out.println("showBackground()");

		Color colour = backCol;
		if (selected)
			colour = colour.invert();

		back.setFill(colour);
	}

	/**
	 * Draw the background of the Tile using the current colour of the 
	 * Subcategory of the Element, or UNKNOWN colour if no Element is 
	 * associated with the cell.
	 */
	public void setBackground(int size, Color colour) {
//		System.out.println("setBackground(" + size + ", " + colour.toString() + ")");

		setTileSize(size);
		setBackgroundColour(colour);
		updateBackground();
	}

	public void setFontSize(int ZFontSize, int symbolFontSize) {
//		System.out.println("setFontSize(" + ZFontSize + ", " + symbolFontSize + ")");

		if (isBlank())
			return;

		Z.setFont(Font.font("arial", FontWeight.NORMAL, FontPosture.REGULAR, ZFontSize));
		symbol.setFont(Font.font("arial", FontWeight.BOLD, FontPosture.REGULAR, symbolFontSize));
	}

	public void setForegroundColour(Color foreCol) {
//		System.out.println("setForegroundColour(" + foreCol.toString() + ")");

		this.foreCol = foreCol;
	}

	public void updateForeground() {
//		System.out.println("showForeground()");

		if (isBlank())
			return;

		Color colour = foreCol;
		if (selected)
			colour = colour.invert();
		Z.setFill(colour);
		symbol.setFill(colour);
	}

	public void setForeground(Color colour, int ZFontSize, int symbolFontSize) {
//		System.out.println("setForeground(" + colour.toString() + ", " + ZFontSize + ", " + symbolFontSize + ")");

		if (isBlank())
			return;

		setFontSize(ZFontSize, symbolFontSize);
		setForegroundColour(colour);
		updateForeground();
	}

	/**
	 * Find out if the cell has been selected.
	 * 
	 * @return true if the cell has been selected, false otherwise.
	 */
	public boolean isSelected() {
		return selected;
	}

	/**
	 * Indicate whether a cell should be highlighted.
	 * 
	 * @param selected	- flag to indicate if the cell should be display as 
	 *					  highlighted.
	 */
	public void setSelected(boolean selected) {
//		System.out.println("setSelected(" + selected + ", " + back.getX() + ", " + back.getY() + ")");
		this.selected = selected;
		updateBackground();

		if (!isBlank())
			updateForeground();
	}

}
