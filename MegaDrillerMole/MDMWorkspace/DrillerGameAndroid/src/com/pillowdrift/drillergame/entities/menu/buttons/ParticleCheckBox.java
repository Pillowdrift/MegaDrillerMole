package com.pillowdrift.drillergame.entities.menu.buttons;

import com.pillowdrift.drillergame.database.ConfigDAO;
import com.pillowdrift.drillergame.framework.Scene;
import com.pillowdrift.drillergame.scenes.GameScene;

public class ParticleCheckBox extends GenericCheckBox {
	/**
	 * Create a new particle check box.
	 * @param parent
	 * @param value
	 */
	public ParticleCheckBox(Scene parent) {
		super(parent, "Particles: ON", "Particles: OFF",
				parent.getOwner().getGame()
				.getDataService()
					.getConfigDataAccessor().getConfigMap().get(ConfigDAO.SettingNames.PARTICLES).getSetting().equals(ConfigDAO.OnOff.ON));
	}
	
	@Override
	protected void onValueChange() {
		super.onValueChange();
		
		// If the setting is being turned on, start the particle thread.
		GameScene scene = (GameScene)_parent.getOwner().getScene("GameScene");
		if (_value == true) {
			scene.getParticlePool01().startThread();
		} else {
			scene.getParticlePool01().terminateThread();
		}
		
		// Change the config setting.
		_parent.getOwner().getGame()
			.getDataService()
				.getConfigDataAccessor().modifyConfigEntry(ConfigDAO.SettingNames.PARTICLES, _value ? ConfigDAO.OnOff.ON : ConfigDAO.OnOff.OFF);
	}
}
