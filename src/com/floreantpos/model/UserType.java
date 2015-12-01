/**
 * ************************************************************************
 * * The contents of this file are subject to the MRPL 1.2
 * * (the  "License"),  being   the  Mozilla   Public  License
 * * Version 1.1  with a permitted attribution clause; you may not  use this
 * * file except in compliance with the License. You  may  obtain  a copy of
 * * the License at http://www.floreantpos.org/license.html
 * * Software distributed under the License  is  distributed  on  an "AS IS"
 * * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * * License for the specific  language  governing  rights  and  limitations
 * * under the License.
 * * The Original Code is FLOREANT POS.
 * * The Initial Developer of the Original Code is OROCUBE LLC
 * * All portions are Copyright (C) 2015 OROCUBE LLC
 * * All Rights Reserved.
 * ************************************************************************
 */
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