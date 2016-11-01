package com.floreantpos.ui.ticket;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;

import javax.swing.JTable;
import javax.swing.JTextPane;
import javax.swing.border.EmptyBorder;
import javax.swing.table.TableCellRenderer;
import javax.swing.text.AbstractDocument;
import javax.swing.text.BoxView;
import javax.swing.text.ComponentView;
import javax.swing.text.Element;
import javax.swing.text.IconView;
import javax.swing.text.LabelView;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.ParagraphView;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import javax.swing.text.StyledEditorKit;
import javax.swing.text.View;
import javax.swing.text.ViewFactory;

import com.floreantpos.swing.PosUIManager;

public class MultiLineTableCellRenderer extends JTextPane implements TableCellRenderer {

	public MultiLineTableCellRenderer() {
		setOpaque(true);
		setEditorKit(new MyEditorKit());

		setBorder(new EmptyBorder(10, 2, 10, 2));
	}

	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
		int colWidth = table.getTableHeader().getColumnModel().getColumn(column).getWidth();
		setSize(new Dimension(colWidth, 240));

		int height = getPreferredSize().height;
		height = height < 60 ? 60 : height;
		height = PosUIManager.getSize(height);

		if (table.getRowHeight() < height) {
			table.setRowHeight(height);
		}

		if (isSelected) {
			setBackground(table.getSelectionBackground());
			setForeground(this, table.getSelectionForeground());
		}
		else {
			setBackground(table.getBackground());
			setForeground(this, table.getForeground());
		}

		if (value != null) {
			setText(value.toString());
		}
		else {
			setText("");
		}

		return this;
	}

	public static void setForeground(JTextPane jtp, Color c) {
		MutableAttributeSet attrs = jtp.getInputAttributes();
		StyleConstants.setForeground(attrs, c);
		StyledDocument doc = jtp.getStyledDocument();
		doc.setCharacterAttributes(0, doc.getLength() + 1, attrs, false);
	}

	public static class MyEditorKit extends StyledEditorKit {
		public ViewFactory getViewFactory() {
			return new StyledViewFactory();
		}

		static class StyledViewFactory implements ViewFactory {
			public View create(Element elem) {
				String kind = elem.getName();
				if (kind != null) {
					if (kind.equals(AbstractDocument.ContentElementName)) {
						return new LabelView(elem);
					}
					else if (kind.equals(AbstractDocument.ParagraphElementName)) {
						return new ParagraphView(elem);
					}
					else if (kind.equals(AbstractDocument.SectionElementName)) {
						return new CenteredBoxView(elem, View.Y_AXIS);
					}
					else if (kind.equals(StyleConstants.ComponentElementName)) {
						return new ComponentView(elem);
					}
					else if (kind.equals(StyleConstants.IconElementName)) {
						return new IconView(elem);
					}
				}

				return new LabelView(elem);
			}

		}
	}

	static class CenteredBoxView extends BoxView {
		public CenteredBoxView(Element elem, int axis) {
			super(elem, axis);
		}

		protected void layoutMajorAxis(int targetSpan, int axis, int[] offsets, int[] spans) {
			super.layoutMajorAxis(targetSpan, axis, offsets, spans);
			int textBlockHeight = 0;
			int offset = 0;

			for (int i = 0; i < spans.length; i++) {
				textBlockHeight = spans[i];
			}

			offset = (targetSpan - textBlockHeight) / 2;
			for (int i = 0; i < offsets.length; i++) {
				offsets[i] += offset;
			}

		}
	}
}