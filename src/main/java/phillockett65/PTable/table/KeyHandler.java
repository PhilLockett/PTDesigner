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
 * KeyHandler is a class that is responsible for handling key pressing, 
 * managing the user selection and requestingPTable to highlight and redraw 
 * the grid.
 */
package phillockett65.PTable.table;

import java.util.HashMap;
import java.util.Map;

import javafx.event.EventHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class KeyHandler implements EventHandler<KeyEvent> {

	private final int NEITHER = 0;
	private final int SELECTING = 1;
	private final int MOVING = 2;
	private int action = NEITHER;

	private PTable table;
	private Grid grid;
	private Selection selection;

	private KeyState shift;
	private KeyState control;
	private KeyState up;
	private KeyState down;
	private KeyState left;
	private KeyState right;

	// Key->Command Map to handled key presses. Inspired by 
	// https://programming.guide/java/function-pointers-in-java.html
	boolean keyPressed = false;
	private Map<KeyCode, Runnable> commands = new HashMap<>();

	/**
	 * Constructor.
	 * 
	 * Responsible for creating the user selection and initializing the link 
	 * back to table.
	 * 
	 * @param table - the link to table.
	 */
	public KeyHandler(PTable table) {
//		System.out.println("KeyHandler constructed.");
		this.table = table;

		shift = new KeyState();
		control = new KeyState();
		up = new KeyState();
		down = new KeyState();
		left = new KeyState();
		right = new KeyState();

		selection = new Selection();

		// Add Key to Command Mappings.
		commands.put(KeyCode.SHIFT, () -> setShift());
		commands.put(KeyCode.CONTROL, () -> setControl());
		commands.put(KeyCode.UP, () -> handleUp());
		commands.put(KeyCode.DOWN, () -> handleDown());
		commands.put(KeyCode.LEFT, () -> handleLeft());
		commands.put(KeyCode.RIGHT, () -> handleRight());
	}

	/**
	 * Make the current user selection accessible.
	 * 
	 * @return the current user selection.
	 */
	public Selection getSelection() {
		return selection;
	}

	/**
	 * Set the grid for the key handler to navigate.
	 * 
	 * @param grid to navigate.
	 */
	public void setGrid(Grid grid) {
		this.grid = grid;
	}

	/**
	 * Request PTable to update the Window Title to reflect the Shift and 
	 * Control key states.
	 */
	private void updateTitle() {
		switch (action) {
		case SELECTING:
			table.augmentTitle(" - MULTI SELECT");
			break;

		case MOVING:
			table.augmentTitle(" - MOVE SELECTION");
			break;

		default:
			table.augmentTitle("");
			break;
		}
	}

	/**
	 * Update the shift state, the current action and the Window Title.
	 */
	private void setShift() {
//		System.out.println("setShift(" + pressed + ")");
		shift.setPressed(keyPressed);
		if (shift.isPressed()) {
			if (action == NEITHER) {
				action = SELECTING;
			}
		} else {
			if (control.isPressed())
				action = MOVING;
			else
				action = NEITHER;
		}
		updateTitle();
	}

	/**
	 * Update the control state, the current action and the Window Title.
	 */
	private void setControl() {
//		System.out.println("setControl(" + pressed + ")");
		control.setPressed(keyPressed);
		if (control.isPressed()) {
			if (action == NEITHER)
				action = MOVING;
		} else {
			if (shift.isPressed())
				action = SELECTING;
			else
				action = NEITHER;
		}
		updateTitle();
	}


	/**
	 * Lock in the current position if we are not creatingt a selection.
	 */
	private void saveCurrent() {
		if (action == SELECTING)
			return;
		selection.saveCurrent();
	}

	/**
	 * Handle up arrow key.
	 */
	private void handleUp() {
//		System.out.println("handleUp(" + pressed + ")");
		if (!up.setPressed(keyPressed))
			return;		// Ignore key repeat.

		if (!up.isPressed())
			return;		// Ignore key release.

		if (action == MOVING) {
			if (selection.getBottom() > 0) {
				table.moveSelection(KeyCode.UP);
				selection.moveUp();
			}

		} else {
			if (selection.getRow() > 0) {
				table.highlightSelectedCells(false);
				selection.positionUp();
				saveCurrent();
				table.highlightSelectedCells(true);
			}
		}
	}

	/**
	 * Handle down arrow key.
	 */
	private void handleDown() {
//		System.out.println("handleDown(" + pressed + ")");
		if (!down.setPressed(keyPressed))
			return;		// Ignore key repeat.

		if (!down.isPressed())
			return;		// Ignore key release.

		if (action == MOVING) {
			if (selection.getBottom() < grid.getRows()-1) {
				table.moveSelection(KeyCode.DOWN);
				selection.moveDown();
			}

		} else {
			if (selection.getRow() < grid.getRows()-1) {
				table.highlightSelectedCells(false);
				selection.positionDown();
				saveCurrent();
				table.highlightSelectedCells(true);
			}
		}
	}

	/**
	 * Handle left arrow key.
	 */
	private void handleLeft() {
//		System.out.println("handleLeft(" + pressed + ")");
		if (!left.setPressed(keyPressed))
			return;		// Ignore key repeat.

		if (!left.isPressed())
			return;		// Ignore key release.

		if (action == MOVING) {
			if (selection.getLeft() > 0) {
				table.moveSelection(KeyCode.LEFT);
				selection.moveLeft();
			}

		} else {
			if (selection.getCol() > 0) {
				table.highlightSelectedCells(false);
				selection.positionLeft();
				saveCurrent();
				table.highlightSelectedCells(true);
			}
		}
	}

	/**
	 * Handle right arrow key.
	 */
	private void handleRight() {
//		System.out.println("handleRight(" + pressed + ")");
		if (!right.setPressed(keyPressed))
			return;		// Ignore key repeat.

		if (!right.isPressed())
			return;		// Ignore key release.

		if (action == MOVING) {
			if (selection.getRight() < grid.getCols()-1) {
				table.moveSelection(KeyCode.RIGHT);
				selection.moveRight();
			}

		} else {
			if (selection.getCol() < grid.getCols()-1) {
				table.highlightSelectedCells(false);
				selection.positionRight();
				saveCurrent();
				table.highlightSelectedCells(true);
			}
		}
	}

	/**
	 * Call the individual key handlers for the keys of interest.
	 * 
	 * @param key		- the KeyCode of the key pressed/released.
	 * @param pressed	- true if key is pressed, false otherwise.
	 */
	private void handleKey(KeyCode key, boolean pressed) {

		keyPressed = pressed;
		if (commands.containsKey(key))
			commands.get(key).run();
	}

	@Override
	public void handle(KeyEvent e) {
		if (e.getEventType() == KeyEvent.KEY_PRESSED)
			handleKey(e.getCode(), true);
		if (e.getEventType() == KeyEvent.KEY_RELEASED)
			handleKey(e.getCode(), false);
	}

}
