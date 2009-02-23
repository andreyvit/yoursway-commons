package com.yoursway.utils.bugs;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.yoursway.utils.annotations.UsedFromGeneratedByteCode;

public class Bugs {
    
    private static BugHandler handler;
    
    static class ReflectionBugHandler implements BugHandler {
    	
    	private final Method method;

		public ReflectionBugHandler(Method method) {
			if (method == null)
				throw new NullPointerException("method is null");
			this.method = method;
		}

		public void bug(Throwable throwable) {
			try {
				method.invoke(null, throwable);
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
		}
    	
    }
    
    static {
    	try {
    		Class<?> feedbackClass = Thread.currentThread().getContextClassLoader().loadClass("com.yoursway.feedback.Feedback");
    		Method method = feedbackClass.getMethod("bug", Throwable.class);
    		handler = new ReflectionBugHandler(method);
		} catch (ClassNotFoundException e) {
		} catch (SecurityException e) {
		} catch (NoSuchMethodException e) {
		}
		if (handler == null)
			handler = new DefaultBugHandler();
    }
    
    public static void setHandler(BugHandler handler) {
        Bugs.handler = handler;
    }
    
    public static void bug(Throwable throwable) {
    	handler.bug(throwable);
    }
    
    public static void listenerFailed(Throwable error, Object listener, String event) {
        bug(new ListenerFailed(error).add("event", event));
    }
    
    @UsedFromGeneratedByteCode
    public static void listenerFailed(Throwable error, Object listener) {
        bug(new ListenerFailed(error));
    }
    
    public static void illegalCaseRecovery(Severity severity, String description) {
    	bug(new IllegalCaseRecovery(description));
    }
    
    public static void illegalCaseRecovery(String description) {
    	bug(new IllegalCaseRecovery(description));
    }
    
    public static void unknownErrorRecovery(Severity severity, Throwable error) {
    	bug(error);
    }
    
    public static void unknownErrorRecovery(Throwable error) {
    	bug(error);
    }
    
    public static void cleanupFailed(Throwable error, Object target) {
    	bug(new DisposeFailed(error));
    }
    
}
