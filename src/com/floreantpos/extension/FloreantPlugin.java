package com.floreantpos.extension;

import net.xeoh.plugins.base.Plugin;

public interface FloreantPlugin extends Plugin {
	String getName();
	void init();
	void initBackoffice();
}
