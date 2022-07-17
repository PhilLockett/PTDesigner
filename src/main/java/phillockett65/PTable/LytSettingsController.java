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
 * LytSettingsController is a class that is responsible for handling the control 
 * of the Layout Settings tab.
 */
package phillockett65.PTable;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;

public class LytSettingsController {

	private MainController main;
	@FXML private Spinner<Integer> spnLytRows;
	@FXML private Spinner<Integer> spnLytColumns;
	@FXML private Spinner<Integer> spnLytTile;
	@FXML private Spinner<Integer> spnLytBorder;
	@FXML private Spinner<Integer> spnLytTemp;
	@FXML private Button btnLytColFlip;
	@FXML private Button btnLytRowFlip;
	@FXML private Button btnLytSettings;

	/**
	 * Constructor.
	 */
	public LytSettingsController() {
//		System.out.println("LytSettingsController constructed.");
	}

	/**
	 * Called by the FXML mechanism to initialize the controller.
	 */
	@FXML public void initialize() {
//		System.out.println("LytSettingsController initialized.");

	}

	/**
	 * Event handler for the Layout Settings tab Flip Columns button.
	 * 
	 * @param event triggered by button click.
	 */
	@FXML void btnSettingsFlipColumns(ActionEvent event) {
//		System.out.println("Flip Columns.");
		main.flipColumns();
	}

	/**
	 * Event handler for the Layout Settings tab Flip Rows button.
	 * 
	 * @param event triggered by button click.
	 */
	@FXML void btnSettingsFlipRows(ActionEvent event) {
//		System.out.println("Flip Rows.");
		main.flipRows();
	}

	/**
	 * Event handler for the Layout Settings tab Apply Change button.
	 * 
	 * @param event triggered by button click.
	 */
	@FXML void btnSettingsApplyClicked(ActionEvent event) {
//		System.out.println("Applying Change.");
		updateSettings();
	}

	/**
	 * Apply the changes selected by the user. Keeps track of which Spinners 
	 * has changed and which haven't.
	 */
	private void updateSettings() {
		int rows = Integer.parseInt(spnLytRows.getValue().toString());
		int cols = Integer.parseInt(spnLytColumns.getValue().toString());
		int tile = Integer.parseInt(spnLytTile.getValue().toString());
		int brdr = Integer.parseInt(spnLytBorder.getValue().toString());
		int temp = Integer.parseInt(spnLytTemp.getValue().toString());

		main.updateLayout(rows, cols, tile, brdr, temp);
	}


	/**
	 * Called by the centralized controller to provide a callback.
	 * 
	 * @param mainController used to call the centralized controller.
	 */
	public void init(MainController mainController) {
//		System.out.println("LytSettingsController init() called.");
		main = mainController;
		initSpnSettings();
	}

	/**
	 * Initialize all the Spinners.
	 */
	private void initSpnSettings() {
		SpinnerValueFactory<Integer> vFRows = new SpinnerValueFactory.IntegerSpinnerValueFactory(8, 45, main.getRows());
		spnLytRows.setValueFactory(vFRows);

		SpinnerValueFactory<Integer> vFColumns = new SpinnerValueFactory.IntegerSpinnerValueFactory(18, 45, main.getCols());
		spnLytColumns.setValueFactory(vFColumns);

		SpinnerValueFactory<Integer> vFTile = new SpinnerValueFactory.IntegerSpinnerValueFactory(25, 100, main.getTileSize());
		spnLytTile.setValueFactory(vFTile);

		SpinnerValueFactory<Integer> vFBorder = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 10, main.getBorderSize());
		spnLytBorder.setValueFactory(vFBorder);

		SpinnerValueFactory<Integer> vFTemp = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, main.getMaxTemp(), main.getTemp());
		spnLytTemp.setValueFactory(vFTemp);
	}
}
