package com.floreantpos.swing;

import java.awt.AWTEvent;
import java.awt.Cursor;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.AWTEventListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JComponent;
import javax.swing.JViewport;
import javax.swing.SwingUtilities;

public class TouchScrollHandler extends MouseAdapter implements AWTEventListener {

	private Point origin;
	private boolean wasDragging;

	public TouchScrollHandler() {
	}

	@Override
	public void mousePressed(MouseEvent e) {
		origin = new Point(e.getPoint());
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if (wasDragging) {
			e.getComponent().setCursor(Cursor.getDefaultCursor());
			wasDragging = false;
		}
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		if (origin != null) {
			JViewport viewPort = (JViewport) SwingUtilities.getAncestorOfClass(JViewport.class, e.getComponent());
			if (viewPort != null) {
				

				Rectangle view = viewPort.getViewRect();
				//MouseEvent convertMouseEvent = SwingUtilities.convertMouseEvent(e.getComponent(), e, viewPort);
				//PosLog.debug(getClass(),convertMouseEvent.getY());
				
				//if (convertMouseEvent.getY() >= 0) {
					int deltaX = origin.x - e.getX();
					int deltaY = origin.y - e.getY();
					
					view.x += deltaX;
					view.y += deltaY;

					e.getComponent().setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
					((JComponent) e.getComponent()).scrollRectToVisible(view);
					wasDragging = true;
					e.consume();
				//}
			}
		}
	}

	@Override
	public void eventDispatched(AWTEvent event) {
		switch (event.getID()) {
			case MouseEvent.MOUSE_PRESSED:
				mousePressed((MouseEvent) event);
				break;

			case MouseEvent.MOUSE_RELEASED:
				mouseReleased((MouseEvent) event);
				break;

			case MouseEvent.MOUSE_DRAGGED:
				mouseDragged((MouseEvent) event);
				break;

			default:
				break;
		}
	}
}