package com.floreantpos.swing;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

public class ImageComponent extends JPanel {
	private Image image;

	private Integer imageWidth;
	private Integer imageHeight;
	private boolean scaleToSize = true;

	public ImageComponent() {
	}

	public ImageComponent(Image image) {
		this.image = image;

		BufferedImage bimage = new BufferedImage(image.getWidth(null), image.getHeight(null), BufferedImage.TYPE_INT_ARGB);
		
		imageWidth = bimage.getWidth();
		imageHeight = bimage.getHeight();

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

	public Integer getImageWidth() {
		return imageWidth;
	}

	public Integer getImageHeight() {
		return imageHeight;
	}

}
