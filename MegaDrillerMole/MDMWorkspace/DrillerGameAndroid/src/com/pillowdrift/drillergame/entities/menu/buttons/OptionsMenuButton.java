package com.pillowdrift.drillergame.entities.menu.buttons;

import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.pillowdrift.drillergame.entities.menu.MenuButton;
import com.pillowdrift.drillergame.framework.Scene;
import com.pillowdrift.drillergame.framework.Sprite;
/**
 * Menu button to open up the options scene.
 * @author strawberries
 *
 */
public class OptionsMenuButton extends MenuButton {

	//CONSTRUCTION
	public OptionsMenuButton(Scene parent) {
		super(parent, "");
	}

	//FUNCTION
	@Override
	protected void onRelease()
	{
		super.onRelease();
		
		//Activate the options scene
		_parent.getOwner().getScene("OptionsScene").activate();
		//Deactivate our parent scene
		_parent.deactivate();
	}
	
	//Sprite functions
	@Override
	protected Sprite getNormalSprite()
	{
		AtlasRegion texRegion = _parent.getResourceManager().getAtlasRegion("atlas01", "spanner");
		return new Sprite(texRegion, texRegion.getRegionWidth(), 0.0f);
	}
	@Override
	protected Sprite getOverlaySprite()
	{
		AtlasRegion texRegion = _parent.getResourceManager().getAtlasRegion("atlas01", "spanneroverlay");
		return new Sprite(texRegion, texRegion.getRegionWidth(), 0.0f);
	}
}
