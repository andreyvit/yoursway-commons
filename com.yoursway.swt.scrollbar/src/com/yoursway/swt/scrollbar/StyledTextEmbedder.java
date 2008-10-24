package com.yoursway.swt.scrollbar;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.swt.custom.PaintObjectEvent;
import org.eclipse.swt.custom.PaintObjectListener;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.graphics.GlyphMetrics;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Control;

public class StyledTextEmbedder {

	private static int MARGIN = 5;

	private final StyledText styledText;
	private Map<Integer, Control> offsets;

	public StyledTextEmbedder(StyledText styledText) {
		if (styledText == null)
			throw new IllegalArgumentException("styledText is null");
		this.styledText = styledText;
		styledText.addPaintObjectListener(new PaintObjectListener() {
			public void paintObject(PaintObjectEvent event) {
				StyleRange style = event.style;
				int start = style.start;
//				System.out.println(offsets);
				Control control = offsets.get(start);
				if (control != null) {
					Point pt = control.getSize();
					int x = event.x + MARGIN;
					int y = event.y + event.ascent - 2 * pt.y / 3;
//					System.out.println(x + ", " + y + " " + control);
					control.setLocation(x, y);
				} 
//					System.out.println();
			}
		});
	}

	void setStyleRange(Control control, int offset) {
		StyleRange style = new StyleRange();
		style.start = offset;
		style.length = 1;
		control.pack();
		Rectangle rect = control.getBounds();
		int ascent = 2 * rect.height / 3;
		int descent = rect.height - ascent;
		style.metrics = new GlyphMetrics(ascent + MARGIN, descent + MARGIN, rect.width + 2 * MARGIN);
		styledText.setStyleRange(style);
	}

	public void setTextWithControls(String text, Control[] controls) {
		this.offsets = new HashMap<Integer, Control>();
		styledText.setText(text);
		int lastOffset = 0;
		for (int i = 0; i < controls.length; i++) {
			int offset = text.indexOf("\uFFFC", lastOffset);
			if (offset == -1)
				break; // just skip extra controls
			this.offsets.put(offset, controls[i]);
			setStyleRange(controls[i], offset);
			lastOffset = offset + 1;
		}
	}

}
