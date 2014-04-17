package com.pillowdrift.drillergame.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.pillowdrift.drillergame.framework.GameEntity;
import com.pillowdrift.drillergame.framework.Sprite;
import com.pillowdrift.drillergame.scenes.GameScene;

public class PlayerAura extends GameEntity {
	// Vars
	float _rot;
	public static final float MAX_AURA = 100000.0f;
	
	/**
	 * Create a new player aura.
	 * @param scene
	 */
	public PlayerAura(GameScene scene, float rotation) {
		super(scene);
		_rot = rotation;
		
		_sprites.addSprite("Still", new Sprite(
				_parent.getResourceManager().getAtlasRegion("atlas01", "aura"), 
				128, 0)
		);
		_sprites.setCurrentSpriteName("Still");
		_originX = 64;
		_originY = 64;
		
	}
	
	@Override
	public void update(float dt) {
		// TODO Auto-generated method stub
		super.update(dt);
		
		// Change the rotation.
		_rotation += _rot * dt;
		
		// Make sure we are at the same position as the player.
		Player player = (Player)_parent.getEntityFirst("Player");
		if (player == null) {
			_removeFlag = true;
			return;
		}
		
		setPos(player.getPosX(), player.getPosY());
		float alpha = 0.0f;
		if (_parentGameScene.getPlayer01Load() > MAX_AURA) {
			alpha = 1.0f;
		} else {
			alpha = _parentGameScene.getPlayer01Load() / MAX_AURA;
		}
		
		setColor(Color.WHITE.cpy().mul(alpha));
	}
	
	public void draw(SpriteBatch spriteBatch) {
		super.draw(spriteBatch);
	}
}
