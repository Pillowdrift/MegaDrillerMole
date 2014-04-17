package com.pillowdrift.drillergame.entities;

import com.pillowdrift.drillergame.framework.Sprite;
import com.pillowdrift.drillergame.scenes.GameScene;

public class FeatherDrill extends Drill {

	public FeatherDrill(GameScene parent, Player player) {
		super(parent, player);
	}
	
	@Override
	public void setupGraphics() {
		//Get graphics
		_sprites.addSprite("transparent", new Sprite(_parent.getResourceManager().getAtlasRegion("atlas01", "playerForwardFeather"), 1, 0.0f));
		_sprites.addSprite("forward", new Sprite(_parent.getResourceManager().getAtlasRegion("atlas01", "playerForwardFeather"), 64, 24.0f));
		_sprites.addSprite("air", new Sprite(_parent.getResourceManager().getAtlasRegion("atlas01", "playerAirFeather"), 64, 0.0f));
		_sprites.setCurrentSpriteName("forward");
	}
	
	@Override
	public float gravityModifier() {
		return 0.6f;
	}

}
