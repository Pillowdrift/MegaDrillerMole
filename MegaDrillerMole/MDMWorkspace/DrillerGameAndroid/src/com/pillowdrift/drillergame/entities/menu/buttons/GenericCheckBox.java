package com.pillowdrift.drillergame.entities.menu.buttons;

import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.pillowdrift.drillergame.entities.menu.CheckBox;
import com.pillowdrift.drillergame.framework.Scene;
import com.pillowdrift.drillergame.framework.Sprite;

public class GenericCheckBox extends CheckBox {
	public GenericCheckBox(Scene parent, String onText, String offText, boolean value) {
		super(parent, onText, offText, value);
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
