package com.floreantpos.extension;

import javax.swing.JMenuBar;

import net.xeoh.plugins.base.Plugin;

public interface FloreantPlugin extends Plugin {
	String getName();
	void configureBackofficeMenuBar(JMenuBar menuBar);
}
