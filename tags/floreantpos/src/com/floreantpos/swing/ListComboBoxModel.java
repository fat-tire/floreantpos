package com.floreantpos.swing;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractListModel;
import javax.swing.MutableComboBoxModel;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;


public class ListComboBoxModel extends AbstractListModel implements MutableComboBoxModel, Serializable, ListDataListener {
	private List dataList;
	private Object selectedObject;

	public ListComboBoxModel() {
		this(new ArrayList());
	}

	public ListComboBoxModel(List list) {
		setDataList(list);
		
		if (getSize() > 0) {
			selectedObject = getElementAt(0);
		}
	}
	
	public void setDataList(List list) {
		dataList = list;
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
		return dataList.size();
	}

	// implements javax.swing.ListModel
	public Object getElementAt(int index) {
		if (index >= 0 && index < dataList.size())
			return dataList.get(index);
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
	public void addElement(Object anObject) {
		dataList.add(anObject);
		fireIntervalAdded(this, dataList.size() - 1, dataList.size() - 1);
		if (dataList.size() == 1 && selectedObject == null && anObject != null) {
			setSelectedItem(anObject);
		}
	}

	// implements javax.swing.MutableComboBoxModel
	public void insertElementAt(Object anObject, int index) {
		dataList.add(index, anObject);
		fireIntervalAdded(this, index, index);
	}

	// implements javax.swing.MutableComboBoxModel
	public void removeElementAt(int index) {
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
		int index = dataList.indexOf(anObject);
		if (index != -1) {
			removeElementAt(index);
		}
	}

	/**
	 * Empties the list.
	 */
	public void removeAllElements() {
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
}
