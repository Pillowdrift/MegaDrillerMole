package com.pillowdrift.drillergame.entities;

import com.pillowdrift.drillergame.scenes.GameScene;

/**
 * Worldpiece entity visually representing impenetrable bedrock.
 * @author cake_cruncher_7
 *
 */
public class BedrockWorldPiece extends WorldPiece {

	public BedrockWorldPiece(GameScene parent, float xColumn, float totalColumns, float yPos)
	{
		super(parent, xColumn, totalColumns, yPos);
		
		//Adjust depth
		_depth = 95;
		
		//Adjust movement factor
		_movFactor = 1.0f;
	}

	@Override
	protected String getAtlasName() {
		return "atlas01";
	}
	
	@Override
	protected String getRegionName() {
		return "bedrock01";
	}

}
