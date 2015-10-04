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
