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
 * SttSettingsController is a class that is responsible for handling the control 
 * of the State Settings tab.
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

public class SttSettingsController {

	private MainController main;
	private ObservableList<String> StatesList = FXCollections.observableArrayList("UNDEFINED");
	@FXML private TextField txtSttSettings;
	@FXML private Button btnSttSettings;
	@FXML private ChoiceBox<String> chcSttSettings;
	@FXML private ColorPicker colSttSettings;

	/**
	 * Constructor.
	 */
	public SttSettingsController() {
//		System.out.println("SettingsController constructed.");
	}

	/**
	 * Called by the FXML mechanism to initialize the controller. Initializes
	 * the ColorPicker and ChoiceBox including adding a listener.
	 */
	@FXML public void initialize() {
//		System.out.println("SettingsController initialized.");

		colSttSettings.getStyleClass().add(ColorPicker.STYLE_CLASS_BUTTON);
		chcSttSettings.setItems(StatesList);

		chcSttSettings.getSelectionModel().selectedItemProperty().addListener((v, oldValue, newValue) -> { 
//			System.out.println("chcSettings Change.");
			txtSttSettings.setText((String)newValue);
			int selected = chcSttSettings.getSelectionModel().getSelectedIndex();
			if (selected != -1)
				colSttSettings.setValue(main.getStateColour(selected));
		});
	}

	/**
	 * Event handler for the Status tab Apply Change button.
	 * 
	 * @param event triggered by button click.
	 */
	@FXML void btnSettingsApplyClicked(ActionEvent event) {
//		System.out.println("Applying Change.");
		updateSettings();
	}

	/**
	 * Apply the changes selected by the user.
	 */
	private void updateSettings() {
		String text = txtSttSettings.getText();
		if (text.length() < 1)
			return;

		final int selected = chcSttSettings.getSelectionModel().getSelectedIndex();
		final Color colour = colSttSettings.getValue();
		if (main.updateState(selected, text, colour)) {
			StatesList.set(selected, text);
			chcSttSettings.getSelectionModel().select(selected);
		}
	}

	/**
	 * Called by the centralized controller to provide a callback.
	 * 
	 * @param mainController used to call the centralized controller.
	 */
	public void init(MainController mainController) {
//		System.out.println("SettingsController init() called.");
		main = mainController;
		initChcSettings();
		initColSettings();
	}

	/**
	 * Initialize the ChoiceBox with data from the Model.
	 */
	private void initChcSettings() {
		int i = 0;
		StatesList.clear();
		for (String item = main.getStateString(i++); item != null; item = main.getStateString(i++)) {
			StatesList.add(item);
		}
		chcSttSettings.getSelectionModel().select(1);
	}

	/**
	 * Initialize the ColorPicker with data from the Model.
	 */
	private void initColSettings() {
		int selected = chcSttSettings.getSelectionModel().getSelectedIndex();
		Color item = main.getStateColour(selected);
		if (item != null)
			colSttSettings.setValue(item);
	}
}
