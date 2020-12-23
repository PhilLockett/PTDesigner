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
 * DetailsController is a class that is responsible for handling the control 
 * of the Details tab.
 */
package phillockett65.PTable;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import phillockett65.PTable.elements.ElementConfig;
import phillockett65.PTable.elements.Elements;
import phillockett65.PTable.table.Cell;

public class DetailsController {

	private MainController main;
	@FXML private Label datDetailsElementName;
	@FXML private Label datDetailsElementSymbol;
	@FXML private Label datDetailsAtomicNumber;
	@FXML private Label datDetailsGroup;
	@FXML private Label datDetailsGroup32;
	@FXML private Label datDetailsPeriod;
	@FXML private Label datDetailsAtomicWeight;
	@FXML private Label datDetailsDensity;
	@FXML private Label datDetailsMeltingPoint;
	@FXML private Label datDetailsBoilingPoint;
	@FXML private Label lblDetailsState;
	@FXML private Label datDetailsState;
	@FXML private Label datDetailsSpecificHeat;
	@FXML private Label datDetailsElectronegativity;
	@FXML private Label datDetailsSubcategory;
	@FXML private Label datDetailsElectronShell;
	@FXML private Label datDetailsElectronSubshell;
	@FXML private Label datDetailsElectronConfiguration;

	/**
	 * Constructor.
	 */
	public DetailsController() {
//		System.out.println("DetailsController constructed.");
	}

	/**
	 * Called by the FXML mechanism to initialize the controller.
	 */
	@FXML public void initialize() {
//		System.out.println("DetailsController initialized.");
	}

	/**
	 * Construct a String to represent Electron Shells of the given Element.
	 * 
	 * @param e the Element of interest.
	 * @return the String representing the Electron Shells. 
	 */
	private String genElectronShellsString(ElementConfig e) {
		String ret = "";
		for (int s = 0; s < e.getElectronShellCounts().length; ++s)
			ret += String.valueOf(e.getElectronShellCounts()[s]) + ' ';

		return ret;
	}

	/**
	 * Construct a String to represent Electron Subshells of the given Element.
	 * 
	 * @param e the Element of interest.
	 * @return the String representing the Electron Subshells. 
	 */
	private String genElectronSubshellsString(ElementConfig e) {
		String ret = "";
		for (int s = 0; s < e.getElectronSubshellCounts().length; ++s)
			ret += Elements.getElectronSubshellString(s) + String.valueOf(e.getElectronSubshellCounts()[s]) + ' ';

		return ret;
	}

	/**
	 * Construct a String to represent Electron Configuration of the given 
	 * Element.
	 * 
	 * @param e the Element of interest.
	 * @return the String representing the Electron Configuration. 
	 */
	private String genElectronConfigurationString(ElementConfig e) {
		String ret = "";
		for (int s = 0; s < e.getElectronConfigurationCounts().length; ++s) {
			final int count = e.getElectronConfigurationCounts()[s];
			if (count != 0)
				ret += Elements.getElectronConfigString(s)+ String.valueOf(count) + ' ';
		}

		return ret;
	}

	/**
	 * Display the chemical elements details associated with the given cell.
	 * 
	 * @param cell details to display.
	 */
	public void setSelected(Cell cell) {

		final ElementConfig e = cell.getE();
		datDetailsElementName.setText(e.getName());
		datDetailsElementSymbol.setText(e.getSymbol());
		datDetailsAtomicNumber.setText(String.valueOf(e.getZ()));
		datDetailsGroup.setText(String.valueOf(e.getGroup()));
		datDetailsGroup32.setText(String.valueOf(e.getGroup32()));
		datDetailsPeriod.setText(String.valueOf(e.getPeriod()));
		datDetailsAtomicWeight.setText(String.valueOf(e.getAtomicWeight()));
		datDetailsDensity.setText(String.valueOf(e.getDensity()));
		datDetailsMeltingPoint.setText(String.valueOf(e.getMelt()));
		datDetailsBoilingPoint.setText(String.valueOf(e.getBoil()));

		lblDetailsState.setText("State (at " + String.valueOf(main.getTemp()) + "K):");
		datDetailsState.setText(main.getStateString(cell.getState()));

		datDetailsSpecificHeat.setText(String.valueOf(e.getC()));
		datDetailsElectronegativity.setText(String.valueOf(e.getX()));
		datDetailsSubcategory.setText(main.getSubcategoryString(e.getSubcategory()));

		datDetailsElectronShell.setText(genElectronShellsString(e));
		datDetailsElectronSubshell.setText(genElectronSubshellsString(e));
		datDetailsElectronConfiguration.setText(genElectronConfigurationString(e));
	}

	/**
	 * Called by the centralized controller to provide a callback.
	 * 
	 * @param mainController used to call the centralized controller.
	 */
	public void init(MainController mainController) {
//		System.out.println("DetailsController init() called.");
		main = mainController;
	}
}

