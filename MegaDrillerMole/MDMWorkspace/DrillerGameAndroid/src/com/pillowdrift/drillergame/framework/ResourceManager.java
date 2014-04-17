package com.pillowdrift.drillergame.framework;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * Class to manage assets for the game, including audio, fonts and images.
 * @author cake_cruncher_7
 *
 */
public final class ResourceManager
{
	//DATA
	//Hash map to contain all loaded atlases
	private Map<String, TextureAtlas> _atlasMap;
	private Map<String, BitmapFont> _fontMap;
	private Map<String, Texture> _textures;
	private Map<String, Sound> _sounds;
	
	//CONSTRUCTION
	/**
	 * Basic constructor for resource manager.
	 * Initializes all collections.
	 */
	public ResourceManager()
	{
		_atlasMap = new HashMap<String, TextureAtlas>();
		_fontMap = new HashMap<String, BitmapFont>();
		_textures = new HashMap<String, Texture>();
		_sounds = new HashMap<String, Sound>();
	}
	
	//FUNCTION
	/**
	 * Loads an atlas from the given path and adds it to our collection.
	 * @param name used as the key to this atlas.
	 * @param path of the atlas.
	 * @return boolean indicating success or failure
	 */
	public boolean loadAtlas(String name, String path)
	{
		if(_atlasMap.containsKey(name)) return false; //Fail to load if the key already exists
		_atlasMap.put(name, new TextureAtlas(Gdx.files.internal(path))); //Otherwise, continue and try to load it
		return true; //Return true for success!
	}
	
	/**
	 * Load a texture from a given path.
	 * @param name
	 * @param path
	 * @return
	 */
	public boolean loadTexture(String name, String path) {
		if (_textures.containsKey(name)) return false;
		_textures.put(name,  new Texture(Gdx.files.internal(path)));
		return true;
	}
	
	/**
	 * Loads a font from a given fnt file and tries to build it from the given
	 * texture region before adding it to our collection.
	 * @param name used as the key to this atlas
	 * @param fntPath of the fnt file
	 * @param texReg to use for this font
	 * @return boolean indicating success or failure
	 */
	public boolean loadFont(String name, String fntPath, TextureRegion texReg)
	{
		if(_fontMap.containsKey(name)) return false; //Fail to load if the key already exists
		_fontMap.put(name, new BitmapFont(Gdx.files.internal(fntPath), texReg, false)); //Otherwise, continue and try to load it
		return true; //Return true for success!
	}
	
	/**
	 * Load a sound and put it into the hashmap.
	 * @param name
	 * @param path
	 * @return
	 */
	public boolean loadSound(String name, String path) {
		if (_sounds.containsKey(name)) return false;
		_sounds.put(name, Gdx.audio.newSound(Gdx.files.internal(path)));
		return true;
	}
	
	/**
	 * Returns the region associated with the given atlas name and region name.
	 * Note that this is not fast, so you better cache these. I won't say it twice.
	 * @param atlasName of the atlas you want to access.
	 * @param regionName of the region you want to find.
	 * @return
	 */
	public AtlasRegion getAtlasRegion(String atlasName, String regionName)
	{
		if(_atlasMap.containsKey(atlasName))
		{
			//Atlas key exists, try to return region
			return _atlasMap.get(atlasName).findRegion(regionName);
		}
		//Atlas key not found, return null;
		return null;
	}
	
	/**
	 * Returns the font associated with the given name.
	 * @param fontName
	 * @return
	 */
	public BitmapFont getFont(String fontName)
	{
		if(_fontMap.containsKey(fontName))
		{
			//Font key exists, try to return it
			return _fontMap.get(fontName);
		}
		//Font key not found, return null;
		return null;
	}
	
	/**
	 * Get a texture from the resource manager.
	 * @param textureName
	 * @return
	 */
	public Texture getTexture(String textureName) {
		if (_textures.containsKey(textureName)) {
			return _textures.get(textureName);
		}
		return null;
	}
	
	/**
	 * Get a sound.
	 * @param name
	 * @return
	 */
	public Sound getSound(String name) {
		if (_sounds.containsKey(name)) {
			return _sounds.get(name);
		}
		return null;
	}
	
	/**
	 * Releases the texture associated with an atlas and removes it from our collection.
	 * This will invalidate all regions obtained from this atlas as well.
	 * @param name of the atlas to remove.
	 */
	public void releaseAtlas(String name)
	{
		if(_atlasMap.containsKey(name))
		{
			_atlasMap.get(name).dispose(); //Dispose of the atlas's texture
			_atlasMap.remove(name); //Remove the texture atlas from the collection
		}
	}
	
	/**
	 * Release and remove the font associated with the given name.
	 * @param name
	 */
	public void releaseFont(String name)
	{
		if(_fontMap.containsKey(name))
		{
			_fontMap.get(name).dispose(); //Dispose of the font
			_fontMap.remove(name); //Remove the font from our collection
		}
	}
	
	/**
	 * Iterates through all our atlases and unloads them.
	 * None of the regions you obtained from these will be valid anymore
	 */
	public void releaseAllAtlas()
	{
		Iterator<Map.Entry<String, TextureAtlas>> it = _atlasMap.entrySet().iterator();
		while(it.hasNext())
		{
			Map.Entry<String, TextureAtlas> pairs = it.next();
			pairs.getValue().dispose();
			it.remove();
		}
	}
	
	/**
	 * Iterates through all our fonts and unloads them.
	 */
	public void releaseAllFonts()
	{
		Iterator<Map.Entry<String, BitmapFont>> it = _fontMap.entrySet().iterator();
		while(it.hasNext())
		{
			Map.Entry<String, BitmapFont> pairs = it.next();
			pairs.getValue().dispose();
			it.remove();
		}
	}	
	
	/**
	 * Iterates through all our fonts and unloads them.
	 */
	public void releaseAllTextures()
	{
		Iterator<Map.Entry<String, Texture>> it = _textures.entrySet().iterator();
		while(it.hasNext())
		{
			Map.Entry<String, Texture> pairs = it.next();
			pairs.getValue().dispose();
			it.remove();
		}
	}
	
	/**
	 * Iterates through all our fonts and unloads them.
	 */
	public void releaseAllSounds()
	{
		Iterator<Map.Entry<String, Sound>> it = _sounds.entrySet().iterator();
		while(it.hasNext())
		{
			Map.Entry<String, Sound> pairs = it.next();
			pairs.getValue().dispose();
			it.remove();
		}
	}
	
	/**
	 * Releases ALL resources associated with this resource manager in one fell swoop.
	 */
	public void releaseAllResources()
	{
		releaseAllAtlas();
		releaseAllFonts();
		releaseAllTextures();
		releaseAllSounds();
	}
}
