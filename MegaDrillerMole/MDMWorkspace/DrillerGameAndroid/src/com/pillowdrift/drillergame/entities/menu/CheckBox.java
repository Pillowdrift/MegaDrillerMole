package com.pillowdrift.drillergame.entities.menu;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.pillowdrift.drillergame.framework.Scene;

public class CheckBox extends MenuButton {
	// Vars
	protected boolean _value; // The value of the checkbox.
	protected String _onText, _offText;
	
	/**
	 * Create a new check box.
	 * @param parent
	 * @param text
	 */
	public CheckBox(Scene parent, String onText, String offText, boolean value) {
		super(parent, value ? onText : offText);
		
		_value = value;
		_onText = onText;
		_offText = offText;
	}
	
	/**
	 * Called when the value is changed by the user.
	 */
	protected void onValueChange() {
	}
	
	@Override
	protected void onRelease() {
		super.onRelease();
		
		// Toggle value
		_value = !_value;
		_text01 = _value ? _onText : _offText;
		onValueChange();
	}
	
	@Override
	public void draw(SpriteBatch spriteBatch) {
		setColor(_value ? Color.ORANGE : Color.WHITE);
		super.draw(spriteBatch);
	}
}
