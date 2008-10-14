package com.yoursway.modelediting.swt.demo;

import static com.yoursway.utils.Listeners.newListenersByIdentity;

import com.yoursway.utils.Listeners;

public class AngryLetter {

	private String to;
	private String from;
	private String message;

	public AngryLetter() {
	}

	private transient Listeners<LetterListener> listeners = newListenersByIdentity();

	public synchronized void addListener(LetterListener listener) {
		listeners.add(listener);
	}

	public synchronized void removeListener(LetterListener listener) {
		listeners.remove(listener);
	}

	public String to() {
		return to;
	}

	public String from() {
		return from;
	}

	public String message() {
		return message;
	}

	public void setTo(String to) {
		this.to = to;
		for (LetterListener listener : listeners)
			listener.toChanged();
	}

	public void setFrom(String from) {
		this.from = from;
		for (LetterListener listener : listeners)
			listener.fromChanged();
	}

	public void setMessage(String message) {
		this.message = message;
		for (LetterListener listener : listeners)
			listener.messageChanged();
	}
}
