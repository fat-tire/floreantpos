package com.floreantpos.model;

import org.apache.commons.lang.StringUtils;

import com.floreantpos.model.base.BaseOrderType;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class OrderType extends BaseOrderType {
	private static final long serialVersionUID = 1L;

	public static final String BAR_TAB = "BAR_TAB"; //$NON-NLS-1$
	public static final String FOR_HERE = "FOR HERE"; //$NON-NLS-1$
	public static final String TO_GO = "TO GO"; //$NON-NLS-1$
	public static final String ALLOW_TO_ADD_TIPS_LATER = "ADD_TIPS_LATER";

	private transient JsonObject propertiesContainer;

	public OrderType() {
		super();
	}

	public OrderType(java.lang.Integer id) {
		super(id);
	}

	public OrderType(java.lang.Integer id, java.lang.String name) {

		super(id, name);
	}

	public String name() {
		return super.getName();
	}

	public OrderType valueOf() {
		return this;
	}

	@Override
	public String toString() {
		return getName().replaceAll("_", " "); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Override
	public String getProperties() {
		if (propertiesContainer != null) {
			return propertiesContainer.toString();
		}
		String properties = super.getProperties();
		if (StringUtils.isEmpty(properties)) {
			return null;
		}
		propertiesContainer = new Gson().fromJson(properties, JsonObject.class);
		return properties;
	}

	@Override
	public void setProperties(String properties) {
		super.setProperties(properties);
		propertiesContainer = new Gson().fromJson(properties, JsonObject.class);
	}

	public void addProperty(String key, String value) {
		if (propertiesContainer == null) {
			propertiesContainer = new JsonObject();
		}
		if (StringUtils.isNotEmpty(value)) {
			propertiesContainer.addProperty(key, value);
		}
	}

	public String getProperty(String key) {
		if (propertiesContainer == null) {
			return null;
		}
		return propertiesContainer.get(key).getAsString();
	}

	public Boolean getBoolean(String key, Boolean defaultValue) {
		if (propertiesContainer == null) {
			return defaultValue;
		}
		return propertiesContainer.has(key) ? propertiesContainer.get(key).getAsBoolean() : defaultValue;
	}

	public boolean isAllowToAddTipsLater() {
		return getBoolean(OrderType.ALLOW_TO_ADD_TIPS_LATER, true);
	}
}