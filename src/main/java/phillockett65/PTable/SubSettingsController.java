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
 * SubSettingsController is a class that is responsible for handling the control 
 * of the Subcategory Settings tab.
 */
package phillockett65.PTable;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;

public class SubSettingsController {

	private MainController main;
	private ObservableList<String> SubcategoryList = FXCollections.observableArrayList("UNDEFINED");
	@FXML private TextField txtSubSettings;
	@FXML private Button btnSubSettings;
	@FXML private ChoiceBox chcSubSettings;
	@FXML private ColorPicker colSubSettings;

	/**
	 * Constructor.
	 */
	public SubSettingsController() {
//		System.out.println("SubSettingsController constructed.");
	}

	/**
	 * Called by the FXML mechanism to initialize the controller. Initializes
	 * the ColorPicker and ChoiceBox including adding a listener.
	 */
	@FXML public void initialize() {
//		System.out.println("SubSettingsController initialized.");

		colSubSettings.getStyleClass().add(ColorPicker.STYLE_CLASS_BUTTON);
		chcSubSettings.setItems(SubcategoryList);

		chcSubSettings.getSelectionModel().selectedItemProperty().addListener((v, oldValue, newValue) -> { 
//			System.out.println("chcSettings Change.");
			txtSubSettings.setText((String)newValue);
			int selected = chcSubSettings.getSelectionModel().getSelectedIndex();
			if (selected != -1)
				colSubSettings.setValue(main.getSubcategoryColour(selected));
		});
	}

	/**
	 * Event handler for the Status tab Apply Change button.
	 * 
	 * @param event triggered by button click.
	 */
	@FXML void btnSubSettingsApplyClicked(ActionEvent event) {
//		System.out.println("Applying Change.");
		updateSettings();
	}

	/**
	 * Apply the changes selected by the user.
	 */
	private void updateSettings() {
		String text = txtSubSettings.getText();
		if (text.length() < 1)
			return;

		int selected = chcSubSettings.getSelectionModel().getSelectedIndex();
		if (main.setSubcategoryString(selected, text)) {
			main.setSubcategoryColour(selected, colSubSettings.getValue());
			SubcategoryList.set(selected, text);
			chcSubSettings.getSelectionModel().select(selected);
		}
	}

	/**
	 * Called by the centralized controller to provide a callback.
	 * 
	 * @param mainController used to call the centralized controller.
	 */
	public void init(MainController mainController) {
//		System.out.println("SubSettingsController init() called.");
		main = mainController;
		initChcSettings();
		initColSettings();
	}

	/**
	 * Initialize the ChoiceBox with data from the Model.
	 */
	private void initChcSettings() {
		int i = 0;
		SubcategoryList.clear();
		for (String item = main.getSubcategoryString(i++); item != null; item = main.getSubcategoryString(i++)) {
			SubcategoryList.add(item);
		}
		chcSubSettings.getSelectionModel().select(1);
	}

	/**
	 * Initialize the ColorPicker with data from the Model.
	 */
	private void initColSettings() {
		int selected = chcSubSettings.getSelectionModel().getSelectedIndex();
		Color item = main.getSubcategoryColour(selected);
		if (item != null)
			colSubSettings.setValue(item);
	}
}
