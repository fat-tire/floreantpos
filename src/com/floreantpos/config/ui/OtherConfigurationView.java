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
package com.floreantpos.config.ui;

import java.awt.BorderLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import net.miginfocom.swing.MigLayout;

import org.apache.commons.lang.StringUtils;

import com.floreantpos.Messages;
import com.floreantpos.model.GlobalConfig;
import com.floreantpos.model.dao.GlobalConfigDAO;
import com.floreantpos.swing.FixedLengthTextField;
import com.floreantpos.util.GlobalConfigUtil;

public class OtherConfigurationView extends ConfigurationView {
	public static final String CONFIG_TAB_OTHER = "Others"; //$NON-NLS-1$
	private FixedLengthTextField tfMapApiKey;

	public OtherConfigurationView() {
		setLayout(new BorderLayout());

		JPanel contentPanel = new JPanel();
		contentPanel.setLayout(new MigLayout("", "[][]", "[]")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

		tfMapApiKey = new FixedLengthTextField(); //$NON-NLS-1$
		tfMapApiKey.setLength(220);
		contentPanel.add(new JLabel(Messages.getString("OtherConfigurationView.0"))); //$NON-NLS-1$
		contentPanel.add(tfMapApiKey); //$NON-NLS-1$

		JScrollPane scrollPane = new JScrollPane(contentPanel);
		scrollPane.setBorder(null);
		add(scrollPane);
	}

	@Override
	public boolean save() throws Exception {
		if (!isInitialized()) {
			return true;
		}

		GlobalConfig globalConfig = GlobalConfigUtil.get(GlobalConfig.MAP_API_KEY);

		if (globalConfig == null) {
			globalConfig = new GlobalConfig();
			globalConfig.setKey(GlobalConfig.MAP_API_KEY);
		}
		globalConfig.setValue(tfMapApiKey.getText());

		GlobalConfigDAO.getInstance().saveOrUpdate(globalConfig);
		GlobalConfigUtil.populateGlobalConfig();
		return true;
	}

	@Override
	public void initialize() throws Exception {
		String map_api_key = GlobalConfigUtil.getValue(GlobalConfig.MAP_API_KEY);
		if (StringUtils.isEmpty(map_api_key)) {
			map_api_key = "AIzaSyDc-5LFTSC-bB9kQcZkM74LHUxwndRy_XM"; //$NON-NLS-1$
		}
		tfMapApiKey.setText(map_api_key);
		setInitialized(true);
	}

	@Override
	public String getName() {
		return CONFIG_TAB_OTHER;
	}
}
