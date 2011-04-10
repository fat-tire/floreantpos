package com.floreantpos.swing;

import java.awt.Image;
import java.net.URL;

public class ImageIcon extends javax.swing.ImageIcon {

	public ImageIcon(String filename, String description) {
		super(filename, description);
	}

	public ImageIcon(String filename) {
		super(filename);
	}

	public ImageIcon(URL location, String description) {
		super(location, description);
	}

	public ImageIcon(URL location) {
		super(location);
	}

	public ImageIcon(Image image, String description) {
		super(image, description);
	}

	public ImageIcon(Image image) {
		super(image);
	}

	public ImageIcon(byte[] imageData, String description) {
		super(imageData, description);
	}

	public ImageIcon(byte[] imageData) {
		super(imageData);
	}

	public ImageIcon() {
		super();
	}

}
