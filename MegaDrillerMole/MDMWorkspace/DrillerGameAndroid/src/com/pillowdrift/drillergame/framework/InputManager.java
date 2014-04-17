package com.pillowdrift.drillergame.framework;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector2;

/**
 * Class which will manage input for the game by listenning for
 * input and storing limitted data about the current state of the device.
 * @author cake_cruncher_7
 *
 */
public class InputManager implements InputProcessor
{
	//Class to hold the status of a pointer
	private class PointerData
	{
		int _x = 0;
		int _y = 0;
		int _pointerId = 0;
		boolean _isPressed = false;
	}
	
	//CONSTANTS
	public static final int NUMBER_OF_POINTERS = 5;
	
	//DATA
	boolean _curBackPressed = false;
	boolean _curMenuPressed = false;
	PointerData[] _pointerDatas;
	
	//ACCESS
	public boolean isBackPressed()
	{
		return _curBackPressed;
	}
	public boolean isMenuPressed()
	{
		return _curMenuPressed;
	}
	/**
	 * Indicates whether the device is being touched by any pointer
	 * @return
	 */
	public boolean isTouched()
	{
		for(int i = 0; i < NUMBER_OF_POINTERS; ++i)
		{
			if(_pointerDatas[i]._isPressed) return true;
		}
		return false;
	}
	/**
	 * Indicates whether the device is being touched by a specific pointer
	 * @param pointer
	 * @return
	 */
	public boolean isTouchedBy(int pointer)
	{
		return _pointerDatas[pointer]._isPressed;
	}
	/**
	 * Retrieves the x coordinate of the first touched pointer, or -5 if none are touched
	 * @param pointer
	 * @return
	 */
	public int getFirstTouchedPositionX()
	{
		for(int i = 0; i < NUMBER_OF_POINTERS; ++i)
		{
			if(_pointerDatas[i]._isPressed) return _pointerDatas[i]._x;
		}
		return -5;
	}
	/**
	 * Retrieves the y coordinate of the first touched pointer, or -5 if none are touched
	 * @param pointer
	 * @return
	 */
	public int getFirstTouchedPositionY()
	{
		for(int i = 0; i < NUMBER_OF_POINTERS; ++i)
		{
			if(_pointerDatas[i]._isPressed) return _pointerDatas[i]._y;
		}
		return -5;
	}
	/**
	 * Retrieves the coordinates of the first touched pointer, or -5, -5 if none are touched
	 * @param pointer
	 * @return
	 */
	public Vector2 getFirstTouchedPosition()
	{
		for(int i = 0; i < NUMBER_OF_POINTERS; ++i)
		{
			if(_pointerDatas[i]._isPressed)
			{
				return new Vector2(_pointerDatas[i]._x, _pointerDatas[i]._y);
			}
		}
		return new Vector2(-5, -5);
	}
	/**
	 * Retrieves the x coordinate of the given pointer, touched or untouched
	 * @param pointer
	 * @return
	 */
	public int getPointerPositionX(int pointer)
	{
		return _pointerDatas[pointer]._x;
	}
	/**
	 * Retrieves the y coordinate of the given pointer, touched or untouched
	 * @param pointer
	 * @return
	 */
	public int getPointerPositionY(int pointer)
	{
		return _pointerDatas[pointer]._y;
	}
	/**
	 * Retrieves the coordinates of the given pointer, touched or untouched
	 * @param pointer
	 * @return
	 */
	public Vector2 getPointerPosition(int pointer)
	{
		return new Vector2(_pointerDatas[pointer]._x, _pointerDatas[pointer]._y);
	}
	
	//CONSTRUCTION
	/**
	 * Basic constructor to set up pointer datas
	 */
	public InputManager()
	{
		//Set up pointer data arrays
		_pointerDatas = new PointerData[NUMBER_OF_POINTERS];
		for(int i = 0; i < NUMBER_OF_POINTERS; ++i)
		{
			_pointerDatas[i] = new PointerData();
			_pointerDatas[i]._pointerId = i;
		}
	}
	
	//FUNCTION
	//used
	@Override
	public boolean keyDown(int keycode)
	{
		//Check for button type
		if(keycode == Keys.BACK)
		{
			_curBackPressed = true;
		}
		else if(keycode == Keys.MENU)
		{
			_curMenuPressed = true;
		}
		//return
		return false;
	}
	@Override
	public boolean keyUp(int keycode)
	{
		//Check for button type
		if(keycode == Keys.BACK)
		{
			_curBackPressed = false;
		}
		else if(keycode == Keys.MENU)
		{
			_curMenuPressed = false;
		}
		//return
		return false;
	}
	@Override
	public boolean touchDown(int x, int y, int pointer, int button)
	{
		if(pointer < NUMBER_OF_POINTERS)
		{
			_pointerDatas[pointer]._isPressed = true;
			_pointerDatas[pointer]._x = x;
			_pointerDatas[pointer]._y = y;
		}
		//return
		return false;
	}
	@Override
	public boolean touchDragged(int x, int y, int pointer)
	{
		if(pointer < NUMBER_OF_POINTERS)
		{
			_pointerDatas[pointer]._x = x;
			_pointerDatas[pointer]._y = y;
		}
		//return
		return false;
	}
	@Override
	public boolean touchUp(int x, int y, int pointer, int button)
	{
		if(pointer < NUMBER_OF_POINTERS)
		{
			_pointerDatas[pointer]._isPressed = false;
			_pointerDatas[pointer]._x = x;
			_pointerDatas[pointer]._y = y;
		}
		//return
		return false;
	}
	//unused
	@Override
	public boolean keyTyped(char character)
	{
		//return
		return false;
	}
	@Override
	public boolean scrolled(int amount)
	{
		//return
		return false;
	}
	@Override
	public boolean mouseMoved(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return false;
	}
}
