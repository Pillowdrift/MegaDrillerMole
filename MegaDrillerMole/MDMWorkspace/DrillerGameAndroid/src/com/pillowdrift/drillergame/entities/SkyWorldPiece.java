package com.pillowdrift.drillergame.entities;

import com.pillowdrift.drillergame.scenes.GameScene;

/**
 * WorldPiece to visually represent the majority of the sky in the game.
 * @author cake_cruncher_7
 *
 */
public class SkyWorldPiece extends WorldPiece
{
	public SkyWorldPiece(GameScene parent, float xColumn, float totalColumns, float yPos)
	{
		super(parent, xColumn, totalColumns, yPos);
		
		//Adjust depth
		_depth = 110;
		
		//Adjust movement factor
		_movFactor = 0.2f;
	}

	@Override
	protected String getAtlasName() {
		return "atlas01";
	}
	
	@Override
	protected String getRegionName() {
		return "sky01";
	}
}
