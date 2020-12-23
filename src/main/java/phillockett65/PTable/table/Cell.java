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

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import phillockett65.PTable.MainController;
import phillockett65.PTable.elements.ElementConfig;

public class Cell {
	private MainController main;
	private Group group;

	private ElementConfig e;
	private int x = 0;
	private int y = 0;

	private int state;
	private boolean selected = false;

	private Rectangle back;
	private Text Z;
	private Text symbol;

	/**
	 * Constructor.
	 * 
	 * @param mainController through which control is centralized.
	 * @param group	- representing the objects that are used to display 
	 *				  the PTable.
	 */
	public Cell(MainController mainController, Group group) {
		main = mainController;
		this.group = group;
		back = new Rectangle();
		back.setFill(main.getSubcategoryColour(0));
		group.getChildren().add(back);
	}

	/**
	 * Get the state (solid, liquid or gas) of the associated element at the 
	 * current temperature. The state is used to set the foreground colour.
	 * 
	 * @return the state.
	 */
	public int getState() {
		return state;
	}

	/**
	 * Set the state (solid, liquid or gas) of the associated element at the 
	 * current temperature. This is called whenever the temperature is changed.
	 * 
	 * @param state (solid, liquid or gas).
	 */
	public void setState(int state) {
		this.state = state;
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
	 * Set the element of this cell.
	 * 
	 * @param e the Element to associate with the cell.
	 */
	public void setE(ElementConfig e) {
		this.e = e;
		Z = new Text(String.valueOf(e.getZ()));
		symbol = new Text(e.getSymbol());
		group.getChildren().addAll(Z, symbol);
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
	 * Set the coordinates of the tile origin in pixels.
	 * 
	 * @param x coordinate of tile position.
	 * @param y coordinate of tile position.
	 */
	public void setPosition(int x, int y) {
		this.x = x;
		this.y = y;
	}

	/**
	 * Draw the background of the Tile using the current colour of the 
	 * Subcategory of the Element, or UNKNOWN colour if no Element is 
	 * associated with the cell.
	 */
	public void drawBackground() {
//		System.out.println("drawBackground (" + x + ", " + y + ")");

		back.setWidth(main.getTileSize());
		back.setHeight(main.getTileSize());
		back.setX(x);
		back.setY(y);
		Color colour = main.getSubcategoryColour(getSubcategory());
		if (selected)
			colour = colour.invert();
		back.setFill(colour);
	}

	/**
	 * Draw the Atomic Number of the Element associated with the cell, using 
	 * the current colour of the State of the Element.
	 */
	public void drawZ() {
//		System.out.println("drawZ(" + x + ", " + y + ")");

		if (isBlank())
			return;

		Z.setFont(Font.font("arial", FontWeight.NORMAL, FontPosture.REGULAR, main.getZ().getSizeInt()));
		final int width = (int)(Z.getLayoutBounds().getWidth());

		final int px = x+main.getTileSize()-width;
		final int py = y+main.getZ().getDy();

		Z.setX(px); 
		Z.setY(py);
		Color colour = main.getStateColour(state);
		if (selected)
			colour = colour.invert();
		Z.setFill(colour);
	}

	/**
	 * Draw the Symbol of the Element associated with the cell, using 
	 * the current colour of the State of the Element.
	 */
	public void drawSymbol() {
//		System.out.println("drawSymbol(" + x + ", " + y + ")");

		if (isBlank())
			return;

		symbol.setFont(Font.font("arial", FontWeight.BOLD, FontPosture.REGULAR, main.getSymbol().getSizeInt()));

		final int px = x+main.getSymbol().getDx();
		final int py = y+main.getSymbol().getDy();

		symbol.setX(px); 
		symbol.setY(py);
		Color colour = main.getStateColour(state);
		if (selected)
			colour = colour.invert();
		symbol.setFill(colour);
	}

	/**
	 * Moves the nodes from the current Group to the given new Group and set 
	 * the current Group to the new Group.
	 * 
	 * @param newGroup to move the nodes to.
	 */
	public void setGroup(Group newGroup) {
//		System.out.println("setGroup()");

		group.getChildren().removeAll(back, Z, symbol);
		group = newGroup;
		group.getChildren().add(back);
		if (!isBlank())
			group.getChildren().addAll(Z, symbol);
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
//		System.out.println("setSelected(" + selected + ", " + x + ", " + y + ")");
		this.selected = selected;
		drawBackground();
		drawZ();
		drawSymbol();
	}

}
