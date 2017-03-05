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
package com.floreantpos.swing;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.AbstractListModel;

public class ListBasedListModel<E> extends AbstractListModel<E> {
	private List<E> dataList;

	public ListBasedListModel() {
		super();
	}

	public ListBasedListModel(List list) {
		super();
		this.dataList = list;
	}

	@Override
	public int getSize() {
		if (dataList == null) {
			return 0;
		}
		return dataList.size();
	}

	@Override
	public E getElementAt(int index) {
		if (dataList == null) {
			return null;
		}
		return dataList.get(index);
	}

	public void addElement(E element) {
		ensureListNotNull();
		dataList.add(element);
	}

	public List<E> getDataList() {
		return dataList;
	}

	public void setDataList(List<E> dataList) {
		this.dataList = dataList;
	}

	public Iterator<E> iterator() {
		ensureListNotNull();
		return dataList.iterator();
	}

	private void ensureListNotNull() {
		if (dataList == null) {
			dataList = new ArrayList<>();
		}
	}

	public void clearAll() {
		for (Iterator iterator = dataList.iterator(); iterator.hasNext();) {
			E value = (E) iterator.next();
			if (value != null)
				iterator.remove();

		}
	}

	public void clearItem(E item) {
		for (Iterator iterator = dataList.iterator(); iterator.hasNext();) {
			E value = (E) iterator.next();
			if (value == item)
				iterator.remove();

		}
	}

}
