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
import java.util.Vector;

import javax.swing.AbstractListModel;

public class ListModel<E> extends AbstractListModel<E> {
	private List<E> list;

	public ListModel() {
		this.list = new ArrayList<E>();
	}

	public ListModel(List<E> list) {
		this.list = list;
	}

	public void setData(List<E> list) {
		this.list = list;
	}

	public List<E> getDataList() {
		return this.list;
	}

	/**
	 * Adds the specified component to the end of this list.
	 *
	 * @param   element   the component to be added
	 * @see Vector#addElement(Object)
	 */
	public void addElement(E element) {
		int index = list.size();
		list.add(element);
		fireIntervalAdded(this, index, index);
	}

	/**
	 * Returns an enumeration of the components of this list.
	 *
	 * @return  an enumeration of the components of this list
	 * @see Vector#elements()
	 */
	public Iterator<E> iterator() {
		return list.iterator();
	}

	@Override
	public int getSize() {
		return list.size();
	}

	@Override
	public E getElementAt(int index) {
		return list.get(index);
	}

	public void remove(int index) {
		list.remove(index);
		fireContentsChanged(this, index, index);
	}

	public void removeElement(E element) {
		list.remove(element);
	}

}
