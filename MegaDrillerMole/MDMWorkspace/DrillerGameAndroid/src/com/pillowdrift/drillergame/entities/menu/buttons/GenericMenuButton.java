package com.pillowdrift.drillergame.entities.menu.buttons;

import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.pillowdrift.drillergame.entities.menu.MenuButton;
import com.pillowdrift.drillergame.framework.Scene;
import com.pillowdrift.drillergame.framework.Sprite;

/**
 * Class to represent a typical menu button with a certain set of images.
 * @author cake_cruncher_7
 *
 */
public abstract class GenericMenuButton extends MenuButton {

	public GenericMenuButton(Scene parent, String text)
	{
		super(parent, text);
	}
	
	//Sprite functions
	@Override
	protected Sprite getNormalSprite()
	{
		AtlasRegion texRegion = _parent.getResourceManager().getAtlasRegion("atlas01", "button01");
		return new Sprite(texRegion, texRegion.getRegionWidth(), 0.0f);
	}
	@Override
	protected Sprite getOverlaySprite()
	{
		AtlasRegion texRegion = _parent.getResourceManager().getAtlasRegion("atlas01", "button01overlay");
		return new Sprite(texRegion, texRegion.getRegionWidth(), 0.0f);
	}
}
