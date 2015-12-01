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
package com.floreantpos.swing;

//A simple clock application using javax.swing.Timer class

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;

import org.joda.time.Duration;
import org.joda.time.Instant;
import org.joda.time.Interval;

public class TimerWatch extends JPanel implements ActionListener {
	
	Timer updateTimer = new Timer(1000, this);
	JLabel timerLabel = new JLabel();
	private final Date date;
	
	public TimerWatch(Date date) {
		this.date = date;
		
		timerLabel.setFont(timerLabel.getFont().deriveFont(Font.BOLD));
		timerLabel.setHorizontalAlignment(JLabel.RIGHT);
		
		actionPerformed(null);
		
		add(timerLabel);
	}



	@Override
	public void actionPerformed(ActionEvent e) {
		Interval interval = new Interval(date.getTime(), new Instant().getMillis());
		Duration duration = interval.toDuration();
		
		timerLabel.setText(duration.getStandardHours() + ":" + (duration.getStandardMinutes()%60) + ":" + (duration.getStandardSeconds()%60)); //$NON-NLS-1$ //$NON-NLS-2$
	}
	
	public void start() {
		updateTimer.start();
	}
	
	public void stop() {
		updateTimer.stop();
	}
}