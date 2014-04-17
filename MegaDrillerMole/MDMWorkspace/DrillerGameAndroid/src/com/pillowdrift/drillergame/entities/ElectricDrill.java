package com.pillowdrift.drillergame.entities;

import com.pillowdrift.drillergame.framework.Sprite;
import com.pillowdrift.drillergame.scenes.GameScene;

public class ElectricDrill extends Drill {

	public ElectricDrill(GameScene parent, Player player) {
		super(parent, player);		
		
	}
	
	@Override
	public void setupGraphics() {
		//Get graphics
		_sprites.addSprite("transparent", new Sprite(_parent.getResourceManager().getAtlasRegion("atlas01", "playerForwardGold"), 1, 0.0f));
		_sprites.addSprite("forward", new Sprite(_parent.getResourceManager().getAtlasRegion("atlas01", "playerForwardGold"), 64, 24.0f));
		_sprites.addSprite("air", new Sprite(_parent.getResourceManager().getAtlasRegion("atlas01", "playerAirGold"), 64, 0.0f));
		_sprites.setCurrentSpriteName("forward");
	}
	
	public float drillSize() {
		return 50.0f;
	}

}
