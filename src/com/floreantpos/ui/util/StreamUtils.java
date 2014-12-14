package com.floreantpos.ui.util;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;

public class StreamUtils {
	public static String toString(InputStream in) throws IOException {
		if(in == null) {
			return "";
		}
		
		try {
			return IOUtils.toString(in);
		} finally {
			IOUtils.closeQuietly(in);
		}
	}
}
