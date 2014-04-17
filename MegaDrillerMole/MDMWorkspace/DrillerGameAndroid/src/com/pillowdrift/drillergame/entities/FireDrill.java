package com.pillowdrift.drillergame.entities;

import com.pillowdrift.drillergame.framework.Sprite;
import com.pillowdrift.drillergame.scenes.GameScene;

public class FireDrill extends Drill {

	public FireDrill(GameScene parent, Player player) {
		super(parent, player);
	}
	
	@Override
	public void setupGraphics() {
		//Get graphics
		_sprites.addSprite("transparent", new Sprite(_parent.getResourceManager().getAtlasRegion("atlas01", "playerForwardRed"), 1, 0.0f));
		_sprites.addSprite("forward", new Sprite(_parent.getResourceManager().getAtlasRegion("atlas01", "playerForwardRed"), 64, 24.0f));
		_sprites.addSprite("air", new Sprite(_parent.getResourceManager().getAtlasRegion("atlas01", "playerAirRed"), 64, 0.0f));
		_sprites.setCurrentSpriteName("forward");
	}
	
	@Override
	public float boostModifier() {
		return 0.60f;
	}	

}
