package com.pillowdrift.drillergame.entities.menu.buttons;

import com.pillowdrift.drillergame.scenes.RecordsScene;
import com.pillowdrift.drillergame.scoreboard.OnlineDatabase;

public class ScoresModeChangeButton extends GenericMenuButton {
	// Vars
	int _state;
	
	// Constrcutor
	public ScoresModeChangeButton(RecordsScene scene, int state, String text) {
		super(scene, text);
		setScale(1.3f, 1.3f);
		_state = state;
	}
	
	protected void onRelease() {
		super.onRelease();
		
		if (_state == RecordsScene.WEEKLY)
		{
			OnlineDatabase.updateWeekly((RecordsScene)_parent);
		}
		else if (_state == RecordsScene.OVERALL)
		{
			OnlineDatabase.updateOverall((RecordsScene)_parent);
		}
		
		// Set the state to local.
		((RecordsScene)_parent).setState(_state);
	};
}
