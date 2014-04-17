package com.pillowdrift.drillergame.framework;

import java.util.Random;

import com.badlogic.gdx.graphics.Color;

/**
 * Utilities class with a collection of general purpose function.
 * @author cake_cruncher_7
 *
 */
public final class Utils
{
	public static Random _rand;
	
	/**
	 * Wrap a float between a minimum and maximum value
	 * @param value
	 * @param min
	 * @param max
	 * @return
	 */
	public static float wrapf(float value, float min, float max)
	{
		//Get the range of valid numbers (not inclusive);
		float range = max-min;
		while(value >= max) value -= range;
		while(value < min) value += range;
		return value;
	}
	
	/**
	 * Initialize the random number generator
	 */
	public static void randInit()
	{
		_rand = new Random();	
	}
	
	/**
	 * Generate a random integer between the provided min and max
	 */
	public static int randomI(int min, int max)
	{
		return min + (int)(_rand.nextFloat() * (max-min+1));
	}
	
	/**
	 * Generate a random float between the provided min and max
	 */
	public static float randomF(float min, float max)
	{
		if (min == max) return min;
		float range = max - min;
		return (_rand.nextFloat()*range) + min;
	}
	
	/**
	 * Return the given object as the given type, or null if not compatible
	 * This works well as a replacement of c#'s 'as' keyword
	 * http://stackoverflow.com/questions/1034204/equivalent-of-cs-as-in-java
	 * @param t
	 * @param o
	 * @return
	 */
	public static <T> T as(Class<T> t, Object o) {
	  return t.isInstance(o) ? t.cast(o) : null;
	}
	
	/**
	 * Rounds an integer to the nearest power of 2.
	 * @param v
	 * @return
	 */
	public static int roundToPower2(int v) {
	    v--;
	    v |= v >> 1;
	    v |= v >> 2;
	    v |= v >> 4;
	    v |= v >> 8;
	    v |= v >> 16;
	    v++;
	    return v;
	}

	/**
	 * Interpolates two colors.
	 * @param _max
	 * @param _min
	 * @param _value
	 * @return
	 */
	public static Color interpolateColor(Color _max, Color _min, float _value) {
		Color result = new Color();
		float value = _value; if (value >= 1) return _max; if (value <= 0) return _min;
		result.a = _min.a + (_max.a - _min.a) * _value;
		result.r = _min.r + (_max.r - _min.r) * _value;
		result.g = _min.g + (_max.g - _min.g) * _value;
		result.b = _min.b + (_max.b - _min.b) * _value;
		return result;
	}
	
    public static float lerp(float a, float first, float second){
        return first + (second - first) * a;
    }
	
}
