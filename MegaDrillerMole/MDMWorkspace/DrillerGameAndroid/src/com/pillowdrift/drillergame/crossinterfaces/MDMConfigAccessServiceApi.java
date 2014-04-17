package com.pillowdrift.drillergame.crossinterfaces;

import java.util.List;
import java.util.Map;

import com.pillowdrift.drillergame.database.ConfigEntry;

/**
 * Interface listing required functionality for config setting and retrieval
 * @author cake_cruncher_7
 *
 */
public interface MDMConfigAccessServiceApi
{
	public List<ConfigEntry> getConfig();
	public Map<String, ConfigEntry> getConfigMap();
	public void modifyConfigEntry(String name, String setting);
	public void clearConfigEntries();
	public void clearConfigToDefault();
}
