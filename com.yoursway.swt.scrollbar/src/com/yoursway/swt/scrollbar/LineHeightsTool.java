package com.yoursway.swt.scrollbar;

import org.eclipse.swt.custom.PaintObjectEvent;
import org.eclipse.swt.custom.PaintObjectListener;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.GlyphMetrics;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;

public class LineHeightsTool {

	private StyledText styledText;

	public LineHeightsTool(StyledText styledText) {
		if (styledText == null)
			throw new IllegalArgumentException("styledText is null");
		this.styledText = styledText;
		installListener();
	}
	
	public LineHeightsTool(Composite parent, int style) {
		styledText = new StyledText(parent, style);
		installListener();
	}

	private void installListener() {
		styledText.addPaintObjectListener(new PaintObjectListener() {
			public void paintObject(PaintObjectEvent event) {
				StyleRange style = event.style;
				int start = style.start;
				String text = styledText.getText(start, start);
				event.gc.setFont(style.font);
				float fontHeight = style.font.getFontData()[0].height;
				event.gc.drawText(text, event.x,
						(int) (event.y + style.metrics.ascent - fontHeight), true);
			}
		});	
	}

	public StyledText styledText() {
		return styledText;
	}

	public void setLineProperties(int start, int length, int pxBeforeLine, int pxAfterLine) {
		int textLength = styledText.getText().length();
		if (start < 0 || start >= textLength || start + length > textLength)
			throw new AssertionError("start & length are out of bounds");
		for (int i = start; i < start + length; i++) {
			Rectangle bounds = styledText.getTextBounds(i, i);
			StyleRange oldRange = styledText.getStyleRangeAtOffset(i);
			Font font = null;
			if (oldRange == null) {
				oldRange = new StyleRange();
				font = styledText.getFont();
			} else {
				font = oldRange.font;
				if (font == null)
					font = styledText.getFont();
			}
			float fontHeight = font.getFontData()[0].height;
			int ascent = (int) (pxBeforeLine + fontHeight);
			int descent = pxAfterLine;
			oldRange.metrics = new GlyphMetrics(ascent, descent, Math.max(4, bounds.width)); // XXX: 4!
			oldRange.start = i;
			oldRange.length = 1;
			styledText.setStyleRange(oldRange);
		}
	}

}
