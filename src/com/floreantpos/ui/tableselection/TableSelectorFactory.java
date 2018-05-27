package com.floreantpos.ui.tableselection;

import com.floreantpos.extension.ExtensionManager;
import com.floreantpos.extension.FloorLayoutPlugin;
import com.floreantpos.model.OrderType;

public class TableSelectorFactory {
	private static TableSelector tableSelector;

	public static TableSelectorDialog createTableSelectorDialog(OrderType orderType) {
		FloorLayoutPlugin floorLayoutPlugin = (FloorLayoutPlugin) ExtensionManager.getPlugin(FloorLayoutPlugin.class);
		if (tableSelector == null) {
			if (floorLayoutPlugin == null) {
				tableSelector = DefaultTableSelectionView.getInstance();
			}
			else {
				tableSelector = floorLayoutPlugin.createTableSelector();
			}
		}
		tableSelector.setOrderType(orderType);
		tableSelector.redererTables();

		return new TableSelectorDialog(tableSelector);
	}
}
