package com.floreantpos.swing;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;
import javax.swing.filechooser.FileView;

public class POSFileChooser extends JFileChooser {

	FileNameExtensionFilter filter = new FileNameExtensionFilter("Image Files", "jpg", "jpeg", "png", "gif");

	public POSFileChooser() {
		setDialogTitle("FloreantPOS");
		setAcceptAllFileFilterUsed(false);
		setFileFilter(filter);
		setAccessory(new ImagePreview(this));

		setFileView(new FileView() {
			public Icon getIcon(File f) {

				return FileSystemView.getFileSystemView().getSystemIcon(f);
			}
		});
	}

	public class ImagePreview extends JComponent implements PropertyChangeListener {
		ImageIcon thumbnail = null;
		File file = null;

		public ImagePreview(JFileChooser fc) {
			setPreferredSize(new Dimension(300, 200));
			fc.addPropertyChangeListener(this);
		}

		public void loadImage() {
			if(file == null) {
				thumbnail = null;
				return;
			}

			ImageIcon tmpIcon = new ImageIcon(file.getPath());
			if(tmpIcon != null) {
				if(tmpIcon.getIconWidth() > 90) {
					thumbnail = new ImageIcon(tmpIcon.getImage().getScaledInstance(280, -1, Image.SCALE_DEFAULT));
				}
				else {
					thumbnail = tmpIcon;
				}
			}
		}

		public void propertyChange(PropertyChangeEvent e) {
			boolean update = false;
			String prop = e.getPropertyName();

			if(JFileChooser.DIRECTORY_CHANGED_PROPERTY.equals(prop)) {
				file = null;
				update = true;

			}
			else if(JFileChooser.SELECTED_FILE_CHANGED_PROPERTY.equals(prop)) {
				file = (File) e.getNewValue();
				update = true;
			}

			if(update) {
				thumbnail = null;
				if(isShowing()) {
					loadImage();
					repaint();
				}
			}
		}

		protected void paintComponent(Graphics g) {
			if(thumbnail == null) {
				loadImage();
			}
			if(thumbnail != null) {
				int x = getWidth() / 2 - thumbnail.getIconWidth() / 2;
				int y = getHeight() / 2 - thumbnail.getIconHeight() / 2;

				if(y < 0) {
					y = 0;
				}

				if(x < 5) {
					x = 5;
				}
				thumbnail.paintIcon(this, g, x, y);
			}
		}
	}
}
