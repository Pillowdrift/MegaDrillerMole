package com.pillowdrift.drillergame.scenes;

import com.badlogic.gdx.Gdx;
import com.pillowdrift.drillergame.entities.Slideshow;
import com.pillowdrift.drillergame.entities.menu.buttons.GenericMenuButton;
import com.pillowdrift.drillergame.entities.menu.buttons.TutorialNextButton;
import com.pillowdrift.drillergame.framework.Scene;

import com.pillowdrift.drillergame.framework.SceneManager;
/**
 * Represents the scene in which the tutorial takes place.
 * @author bombpersons
 *
 */
public class TutorialScene extends Scene
{
	// Vars
	GenericMenuButton _next;
	Slideshow _slideshow;
	
	/**
	 * Create a new tutorial scene.
	 * @param owner
	 */
	public TutorialScene(SceneManager owner)
	{
		super(owner);
		
		// Catch back button so we handle it manually
		Gdx.input.setCatchBackKey(true);
		
		// Add the slideshow
		_slideshow = new Slideshow(this, new String[] {
														"data/graphics/tutorial1.png",
														"data/graphics/tutorial2.png",
														"data/graphics/tutorial3.png"
													  });
		addEntity("Slideshow", _slideshow);
		
		// Add the next button.
		_next = new TutorialNextButton(this, _slideshow);
		_next.setScale(1.3f, 1.3f);
		_next.setPos(_targetWidth * 0.8f, _targetHeight * 0.10f);
		addEntity("Next", _next);
	}
	
	@Override
	public void update(float dt)
	{
		super.update(dt);
		
		if (getInputManager().isBackPressed())
		{
			getOwner().getScene("MenuScene").activate();
			this.deactivate();
		}
	}
	
	// Make sure the slideshow loads and unloads its data.
	@Override
	public void onActivate() {
		super.onActivate();
		
		// Deactivate the gamescene.
		_owner.getScene("GameScene").deactivate();
		_slideshow.load();
	}
	
	public void onDeactivate() {
		// Activate the gamescene
		_owner.getScene("GameScene").activate();
		_slideshow.unload();
	}
}
