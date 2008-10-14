package com.yoursway.modelediting.swt.demo;

import com.yoursway.modelediting.AbstractModel;
import com.yoursway.modelediting.Fragment;
import com.yoursway.modelediting.IModelListener;
import com.yoursway.modelediting.ReplaceImpossibleException;

public class TextFormModel extends AbstractModel {

	public boolean canReplace(Fragment fragment, int startOffset, int length) {
		if (fragment == null)
			throw new NullPointerException("fragment is null");

		String fragmentText = fragment.toString();
		if (startOffset < 0 || length < 0 || startOffset + length > fragmentText.length())
			return false;

		return fragment.canReplace(startOffset, length);
	}

	public void replace(Fragment fragment, int startOffset, int length, String text)
			throws ReplaceImpossibleException {
		if (fragment == null)
			throw new NullPointerException("fragment is null");
		if (text == null)
			throw new NullPointerException("text is null");
		if (!canReplace(fragment, startOffset, length))
			throw new ReplaceImpossibleException();

		fragment.replace(startOffset, length, text);

		for (IModelListener listener : listeners)
			listener.modelChanged(this, fragments.indexOf(fragment), 1, 1);
	}

}
