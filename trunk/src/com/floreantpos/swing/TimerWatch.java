package com.floreantpos.swing;

//A simple clock application using javax.swing.Timer class

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;

public class TimerWatch extends JPanel implements ActionListener {
	int hour;
	int min;
	int sec;
	
	Timer updateTimer = new Timer(1000, this);
	JLabel timerLabel = new JLabel();
	
	public TimerWatch() {
		timerLabel.setFont(timerLabel.getFont().deriveFont(Font.BOLD));
		timerLabel.setHorizontalAlignment(JLabel.RIGHT);
		timerLabel.setText(hour + ":" + min + ":" + sec);
		
		add(timerLabel);
	}

	public int getHour() {
		return hour;
	}

	public void increaseHour() {
		++hour;
	}

	public int getMin() {
		return min;
	}

	public void increaseMin() {
		++min;
		if(min > 60) {
			min = 0;
			increaseHour();
		}
	}

	public int getSec() {
		return sec;
	}

	public void increaseSec() {
		++sec;
		if(sec > 60) {
			sec = 0;
			increaseMin();
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		increaseSec();
		timerLabel.setText(hour + ":" + min + ":" + sec);
	}
	
	public void start() {
		updateTimer.start();
	}
	
	public void stop() {
		updateTimer.stop();
	}
}