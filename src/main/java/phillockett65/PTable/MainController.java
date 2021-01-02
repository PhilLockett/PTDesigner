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
 * MainController is a class that is responsible for centralizing control. It
 * creates the Model and PTable window and provides access between all the 
 * tabs via a callback mechanism.
 */
package phillockett65.PTable;

import javafx.fxml.FXML;
import javafx.scene.paint.Color;
import phillockett65.PTable.elements.ElementConfig;
import phillockett65.PTable.table.Cell;
import phillockett65.PTable.table.Desc;
import phillockett65.PTable.table.PTable;
import phillockett65.PTable.table.Quantities;

public class MainController {

	@FXML StatusController statusTabController;
	@FXML DetailsController detailsTabController;
	@FXML LytSettingsController lytSettingsTabController;
	@FXML SttSettingsController sttSettingsTabController;
	@FXML SubSettingsController subSettingsTabController;

	private Model model;
	private PTable table;

	/**
	 * Constructor.
	 * 
	 * Responsible for creating the Model.
	 */
	public MainController() {
//		System.out.println("MainController constructed.");
		model = new Model();
	}

	/**
	 * Called by the FXML mechanism to initialize the controller. Creates a 
	 * callback link for all the tab controllers, creates the PTable window and
	 * update the Status tab display.
	 */
	@FXML public void initialize() {
//		System.out.println("MainController initialized.");
		statusTabController.init(this);
		detailsTabController.init(this);
		lytSettingsTabController.init(this);
		sttSettingsTabController.init(this);
		subSettingsTabController.init(this);

		table = new PTable(this, "Periodic Table");

		statusTabController.updateSettings();
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
		final float temp = model.getTemp();
		if ((melt == 0) && (boil == 0))
			return Model.UNDEFINED;

		if (temp < melt)
			return Model.SOLID;

		if (temp < boil)
			return Model.LIQUID;

		return Model.GAS;
	}

	public Color getStateColour(ElementConfig e) {
		final int index = findState(e);
		Color ret = model.getStateColour(index);
		return ret;
	}

	public Color getStateColour(int index) {
		Color ret = model.getStateColour(index);
		return ret;
	}

	public String getStateString(ElementConfig e) {
		final int index = findState(e);
		String ret = model.getStateString(index);
		return ret;
	}

	public String getStateString(int index) {
		String ret = model.getStateString(index);
		return ret;
	}

	public Color getSubcategoryColour(int index) {
		Color ret = model.getSubcategoryColour(index);
		return ret;
	}

	public String getSubcategoryString(int index) {
		String ret = model.getSubcategoryString(index);
		return ret;
	}

	public int getTemp() {
		return model.getTemp();
	}

	public int getMaxTemp() {
		return model.getMaxTemp();
	}

	public int getRows() {
		return model.getRows();
	}

	public int getCols() {
		return model.getCols();
	}

	public int getTileSize() {
		return model.getTileSize();
	}

	public int getBorderSize() {
		return model.getBorderSize();
	}

	public int getStepSize() {
		return model.getStepSize();
	}

	public int getWidth() {
		return model.getWidth();
	}

	public int getHeight() {
		return model.getHeight();
	}

	public Desc getZ() {
		return model.getZ();
	}

	public Desc getSymbol() {
		return model.getSymbol();
	}

	/**
	 * Called by the "Subcategory Settings" tab when the "Apply Change" button 
	 * is clicked. Updates the model and the colours on the table.
	 * 
	 * @param index of the changed Subcategory.
	 * @param text to use to describe the Subcategory.
	 * @param colour to use for the Subcategory background colour.
	 * @return true if the change was successful, false otherwise.
	 */
	public boolean updateSubcategory(int index, String text, Color colour) {

		if (model.setSubcategoryString(index, text)) {
			model.setSubcategoryColour(index, colour);
			table.setSubcategoryColours(index, colour);

			return true;
		}

		return false;
	}

	/**
	 * Called by the "State Settings" tab when the "Apply Change" button is 
	 * clicked. Updates the model and the colours on the table.
	 * 
	 * @param index of the changed State.
	 * @param text to use to describe the State.
	 * @param colour to use for the State foreground colour.
	 * @return true if the change was successful, false otherwise.
	 */
	public boolean updateState(int index, String text, Color colour) {

		if (model.setStateString(index, text)) {
			model.setStateColour(index, colour);
			table.setStateColours(index, colour);

			return true;
		}

		return false;
	}

	/**
	 * Called by the "Layout Settings" tab when the "Apply Change" button is 
	 * clicked. Changes the layout of the grid and updates the model with the 
	 * new data.
	 * 
	 * @param rowCkr		- Row count change.
	 * @param colCkr		- Column count change.
	 * @param tileCkr		- Tile size change.
	 * @param brdrCkr		- Border size change.
	 * @param tempCkr		- Temperature change.
	 * @param ZFontSize		- Atomic Weight font size change.
	 * @param symbolFontSize - Symbol font size change.
	 */
	public void updateLayout(int rows, int cols, int tile, int border, int temp) {
		ChangeChecker rowCkr = new ChangeChecker(getRows(), rows);
		if (rowCkr.isChanged())
			model.setRows(rowCkr.getNewValue());

		ChangeChecker colCkr = new ChangeChecker(getCols(), cols);
		if (colCkr.isChanged())
			model.setCols(colCkr.getNewValue());

		ChangeChecker tileCkr = new ChangeChecker(getTileSize(), tile);
		if (tileCkr.isChanged())
			model.setTileSize(tileCkr.getNewValue());

		ChangeChecker brdrCkr = new ChangeChecker(getBorderSize(), border);
		if (brdrCkr.isChanged())
			model.setBorderSize(brdrCkr.getNewValue());

		ChangeChecker tempCkr = new ChangeChecker(getTemp(), temp);
		if (tempCkr.isChanged()) {
			// Update model with temperature change.
			model.setTemp(tempCkr.getNewValue());

			// Update Details Tab with temperature change.
			Cell cell = table.getCurrentCell();
			if (!cell.isBlank())
				detailsTabController.setSelected(cell);
		}

		// Update the PTable.
		final int ZFontSize = model.getZ().getSizeInt();
		final int symbolFontSize = model.getSymbol().getSizeInt();
		table.updateLayout(rowCkr, colCkr, tileCkr, brdrCkr, tempCkr, ZFontSize, symbolFontSize);
	}

	/**
	 * Reverses the order of the columns and repositions the cells.
	 */
	public void flipColumns() {
		table.flipColumns(model.getStepSize(), model.getBorderSize());
	}

	/**
	 * Reverses the order of the rows and then repositions the cells.
	 */
	public void flipRows() {
		table.flipRows(model.getStepSize(), model.getBorderSize());
	}

	public Quantities getQuantities() {
		return table.getQuantities();
	}

	public void setSelected(Cell cell) {
		detailsTabController.setSelected(cell);
	}
}
