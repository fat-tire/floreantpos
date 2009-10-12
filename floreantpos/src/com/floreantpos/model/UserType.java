package com.floreantpos.model;

import java.util.Set;

import com.floreantpos.model.base.BaseUserType;



public class UserType extends BaseUserType {
	private static final long serialVersionUID = 1L;

/*[CONSTRUCTOR MARKER BEGIN]*/
	public UserType () {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public UserType (java.lang.Integer id) {
		super(id);
	}

/*[CONSTRUCTOR MARKER END]*/

	public void clearPermissions() {
		Set<UserPermission> permissions = getPermissions();
		if(permissions != null) {
			permissions.clear();
		}
	}
	
	@Override
	public String toString() {
		String s = getName();
		/*if(getPermissions() != null) {
			s += " " + getPermissions();
		}*/
		return s;
	}
}