package com.pillowdrift.drillergame.entities;

import com.pillowdrift.drillergame.scenes.GameScene;

/**
 * WorldPiece to visually represent the majority (or all) of the ground in the game.
 * @author cake_cruncher_7
 *
 */
public class EarthWorldPiece extends WorldPiece
{
	public EarthWorldPiece(GameScene parent, float xColumn, float totalColumns, float yPos)
	{
		super(parent, xColumn, totalColumns, yPos);
	}
	
	@Override
	protected String getAtlasName() {
		return "atlas01";
	}
	
	@Override
	protected String getRegionName() {
		return "dirt01";
	}
}
