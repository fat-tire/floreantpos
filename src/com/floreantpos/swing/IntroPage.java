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

import java.awt.Graphics;
import java.awt.Image;

import javax.swing.JPanel;

import com.floreantpos.IconFactory;

public class IntroPage extends JPanel {
	private Image image;
	private Image image2 = IconFactory.getIcon("/images", "open_initiative.png").getImage(); //$NON-NLS-1$ //$NON-NLS-2$
	private boolean scaleToSize = true;

	public IntroPage() {
	}

	public IntroPage(Image image) {
		this.image = image;
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		int x = 0;
		int y = 0;
		int width = getWidth();
		int height = getHeight();

		if (scaleToSize) {
			g.drawImage(image, x, y, width, height, this);
		}
		else {
			g.drawImage(image, x, y, this);
		}

		g.drawImage(image2, x + 30, height - 190, width - 60, 200, this);
	}

	public Image getImage() {
		return image;
	}

	public void setImage(Image image) {
		this.image = image;
	}

	public boolean isScaleToSize() {
		return scaleToSize;
	}

	public void setScaleToSize(boolean scaleToSize) {
		this.scaleToSize = scaleToSize;
	}
}
