package com.floreantpos.config.ui;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

import javax.swing.JLabel;
import javax.swing.JOptionPane;

import net.miginfocom.swing.MigLayout;

import org.jfree.util.Log;

import com.floreantpos.PosLog;
import com.floreantpos.main.Application;
import com.floreantpos.swing.IntegerTextField;
import com.floreantpos.util.JarUtil;

public class InginicoConfigurationView extends ConfigurationView {
	
	public static final String COMMSETTING_INI = "commsetting.ini";

	private JLabel lblCommunicationTitle;
	private JLabel lblIp;
	private JLabel lblPort;
	private JLabel lblTimeOut;

	private IntegerTextField txtIp1;
	private IntegerTextField txtIp2;
	private IntegerTextField txtIp3;
	private IntegerTextField txtIp4;

	private IntegerTextField txtPort;
	private IntegerTextField txtTimeOut;
	
	public InginicoConfigurationView() {

		createPacksConfiguration();

	}

	private void createPacksConfiguration() {
		lblCommunicationTitle = new JLabel("Communication type:");
		lblIp = new JLabel("Ip");
		lblPort = new JLabel("Port");
		lblTimeOut = new JLabel("Timeout");

		txtIp1 = new IntegerTextField(7);
		txtIp2 = new IntegerTextField(7);
		txtIp3 = new IntegerTextField(7);
		txtIp4 = new IntegerTextField(7);

		txtPort = new IntegerTextField(20);
		txtTimeOut = new IntegerTextField(20);
		
		setLayout(new MigLayout("", " []10[grow]", ""));

		add(lblCommunicationTitle);
		add(new JLabel("TCP"), "wrap");

		/*add(lblIp);
		add(txtIp1, "split 7");
		add(new JLabel("."));
		add(txtIp2);
		add(new JLabel("."));
		add(txtIp3);
		add(new JLabel("."));
		add(txtIp4, "wrap");*/

		add(lblPort);
		add(txtPort, "grow,wrap");

		add(lblTimeOut);
		add(txtTimeOut, "grow");
	}

	@Override
	public boolean save() throws Exception {

		if(ipPortisEmpty(txtPort) || ipPortisEmpty(txtIp1) || ipPortisEmpty(txtIp2) || ipPortisEmpty(txtIp3) || ipPortisEmpty(txtIp4) || ipPortisEmpty(txtTimeOut)) {
			JOptionPane.showMessageDialog(null, "Please fill all the fields properly ");
			return false;
		}

		String jarLocation = JarUtil.getJarLocation(Application.class);
		PrintWriter writer = null;
		try {

			File newFile = new File(jarLocation, COMMSETTING_INI);
			writer = new PrintWriter(newFile, "UTF-8");

			writer.println("[COMMUNICATE]");
			writer.println("PORT=" + txtPort.getText());
			writer.println("DestPort=10009");
			writer.println("IP=" + txtIp1.getText() + String.valueOf(".") + txtIp2.getText() + String.valueOf(".") + txtIp3.getText() + String.valueOf(".")
					+ txtIp4.getText());
			writer.println("DestIP=10.0.2.15");
			writer.println("SERIALPORT=COM1");
			writer.println("TimeOut=3000");
			writer.println("TIMEOUT=" + txtTimeOut.getText());
			writer.println("CommType=TCP");
			
		} catch (Exception e) {
			PosLog.error(getClass(), e);
		} finally {
			try {
				writer.close();
			} catch (Exception e2) {
			}
		}
		return false;
	}

	@Override
	public void initialize() throws Exception {

		String jarLocation = JarUtil.getJarLocation(Application.class);
		BufferedReader fileReader = null;
		try {

			File file = new File(jarLocation, COMMSETTING_INI);
			if(!file.exists()) {
				/*txtIp1.setText(String.valueOf("127"));
				txtIp2.setText(String.valueOf("0"));
				txtIp3.setText(String.valueOf("0"));
				txtIp4.setText(String.valueOf("1"));*/
				txtPort.setText(String.valueOf("5656"));
				txtTimeOut.setText(String.valueOf("5000"));
				return;
			}

			fileReader = new BufferedReader(new FileReader(file));
			String line;
			while ((line = fileReader.readLine()) != null) {

				String str[] = line.split("=");

				for (int i = 0; i < str.length; i++) {

					if(str[i].equals("PORT")) {
						String port = str[i + 1];
						txtPort.setText(port);
						break;
					}

					if(str[i].equals("IP")) {

						String ip = str[i + 1];
						String ipStr[] = ip.split("\\.");

						txtIp1.setText(ipStr[0]);
						txtIp2.setText(ipStr[1]);
						txtIp3.setText(ipStr[2]);
						txtIp4.setText(ipStr[3]);
					}

					if(str[i].equals("TIMEOUT")) {
						String timeOut = str[i + 1];
						txtTimeOut.setText(timeOut);
						break;
					}
				}
			}
			
		} catch (IOException e) {
			Log.debug(e); 
		} finally {
			try {
				fileReader.close();
			} catch (Exception e2) {
			}
		}

	}

	public boolean ipPortisEmpty(IntegerTextField txtF) {
		if(txtF.getText().equals(String.valueOf(""))) {
			return true;
		}
		return false;
	}

	@Override
	public String getName() {
		return null;
	}

}
