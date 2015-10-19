package com.floreantpos.ui;

import javax.swing.JPanel;

import com.floreantpos.bo.ui.ModelBrowser;

public abstract class SearchPanel<E> extends JPanel {
	protected ModelBrowser<E> modelBrowser;

	/**
	 * @return the modelBrowser
	 */
	public ModelBrowser<E> getModelBrowser() {
		return modelBrowser;
	}

	/**
	 * @param modelBrowser the modelBrowser to set
	 */
	public void setModelBrowser(ModelBrowser<E> modelBrowser) {
		this.modelBrowser = modelBrowser;
	}

}
