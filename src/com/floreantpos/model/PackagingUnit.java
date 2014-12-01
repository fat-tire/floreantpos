package com.floreantpos.model;

import org.apache.commons.lang.StringUtils;

import com.floreantpos.model.base.BasePackagingUnit;



public class PackagingUnit extends BasePackagingUnit {
	private static final long serialVersionUID = 1L;

/*[CONSTRUCTOR MARKER BEGIN]*/
	public PackagingUnit () {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public PackagingUnit (java.lang.Integer id) {
		super(id);
	}

/*[CONSTRUCTOR MARKER END]*/

	public void setPackagingDimension(PackagingDimension dimension) {
		setDimension(dimension.name());
	}
	
	public PackagingDimension getPackagingDimension() {
		String dimension2 = getDimension();
		
		if(StringUtils.isEmpty(dimension2)) {
			return null;
		}
		
		return PackagingDimension.valueOf(dimension2);
	}

	@Override
	public String toString() {
		return getName();
	}
}