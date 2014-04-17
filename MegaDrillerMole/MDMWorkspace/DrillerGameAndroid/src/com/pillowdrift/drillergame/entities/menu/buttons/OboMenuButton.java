package com.pillowdrift.drillergame.entities.menu.buttons;

import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.pillowdrift.drillergame.entities.menu.MenuButton;
import com.pillowdrift.drillergame.framework.Scene;
import com.pillowdrift.drillergame.framework.Sprite;

/**
 * Button to direct you to the Pillowdrift website.
 * It's also a picture of Obo, the telekinetic ghost-seal.
 * @author cake_cruncher_7
 *
 */
public class OboMenuButton extends MenuButton
{
	//CONSTRUCTION
	public OboMenuButton(Scene parent) {
		super(parent, "");
	}

	//FUNCTION
	@Override
	protected void onRelease()
	{
		super.onRelease();
		
		//Open the Pillowdrift website
		_parent.getOwner().getGame().getWebBrowserService().openUrl("http://pillowdrift.com");
	}
	
	//Sprite functions
	@Override
	protected Sprite getNormalSprite()
	{
		AtlasRegion texRegion = _parent.getResourceManager().getAtlasRegion("atlas01", "buttonObo");
		return new Sprite(texRegion, texRegion.getRegionWidth(), 0.0f);
	}
	@Override
	protected Sprite getOverlaySprite()
	{
		AtlasRegion texRegion = _parent.getResourceManager().getAtlasRegion("atlas01", "buttonOboverlay");
		return new Sprite(texRegion, texRegion.getRegionWidth(), 0.0f);
	}
}
