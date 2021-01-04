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

	private PTable table;
	private Selection selection;

	private KeyState up;
	private KeyState down;
	private KeyState left;
	private KeyState right;

	// Key->Command Map to handled key presses. Inspired by 
	// https://programming.guide/java/function-pointers-in-java.html
	private KeyAction action;
	private Map<KeyCode, Runnable> pressess;
	private Map<KeyCode, Runnable> releases;

	/**
	 * Constructor.
	 * 
	 * Responsible for creating the user selection and initializing the link 
	 * back to table.
	 * 
	 * @param table - the link to table.
	 */
	public KeyHandler(PTable table, Selection sel) {
//		System.out.println("KeyHandler constructed.");
		this.table = table;

		up = new KeyState();
		down = new KeyState();
		left = new KeyState();
		right = new KeyState();

		selection = sel;

		// Add Key to Command Mappings.
		action = new KeyAction();

		pressess = new HashMap<>();
		pressess.put(KeyCode.SHIFT, () -> setShift(action, true));
		pressess.put(KeyCode.CONTROL, () -> setControl(action, true));
		pressess.put(KeyCode.UP, () -> handleUp(action));
		pressess.put(KeyCode.DOWN, () -> handleDown(action));
		pressess.put(KeyCode.LEFT, () -> handleLeft(action));
		pressess.put(KeyCode.RIGHT, () -> handleRight(action));

		releases = new HashMap<>();
		releases.put(KeyCode.SHIFT, () -> setShift(action, false));
		releases.put(KeyCode.CONTROL, () -> setControl(action, false));
		releases.put(KeyCode.UP, () -> upReleased());
		releases.put(KeyCode.DOWN, () -> downReleased());
		releases.put(KeyCode.LEFT, () -> leftReleased());
		releases.put(KeyCode.RIGHT, () -> rightReleased());
	}

	/**
	 * Request PTable to update the Window Title to reflect the Shift and 
	 * Control key states.
	 */
	private void updateTitle(KeyAction action) {
		if (action.isSelecting()) {
			table.augmentTitle(" - MULTI SELECT");
			return;
		}

		if (action.isMoving()) {
			table.augmentTitle(" - MOVE SELECTION");
			return;
		}

		table.augmentTitle("");
	}

	/**
	 * Update the shift state, the current action and the Window Title.
	 */
	private void setShift(KeyAction action, boolean pressed) {
//		System.out.println("setShift(" + pressed + ")");
		action.setShift(pressed);
		updateTitle(action);
	}

	/**
	 * Update the control state, the current action and the Window Title.
	 */
	private void setControl(KeyAction action, boolean pressed) {
//		System.out.println("setControl(" + pressed + ")");
		action.setControl(pressed);
		updateTitle(action);
	}

	/**
	 * Lock in the current position if we are not creating a selection.
	 */
	private void saveCurrent(KeyAction action) {
		if (action.isSelecting())
			return;
		selection.saveCurrent();
	}

	/**
	 * Handle up arrow key press.
	 */
	private void handleUp(KeyAction action) {
//		System.out.println("handleUp()");
		if (!up.setPressed(true))
			return;		// Ignore key repeat.

		if (action.isMoving()) {
			if (selection.isMoveUp()) {
				table.moveSelection(KeyCode.UP);
				selection.moveUp();
			}

		} else {
			if (selection.isPositionUp()) {
				table.highlightSelectedCells(false);
				selection.positionUp();
				saveCurrent(action);
				table.highlightSelectedCells(true);
			}
		}
	}

	/**
	 * Handle up arrow key release.
	 */
	private void upReleased() {
		up.setPressed(false);
	}

	/**
	 * Handle down arrow key press.
	 */
	private void handleDown(KeyAction action) {
//		System.out.println("handleDown()");
		if (!down.setPressed(true))
			return;		// Ignore key repeat.

		if (action.isMoving()) {
			if (selection.isMoveDown()) {
				table.moveSelection(KeyCode.DOWN);
				selection.moveDown();
			}

		} else {
			if (selection.isPositionDown()) {
				table.highlightSelectedCells(false);
				selection.positionDown();
				saveCurrent(action);
				table.highlightSelectedCells(true);
			}
		}
	}

	/**
	 * Handle down arrow key release.
	 */
	private void downReleased() {
		down.setPressed(false);
	}

	/**
	 * Handle left arrow key press.
	 */
	private void handleLeft(KeyAction action) {
//		System.out.println("handleLeft()");
		if (!left.setPressed(true))
			return;		// Ignore key repeat.

		if (action.isMoving()) {
			if (selection.isMoveLeft()) {
				table.moveSelection(KeyCode.LEFT);
				selection.moveLeft();
			}

		} else {
			if (selection.isPositionLeft()) {
				table.highlightSelectedCells(false);
				selection.positionLeft();
				saveCurrent(action);
				table.highlightSelectedCells(true);
			}
		}
	}

	/**
	 * Handle left arrow key release.
	 */
	private void leftReleased() {
		left.setPressed(false);
	}

	/**
	 * Handle right arrow key press.
	 */
	private void handleRight(KeyAction action) {
//		System.out.println("handleRight()");
		if (!right.setPressed(true))
			return;		// Ignore key repeat.

		if (action.isMoving()) {
			if (selection.isMoveRight()) {
				table.moveSelection(KeyCode.RIGHT);
				selection.moveRight();
			}

		} else {
			if (selection.isPositionRight()) {
				table.highlightSelectedCells(false);
				selection.positionRight();
				saveCurrent(action);
				table.highlightSelectedCells(true);
			}
		}
	}

	/**
	 * Handle right arrow key release.
	 */
	private void rightReleased() {
		right.setPressed(false);
	}

	@Override
	public void handle(KeyEvent e) {
		final KeyCode key = e.getCode();

		if (e.getEventType() == KeyEvent.KEY_PRESSED)
			if (pressess.containsKey(key))
				pressess.get(key).run();

		if (e.getEventType() == KeyEvent.KEY_RELEASED)
			if (releases.containsKey(key))
				releases.get(key).run();
	}
	
}
