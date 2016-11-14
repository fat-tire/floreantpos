package com.floreantpos.model;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.floreantpos.model.base.BaseShopFloorTemplate;
import com.floreantpos.util.POSUtil;

public class ShopFloorTemplate extends BaseShopFloorTemplate {
	private static final long serialVersionUID = 1L;

	/*[CONSTRUCTOR MARKER BEGIN]*/
	public ShopFloorTemplate() {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public ShopFloorTemplate(java.lang.Integer id) {
		super(id);
	}

	/*[CONSTRUCTOR MARKER END]*/
	@Override
	public String toString() {
		String displayName = super.getName();
		if (isDefaultFloor()) {
			displayName += " -Default"; //$NON-NLS-1$
		}
		return displayName;
	}

	public void addProperty(String name, String value) {
		if (getProperties() == null) {
			setProperties(new HashMap<String, String>());
		}

		getProperties().put(name, value);
	}

	public boolean hasProperty(String key) {
		return getProperty(key) != null;
	}

	public String getProperty(String key) {
		if (getProperties() == null) {
			return null;
		}

		return (String) getProperties().get(key);
	}

	public String getProperty(String key, String defaultValue) {
		if (getProperties() == null) {
			return null;
		}

		String string = (String) getProperties().get(key);
		if (StringUtils.isEmpty(string)) {
			return defaultValue;
		}

		return string;
	}

	public void removeProperty(String propertyName) {
		Map<String, String> properties = getProperties();
		if (properties == null) {
			return;
		}

		properties.remove(propertyName);
	}

	public boolean isPropertyValueTrue(String propertyName) {
		String property = getProperty(propertyName);

		return POSUtil.getBoolean(property);
	}

}