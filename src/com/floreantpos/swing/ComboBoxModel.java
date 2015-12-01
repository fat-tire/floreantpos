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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractListModel;
import javax.swing.MutableComboBoxModel;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;


public class ComboBoxModel extends AbstractListModel implements MutableComboBoxModel, Serializable, ListDataListener {
	private List dataList;
	private Object selectedObject;

	public ComboBoxModel() {
		this(new ArrayList());
	}

	public ComboBoxModel(List list) {
		setDataList(list);
		
		if (getSize() > 0) {
			selectedObject = getElementAt(0);
		}
	}
	
	public void setDataList(List list) {
		int size = 0;
		if(list != null) {
			size = list.size();
		}
		this.dataList = list;
		fireContentsChanged(this, 0, size);
	}

	// implements javax.swing.ComboBoxModel
	/**
	 * Set the value of the selected item. The selected item may be null.
	 * <p>
	 * @param anObject The combo box value or null for no selection.
	 */
	public void setSelectedItem(Object anObject) {
		if ((selectedObject != null && !selectedObject.equals(anObject)) || selectedObject == null && anObject != null) {
			selectedObject = anObject;
			fireContentsChanged(this, -1, -1);
		}
	}

	// implements javax.swing.ComboBoxModel
	public Object getSelectedItem() {
		return selectedObject;
	}

	// implements javax.swing.ListModel
	public int getSize() {
		if(dataList == null) {
			return 0;
		}
		return dataList.size();
	}

	// implements javax.swing.ListModel
	public Object getElementAt(int index) {
		if(dataList != null) {
			if (index >= 0 && index < dataList.size()) {
				return dataList.get(index);
			}
			return null;
		}
		
		else
			return null;
	}

	/**
	 * Returns the index-position of the specified object in the list.
	 *
	 * @param anObject  
	 * @return an int representing the index position, where 0 is 
	 *         the first position
	 */
	public int getIndexOf(Object anObject) {
		return dataList.indexOf(anObject);
	}

	// implements javax.swing.MutableComboBoxModel
	@SuppressWarnings("unchecked")
	public void addElement(Object anObject) {
		dataList.add(anObject);
		fireIntervalAdded(this, dataList.size() - 1, dataList.size() - 1);
		if (dataList.size() == 1 && selectedObject == null && anObject != null) {
			setSelectedItem(anObject);
		}
	}

	// implements javax.swing.MutableComboBoxModel
	public void insertElementAt(Object anObject, int index) {
		if(dataList == null) {
			dataList = new ArrayList();
		}
		dataList.add(index, anObject);
		fireIntervalAdded(this, index, index);
	}

	// implements javax.swing.MutableComboBoxModel
	public void removeElementAt(int index) {
		if(dataList == null) {
			return;
		}
		if (getElementAt(index) == selectedObject) {
			if (index == 0) {
				setSelectedItem(getSize() == 1 ? null : getElementAt(index + 1));
			} else {
				setSelectedItem(getElementAt(index - 1));
			}
		}

		dataList.remove(index);

		fireIntervalRemoved(this, index, index);
	}

	// implements javax.swing.MutableComboBoxModel
	public void removeElement(Object anObject) {
		if(dataList == null) {
			return;
		}
		int index = dataList.indexOf(anObject);
		if (index != -1) {
			removeElementAt(index);
		}
	}

	/**
	 * Empties the list.
	 */
	public void removeAllElements() {
		if(dataList == null) {
			return;
		}
		if (dataList.size() > 0) {
			int firstIndex = 0;
			int lastIndex = dataList.size() - 1;
			dataList.clear();
			selectedObject = null;
			fireIntervalRemoved(this, firstIndex, lastIndex);
		} else {
			selectedObject = null;
		}
	}

	public void intervalAdded(ListDataEvent e) {
		int index0 = e.getIndex0();
		int index1 = e.getIndex1();
		fireIntervalAdded(this, index0, index1);
	}

	public void intervalRemoved(ListDataEvent e) {
		int index0 = e.getIndex0();
		int index1 = e.getIndex1();
		fireIntervalRemoved(this, index0, index1);
	}

	public void contentsChanged(ListDataEvent e) {
		int index0 = e.getIndex0();
		int index1 = e.getIndex1();
		fireContentsChanged(this, index0, index1);
	}

	public List getDataList() {
		return dataList;
	}
}
