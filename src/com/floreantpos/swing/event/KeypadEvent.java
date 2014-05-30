package com.floreantpos.swing.event;
import java.awt.AWTEvent;

public class KeypadEvent extends AWTEvent {

public static final int TEXT_CHANGED = 1;
public static final int ENTER_TRIGGERED = 2;
public static final int CLEAR_TRIGGERED = 3;

private int _eventId;

public KeypadEvent(Object source, int id) {
super(source, id);
_eventId = id;
}

public int getEventId() {
return _eventId;
}

}