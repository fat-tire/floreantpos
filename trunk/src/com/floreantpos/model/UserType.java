package com.floreantpos.model;

import java.util.Set;

import javax.xml.bind.annotation.XmlRootElement;

import com.floreantpos.model.base.BaseUserType;


@XmlRootElement(name="user-type")
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
	
	public boolean hasPermission(UserPermission permission) {
		Set<UserPermission> permissions = getPermissions();
		if(permissions == null) {
			return false;
		}
		
		return permissions.contains(permission);
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