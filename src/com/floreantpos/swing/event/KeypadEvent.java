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