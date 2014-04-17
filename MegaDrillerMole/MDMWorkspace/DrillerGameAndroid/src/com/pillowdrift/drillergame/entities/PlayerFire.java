package com.pillowdrift.drillergame.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.pillowdrift.drillergame.framework.GameEntity;
import com.pillowdrift.drillergame.framework.Sprite;
import com.pillowdrift.drillergame.scenes.GameScene;

public class PlayerFire extends GameEntity {
	// Vars
	public static final float MAX_SPEED = 1000.0f;
	
	/**
	 * Create a new player fire.
	 * @param parent
	 */
	public PlayerFire(GameScene parent) {
		super(parent);
		
		_sprites.addSprite("Fire", new Sprite(
				_parent.getResourceManager().getAtlasRegion("atlas01", "fire"), 
				128, 20)
		);
		_sprites.setCurrentSpriteName("Fire");
		_originX = 64;
		_originY = 64;	
	}
	
	@Override
	public void update(float dt) {
		// TODO Auto-generated method stub
		super.update(dt);
		
		// Make sure we are at the same position as the player.
		Player player = (Player)_parent.getEntityFirst("Player");
		if (player == null) {
			_removeFlag = true;
			return;
		}
		
		setPos(player.getPosX(), player.getPosY());
		_rotation = player.getVelocity().angle();
		float alpha = 0.0f;
		float speed = player.getVelocity().len();
		if (speed > MAX_SPEED) {
			alpha = 1.0f;
		} else {
			alpha = speed / MAX_SPEED;
		}
		if (!player.onSurface())
			alpha = 0;
		
		setColor(Color.WHITE.cpy().mul(alpha));
	}
	
	public void draw(SpriteBatch spriteBatch) {
		super.draw(spriteBatch);
	}
}
