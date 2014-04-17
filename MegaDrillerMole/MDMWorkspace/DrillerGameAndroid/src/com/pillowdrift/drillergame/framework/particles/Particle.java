package com.pillowdrift.drillergame.framework.particles;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

/***
 * Class to represent a particle in our system.
 * All members are public.
 * @author cake_cruncher_7
 *
 */
public class Particle {
	//Particle properties
	public TextureRegion _texture;
	public float _originX;
	public float _originY;
	public float _width;
	public float _height;
	public float _x;
	public float _y;
	public float _dx;
	public float _dy;
	public float _gravityCo;
	public float _rot;
	public float _drot;
	public float _scale;
	public float _dscale;
	public float _alpha;
	public float _dalpha;
	public float _lifetime;
	//Used in spawning
	public boolean _ready;
	
	public void copyFrom(Particle target)
	{
		_texture = target._texture;
		_originX = target._originX;
		_originY = target._originY;
		_width = target._width;
		_height = target._height;
		_x = target._x;
		_y = target._y;
		_dx = target._dx;
		_dy = target._dy;
		_gravityCo = target._gravityCo;
		_rot = target._rot;
		_drot = target._drot;
		_scale = target._scale;
		_dscale = target._dscale;
		_alpha = target._alpha;
		_dalpha = target._dalpha;
		_lifetime = target._lifetime;
		//Don't bother copying _ready
	}
}
