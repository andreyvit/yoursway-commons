package com.yoursway.modelediting;

import java.util.List;

public interface Model {

	void addListener(IModelListener listener);

	void removeListener(IModelListener listener);

	List<Fragment> fragments();

	boolean canReplace(Fragment fragment, int startOffset, int length);

	void replace(Fragment fragment, int startOffset, int length, String text)
			throws ReplaceImpossibleException;

}
