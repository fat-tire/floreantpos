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