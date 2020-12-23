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
 * StatusController is a class that is responsible for handling the control 
 * of the Status tab.
 */
package phillockett65.PTable;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import phillockett65.PTable.table.Quantities;

public class StatusController {

	private MainController main;
	@FXML private Button btnStatusUpdate;
	@FXML private Label datStatusElementCount;
	@FXML private Label datStatusNeighbourCount;
	@FXML private Label datStatusElectronShell;
	@FXML private Label datStatusElectronSubshell;
	@FXML private Label datStatusElectronConfig;

	/**
	 * Constructor.
	 */
	public StatusController() {
//		System.out.println("StatusController constructed.");
	}

	/**
	 * Called by the FXML mechanism to initialize the controller.
	 */
	@FXML public void initialize() {
//		System.out.println("StatusController initialized.");
	}

	/**
	 * Event handler for the Status tab Update button.
	 * 
	 * @param event triggered by button click.
	 */
	@FXML void btnStatusUpdateClicked(ActionEvent event) {
//		System.out.println("Update Status.");
		updateSettings();
	}

	/**
	 * Apply the changes selected by the user.
	 */
	public void updateSettings() {
		Quantities quantities = main.getQuantities();
		datStatusElementCount.setText(quantities.getElementCount());
		datStatusNeighbourCount.setText(quantities.getNeighbourCount());
		datStatusElectronShell.setText(quantities.getElectronShellSimilarity());
		datStatusElectronSubshell.setText(quantities.getElectronSubshellSimilarity());
		datStatusElectronConfig.setText(quantities.getElectronConfigSimilarity());
	}

	/**
	 * Called by the centralized controller to provide a callback.
	 * 
	 * @param mainController used to call the centralized controller.
	 */
	public void init(MainController mainController) {
//		System.out.println("StatusController init() called.");
		main = mainController;
	}
}

