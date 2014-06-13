package com.floreantpos;

import java.util.HashMap;

import javax.swing.ImageIcon;

public class IconFactory {
	private static HashMap<String, ImageIcon> iconCache = new HashMap<String, ImageIcon>();
	
	public static ImageIcon getIcon(String iconName) {
		ImageIcon icon = iconCache.get(iconName);
		if(icon == null) {
			try {
				icon = new ImageIcon(IconFactory.class.getResource("/images/" + iconName)); //$NON-NLS-1$
				iconCache.put(iconName, icon);
			}catch(Exception x) {}
		}
		return icon;
	}
}
