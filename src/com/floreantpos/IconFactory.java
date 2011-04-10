package com.floreantpos;

import java.util.HashMap;

import javax.swing.ImageIcon;

public class IconFactory {
	private static HashMap<String, ImageIcon> iconCashe = new HashMap<String, ImageIcon>();
	
	public static ImageIcon getIcon(String iconName) {
		ImageIcon icon = iconCashe.get(iconName);
		if(icon == null) {
			try {
				icon = new ImageIcon(IconFactory.class.getResource("/images/" + iconName));
				iconCashe.put(iconName, icon);
			}catch(Exception x) {}
		}
		return icon;
	}
}
