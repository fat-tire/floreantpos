package com.floreantpos.model;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.Set;

import org.hibernate.Hibernate;

import com.floreantpos.model.base.BaseShopFloor;

public class ShopFloor extends BaseShopFloor {
	private static final long serialVersionUID = 1L;

	/*[CONSTRUCTOR MARKER BEGIN]*/
	public ShopFloor () {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public ShopFloor (java.lang.Integer id) {
		super(id);
	}

	/*[CONSTRUCTOR MARKER END]*/

	private byte[] imageData;

	public void setImageData(byte[] imageData) {
		this.imageData = imageData;
	}

	public byte[] getImageData() {
		return imageData;
	}

	@Override
	public void setImage(Blob image) {
		//super.setImage(image);
		try {
			this.imageData = toByteArray(image);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public Blob getImage() {
		return Hibernate.createBlob(this.imageData);
	}
	
	@Override
	public String toString() {
		return getName();
	}

	private byte[] toByteArray(Blob fromBlob) throws Exception {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			return toByteArrayImpl(fromBlob, baos);
		} finally {
			if (baos != null) {
				try {
					baos.close();
				} catch (IOException ex) {
				}
			}
		}
	}

	private byte[] toByteArrayImpl(Blob fromBlob, ByteArrayOutputStream baos) throws SQLException, IOException {
		byte[] buf = new byte[4000];
		InputStream is = fromBlob.getBinaryStream();
		try {
			for (;;) {
				int dataSize = is.read(buf);
				if (dataSize == -1)
					break;
				baos.write(buf, 0, dataSize);
			}
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException ex) {
				}
			}
		}
		return baos.toByteArray();
	}
	
	public boolean hasTableWithNumber(String number) {
		Set<ShopTable> tables = getTables();
		if(tables == null) {
			return false;
		}
		
		for (ShopTable shopTable : tables) {
			if(shopTable.getTableNumber().equals(number)) {
				return true;
			}
		}
		
		return false;
	}
}
