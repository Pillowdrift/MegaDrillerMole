package com.pillowdrift.drillergame.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.pillowdrift.drillergame.framework.Entity;
import com.pillowdrift.drillergame.framework.Scene;

public class Slideshow extends Entity {
	// The images to display
	String[] _images;
	Texture[] _textures;
	int _imagePointer;
	
	/**
	 * Create a new slideshow with an array of images.
	 * @param scene
	 * @param images
	 */
	public Slideshow(Scene scene, String[] images) {
		super(scene);
		_images = images;
		_textures = new Texture[_images.length];
		
		_depth = 10;
	}
	
	/**
	 * Load all the images into ram.
	 */
	public void load() {
		for (int i = 0; i < _images.length; ++i) {
			_textures[i] = new Texture(Gdx.files.internal(_images[i]));
		}
	}
	
	/**
	 * Unload all the images.
	 */
	public void unload() {
		for (int i = 0; i < _images.length; ++i) {
			_textures[i].dispose();
			_textures[i] = null;
		}	
	}
	
	/**
	 * Go to the next image.
	 */
	public void next() {
		_imagePointer = (_imagePointer + 1) % _images.length;
	}
	
	/**
	 * Whether or not this image is the last one.
	 * @return
	 */
	public boolean isLast() {
		return _imagePointer == _images.length - 1;
	}
	
	@Override
	public void draw(SpriteBatch spriteBatch) {
		// Draw the current image.
		spriteBatch.draw(_textures[_imagePointer], 0, 0);
	}
	
	@Override
	public void update(float dt) {
		super.update(dt);
	}
}
