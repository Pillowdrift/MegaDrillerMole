package com.pillowdrift.drillergame.framework;

import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * Animated sprite class which will step through a given spritesheet at the given framerate.
 * @author cake_cruncher_7
 *
 */
public final class Sprite
{
	//DATA
	float _framesPerSecond;
	int _frameWidth;
	int _frameHeight;
	int _totalFrames;
	float _currentFrame;
	TextureRegion[][] _frames;
	AtlasRegion _texture;
	
	
	//ACCESS
	public AtlasRegion getTexture()
	{
		return _texture;
	}
	public TextureRegion getCurrentFrame()
	{
		return _frames[0][(int)_currentFrame];
	}
	public int getWidth()
	{
		return _frameWidth;
	}
	public int getHeight()
	{
		return _frameHeight;
	}
	public float getCurrentFrameNumber()
	{
		return _currentFrame;
	}
	public void setCurrentFrameNumber(float frame)
	{
		_currentFrame = frame;
	}
	public float getFPS()
	{
		return _framesPerSecond;
	}
	public void setFPS(float fps)
	{
		_framesPerSecond = fps;
	}
	
	
	
	//CONSTRUCTION - Give a texture and specify the width of each
	//frame. The height of a frame will be taken from the texture,
	//so frames should be organised horizontally.
	public Sprite(AtlasRegion texture, int frameWidth, float framesPerSecond)
	{
		//Constructor 
		_frameWidth = frameWidth;
		_frameHeight = texture.getRegionHeight();
		
		//Split the given texture into tiles of the given width and store them
		_frames = texture.split(_frameWidth, _frameHeight);
		
		//Store the framerate for animation
		_framesPerSecond = framesPerSecond;
		
		//Store the texture
		_texture = texture;
		
		//Set the total number of frames
		_totalFrames = _frames[0].length;
		
		//Set current frame to zero
		_currentFrame = 0.0f;
	}
	
	
	//FUNCTION
	public void update(float dt)
	{
		if(_framesPerSecond != 0.0f)
		{
			_currentFrame += dt*_framesPerSecond;
			
			while(_currentFrame >= _totalFrames)
			{
				_currentFrame = _currentFrame - _totalFrames;
			}
		}
	}
}
