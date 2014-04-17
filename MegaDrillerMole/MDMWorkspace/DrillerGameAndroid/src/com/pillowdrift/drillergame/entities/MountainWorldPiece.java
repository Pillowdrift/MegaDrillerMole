package com.pillowdrift.drillergame.entities;

import com.pillowdrift.drillergame.scenes.GameScene;

/**
 * Worldpiece visually representing mountains which go by in the background.
 * @author cake_cruncher_7
 *
 */
public class MountainWorldPiece extends WorldPiece
{
	public MountainWorldPiece(GameScene parent, float xColumn, float totalColumns, float yPos)
	{
		super(parent, xColumn, totalColumns, yPos);
		
		//Adjust depth
		_depth = 105;
		
		//Adjust movement factor
		_movFactor = 0.5f;
	}

	@Override
	protected String getAtlasName() {
		return "atlas01";
	}
	
	@Override
	protected String getRegionName() {
		return "mountain01";
	}
}
