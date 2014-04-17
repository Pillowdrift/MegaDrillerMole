package com.pillowdrift.drillergame.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.pillowdrift.drillergame.framework.GameEntity;
import com.pillowdrift.drillergame.framework.Sprite;
import com.pillowdrift.drillergame.framework.SpriteCollection;
import com.pillowdrift.drillergame.scenes.GameScene;

public class Drill extends GameEntity {
	
	Player _player;

	public Drill(GameScene parent, Player player) 
	{
		super(parent);
		
		// Set up the sprite for this drill
		setupGraphics();
		
		//Set origin
		_originX = 32.0f;
		_originY = 32.0f;
		
		//Set scale
		_scaleX = 1.0f;
		_scaleY = 1.0f;	
		
		_velocity = player.getVelocity();
		
		_player = player;
	}
	
	public void setupGraphics() 
	{
		//Get graphics
		_sprites.addSprite("transparent", new Sprite(_parent.getResourceManager().getAtlasRegion("atlas01", "playerForward"), 1, 0.0f));
		_sprites.addSprite("forward", new Sprite(_parent.getResourceManager().getAtlasRegion("atlas01", "playerForward"), 64, 24.0f));
		_sprites.addSprite("air", new Sprite(_parent.getResourceManager().getAtlasRegion("atlas01", "playerAir"), 64, 0.0f));
		_sprites.setCurrentSpriteName("forward");
	}
	
	@Override
	public void update(float dt) 
	{
		super.update(dt);
		
		_x = _player.getPosX();
		_y = _player.getPosY();
		
		//_velocity = _player.getVelocity();
		
		_rotation = _player.getRotation();
		
		_color = _player.getColor();
	}

	
	public void deadDraw(SpriteBatch spriteBatch)
	{
		//Draw darkenned
		Sprite cur = _sprites.getCurrentSprite();
		spriteBatch.setColor(0.4f, 0.4f, 0.4f, 1.0f);		
		spriteBatch.draw(cur.getCurrentFrame(), _x-_originX, _y-_originY, _originX, _originY, cur.getWidth(), cur.getHeight(), _scaleX, _scaleY, _rotation);
	}
	
	public SpriteCollection getSprites()
	{
		return _sprites;
	}

	public float drillSize() {
		return 0;
	}

	public float gravityModifier() {
		return 1;
	}

	public float boostModifier() {
		return 1;
	}
	
}
