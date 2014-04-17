package com.pillowdrift.drillergame.crossinterfaces;

import java.util.List;
import java.util.Map;

import com.pillowdrift.drillergame.database.ItemEntry;

public interface MDMItemsServiceApi 
{
	public List<ItemEntry> getItems();
	public Map<String, ItemEntry> getItemMap();
	public void modifyItemEntry(String name, String purchased, String using);
	public void clearItemEntries();
	public void clearItemsToDefault();
	public String getCurrentDrillInUse();
	public void setCurrentDrillInUse(String drill);
	public boolean hasBubble();
	public void setBubble(boolean hasBubble);
}
