package com.yoursway.modelediting.swt;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

import com.yoursway.modelediting.Fragment;
import com.yoursway.modelediting.IModelListener;
import com.yoursway.modelediting.Model;
import com.yoursway.modelediting.ReplaceImpossibleException;

public class ModelReconciler implements IModelListener {

	private class StyledTextListener implements Listener {

		boolean disabled = false;
		
		public void handleEvent(Event event) {
			if (disabled)
				return;
			if (event.type == SWT.Verify) {		
				event.doit = false;
				try {
					if (canModify(event.start, event.end))
						modify(event.start, event.end, event.text);
				} catch (ReplaceImpossibleException e) {
					throw new RuntimeException(e);
				}
			}
		}

	}

	private final IStyledText styledText;
	private final Model model;
	private StyledTextListener styledTextListener;	
	private List<Fragment> fragments;

	public ModelReconciler(IStyledText styledText, Model model) {
		if (model == null)
			throw new NullPointerException("model is null");
		if (model == null)
			throw new NullPointerException("model is null");
		this.styledText = styledText;
		this.model = model;

		fragments = model.fragments();

		StringBuilder text = new StringBuilder();
		for (Fragment f : fragments) {
			text.append(f.toString());
		}
		styledText.setText(text.toString());

		model.addListener(this);

		styledTextListener = new StyledTextListener();
		styledText.addListener(SWT.Verify, styledTextListener);
		styledText.addListener(SWT.Modify, styledTextListener);

	}

	public int findLeftFragment(int offset, boolean isDeletion) {
		int position = 0;
		int index = 0;
		for (Fragment fragment : fragments) {
			int intervalStart = position + (fragment.includesStart() || isDeletion ? 0 : 1);
			int fLength = fragment.toString().length();
			int intervalEnd = position + (fragment.includesEnd() ? 0 : -1) + fLength;

			// System.out.println("("+intervalStart+","+intervalEnd+")");

			if (offset < intervalStart - 1)
				return -1;

			if (offset >= intervalStart && offset <= intervalEnd)
				return index;

			position += fLength;
			index++;
		}
		return -1;
	}

	public int findRightFragment(int offset, boolean isDeletion) {
		int position = 0;
		int index = 0;
		int rightmost = -1;
		for (Fragment fragment : fragments) {
			int intervalStart = position + (fragment.includesStart() ? 0 : 1);
			int fLength = fragment.toString().length();
			int intervalEnd = position + (fragment.includesEnd() || isDeletion ? 0 : -1) + fLength;

			if (offset < intervalStart - 1)
				return rightmost;

			if (offset >= intervalStart && offset <= intervalEnd)
				rightmost = index;

			position += fLength;
			index++;
		}
		return rightmost;
	}

	/**
	 * FIXME: speedup by caching fragment offsets
	 */
	public int getFragmentOffset(int index) {
		int pos = 0;
		int num = 0;
		for (Fragment fragment : fragments) {
			if (num == index)
				return pos;
			pos += fragment.toString().length();
			num++;
		}
		return pos;
	}

	public boolean canModify(int start, int end) {
		boolean isDeletion = start < end;
		int startFragment = findLeftFragment(start, isDeletion);
		int endFragment = findRightFragment(end, isDeletion);
		if (startFragment == -1 || endFragment == -1 || startFragment > endFragment)
			return false;
		int startOffset = start - getFragmentOffset(startFragment);
		boolean hasModifiableArea = false;
		for (int i = startFragment; i <= endFragment; i++) {
			int endOffset;
			Fragment fragment = fragments.get(i);
			if (i == endFragment) {
				endOffset = end - getFragmentOffset(endFragment);
			} else {
				endOffset = fragment.toString().length();
			}
			startOffset = 0;
			if (fragment.canReplace(startOffset, endOffset - startOffset)) {
				hasModifiableArea = true;
			}
		}
		return hasModifiableArea;
	}

	public void modify(int start, int end, String text) throws ReplaceImpossibleException {
		if (text == null)
			throw new NullPointerException("text is null");
		boolean isDeletion = (start < end);
		int startFragment = findLeftFragment(start, isDeletion);
		int endFragment = findRightFragment(end, isDeletion);
		if (startFragment == -1 || endFragment == -1 || startFragment > endFragment)
			throw new ReplaceImpossibleException("Given text ranges are not available");
		int startOffset = start - getFragmentOffset(startFragment);
		for (int i = startFragment; i <= endFragment; i++) {
			int endOffset;
			Fragment fragment = fragments.get(i);
			if (i == endFragment)
				endOffset = end - getFragmentOffset(endFragment);
			else
				endOffset = fragment.toString().length();
			if (fragment.canReplace(startOffset, endOffset - startOffset)) {
				model.replace(this, fragment, startOffset, endOffset - startOffset, text);
				text = "";
			}
			startOffset = 0;
		}
		if (!text.equals("")) {
			throw new ReplaceImpossibleException("Text ranges are not editable");
		}
	}

	public void modelChanged(Object sender, Model model, int firstFragment, int oldCount,
			int newCount) {
		int cutLength = 0, startOffset = 0;
		for (int i = 0; i < firstFragment + oldCount; i++) {
			if (i >= firstFragment)
				cutLength += fragments.get(i).toString().length();
			else
				startOffset += fragments.get(i).toString().length();
		}
		StringBuilder newText = new StringBuilder();
		for (int i = firstFragment; i < firstFragment + newCount; i++) {
			newText.append(model.fragments().get(i));
		}
		this.fragments = new ArrayList<Fragment>(model.fragments());
		final String text = styledText.getText();
		final String text2 = text.substring(0, startOffset) + newText
				+ text.substring(startOffset + cutLength);
		Display.getDefault().syncExec(new Runnable(){

			public void run() {
				styledTextListener.disabled = true;
				if (!text.equals(text2)) 
					styledText.setText(text2);		
				styledTextListener.disabled = false;
				
			}
			
		});
	}

	void dispose() {
		model.removeListener(this);
		styledText.removeListener(SWT.Verify, styledTextListener);
		styledText.removeListener(SWT.Modify, styledTextListener);
	}

}
