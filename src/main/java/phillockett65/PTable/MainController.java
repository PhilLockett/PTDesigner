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

	public Color getStateColour(int index) {
		Color ret = model.getStateColour(index);
		return ret;
	}

	public boolean setStateColour(int index, Color value) {
		boolean ret = model.setStateColour(index, value);
		if (ret)
			table.setStateColour(index);
		return ret;
	}

	public String getStateString(int index) {
		String ret = model.getStateString(index);
		return ret;
	}

	public boolean setStateString(int index, String value) {
		boolean ret = model.setStateString(index, value);
		return ret;
	}

	public Color getSubcategoryColour(int index) {
		Color ret = model.getSubcategoryColour(index);
		return ret;
	}

	public boolean setSubcategoryColour(int index, Color value) {
		boolean ret = model.setSubcategoryColour(index, value);
		if (ret)
			table.setSubcategoryColour(index);
		return ret;
	}

	public String getSubcategoryString(int index) {
		String ret = model.getSubcategoryString(index);
		return ret;
	}

	public boolean setSubcategoryString(int index, String value) {
		boolean ret = model.setSubcategoryString(index, value);
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

	public void setRows(int value) {
		model.setRows(value);
	}

	public void setCols(int value) {
		model.setCols(value);
	}

	public void setTileSize(int value) {
		model.setTileSize(value);
	}

	public void setBorderSize(int value) {
		model.setBorderSize(value);
	}

	public void setTemp(int value) {
		model.setTemp(value);
	}

	public void redrawTable(boolean gridChanged, boolean sizeChanged, boolean tempChanged) {
		if (tempChanged)
			table.updateState();

		if (gridChanged)
			table.gridChange();

		if (sizeChanged)
			table.sizeChange();

		table.drawTable();
	}

	public void flipColumns() {
		table.flipColumns();
	}

	public void flipRows() {
		table.flipRows();
	}

	public Quantities getQuantities() {
		return table.getQuantities();
	}

	public void setSelected(Cell cell) {
		detailsTabController.setSelected(cell);
	}
}
