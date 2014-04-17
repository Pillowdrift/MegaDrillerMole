package com.pillowdrift.drillergame.scenes;

import com.badlogic.gdx.Gdx;
import com.pillowdrift.drillergame.framework.Scene;
import com.pillowdrift.drillergame.framework.SceneManager;

public class DebugScene extends Scene {
	// Vars
	
	/**
	 * Create a new debug scene.
	 * @param owner
	 */
	public DebugScene(SceneManager owner) {
		super(owner);
		
		// Definately be on top.
		_depth = -100000;
		
		activate();
	}
	
	@Override
	public void update(float dt) {
		super.update(dt);
	}
	
	@Override
	public void draw() {
		super.draw();
		
		// Draw the frame rate.
		_spriteBatch.begin();
		getResourceManager().getFont("OSP-DIN").setScale(1.0f);
		getResourceManager().getFont("OSP-DIN").draw(_spriteBatch,
								"FPS: " + Float.toString(1.0f / Gdx.graphics.getDeltaTime()), _targetWidth - 300, 50);
		_spriteBatch.end();
	}
}
