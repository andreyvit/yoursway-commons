package com.yoursway.utils.bugs;

import com.yoursway.utils.Failure;

public class IllegalCaseRecovery extends Failure {

	private static final long serialVersionUID = 1L;

	public IllegalCaseRecovery(String message) {
		super();
		add("message", message);
	}

}
