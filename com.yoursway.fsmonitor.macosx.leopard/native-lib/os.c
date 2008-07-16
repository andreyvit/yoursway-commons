/*******************************************************************************
 * Copyright (c) 2000, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation
 *******************************************************************************/

#include "os.h"
#include "os_stats.h"

#define OS_NATIVE(func) Java_com_yoursway_fsmonitor_ChangesDetectorImpl_##func

typedef struct {
	JavaVM *vm;
	jobject that;
	jmethodID callback_method_id;
} OurContext;

JNIEnv *get_env(JavaVM *vm) {
	JNIEnv *env;
	(*vm)->GetEnv(vm, (void**) &env, JNI_VERSION_1_4);
	return env;
}

void OurContextRelease(void *clientCallBackInfo) {
	JNIEnv *env = get_env(((OurContext *) clientCallBackInfo)->vm);
	jobject that = ((OurContext *) clientCallBackInfo)->that;
	(*env)->DeleteGlobalRef(env, that);
	
	free(clientCallBackInfo);
}

void OurFSEventStreamCallback(ConstFSEventStreamRef streamRef, 
									   void *clientCallBackInfo, 
									   size_t numEvents, 
									   void *eventPaths, 
									   const FSEventStreamEventFlags eventFlags[], 
									   const FSEventStreamEventId eventIds[])
{
	JNIEnv *env = get_env(((OurContext *) clientCallBackInfo)->vm);
	jobject that = ((OurContext *) clientCallBackInfo)->that;
	jmethodID callback_method_id = ((OurContext *) clientCallBackInfo)->callback_method_id;
	
	jclass string_class = (*env)->FindClass(env, "java/lang/String");
	jobjectArray paths_array = (*env)->NewObjectArray(env, numEvents, string_class, NULL);
	jlongArray eventids_array = (*env)->NewLongArray(env, numEvents);
	
	int i;
	for (i = 0; i < numEvents; i++) {
		(*env)->SetLongArrayRegion(env, eventids_array, 0, numEvents, (jlong*) eventIds);
		jstring path = (*env)->NewStringUTF(env, ((const char**) eventPaths)[i]);
		(*env)->SetObjectArrayElement(env, paths_array, i, path);
	}
	
	(*env)->CallVoidMethod(env, that, callback_method_id, paths_array, eventids_array);

	if ((*env)->ExceptionOccurred(env)) {
		fprintf(stderr, "* java exception occurred\n");
		(*env)->ExceptionDescribe(env);
		fflush(stdout);
	}
}

JNIEXPORT jlong JNICALL OS_NATIVE(FSEventStreamCreate)
(JNIEnv *env, jobject that, jarray paths, jlong since_when, jdouble latency)
{
	OurContext *our_context = (OurContext *) malloc(sizeof(OurContext));
	(*env)->GetJavaVM(env, &our_context->vm);
	our_context->that = (*env)->NewGlobalRef(env, that);
	
	jclass klass = (*env)->GetObjectClass(env, that);
	our_context->callback_method_id = (*env)->GetMethodID(env, klass, "handleChange", "([Ljava/lang/String;[J)V");
	if (NULL == our_context->callback_method_id) {
		free(our_context);
		return 0;
	}

	int i;
	
	FSEventStreamEventId since_when_id = (since_when < 0 ? kFSEventStreamEventIdSinceNow : since_when);
	
	jsize path_count = (*env)->GetArrayLength(env, paths);
	CFStringRef refs_array[path_count];
	for (i = 0; i < path_count; i++) {
		jstring path = (*env)->GetObjectArrayElement(env, paths, i);
		const char *utf = (*env)->GetStringUTFChars(env, path, NULL);
		refs_array[i] = CFStringCreateWithCString(kCFAllocatorDefault, utf, kCFStringEncodingUTF8);
		(*env)->ReleaseStringUTFChars(env, path, utf);
	}
	CFArrayRef paths_array = CFArrayCreate(kCFAllocatorDefault, (const void**) &refs_array, path_count, &kCFTypeArrayCallBacks);
	for (i = 0; i < path_count; i++)
		CFRelease(refs_array[i]);

	FSEventStreamContext context = {0};
	context.info = our_context;
	context.release = &OurContextRelease;
	FSEventStreamRef stream = FSEventStreamCreate(kCFAllocatorDefault, &OurFSEventStreamCallback,
		&context, paths_array, since_when_id, latency, kFSEventStreamCreateFlagNoDefer);
	
	CFRelease(paths_array);
	
	return (jlong) stream;
}

JNIEXPORT void JNICALL OS_NATIVE(FSEventStreamScheduleWithRunLoop) (JNIEnv *env, jobject that, jlong stream_id) {
	jclass that_class = (*env)->GetObjectClass(env, that);
	jfieldID loop_field = (*env)->GetFieldID(env, that_class, "runLoopHandle", "J");
	CFRunLoopRef loop = (CFRunLoopRef) (*env)->GetLongField(env, that, loop_field);
	
	FSEventStreamScheduleWithRunLoop((FSEventStreamRef) stream_id, loop, kCFRunLoopCommonModes);
}

JNIEXPORT jboolean JNICALL OS_NATIVE(FSEventStreamStart) (JNIEnv *env, jobject that, jlong stream_id) {
	return FSEventStreamStart((FSEventStreamRef) stream_id) ? true : false;
}

JNIEXPORT void JNICALL OS_NATIVE(FSEventStreamStop) (JNIEnv *env, jobject that, jlong stream_id) {
	FSEventStreamStop((FSEventStreamRef) stream_id);
}

JNIEXPORT void JNICALL OS_NATIVE(FSEventStreamInvalidate) (JNIEnv *env, jobject that, jlong stream_id) {
	FSEventStreamInvalidate((FSEventStreamRef) stream_id);
}

JNIEXPORT void JNICALL OS_NATIVE(FSEventStreamRelease) (JNIEnv *env, jobject that, jlong stream_id) {
	FSEventStreamRelease((FSEventStreamRef) stream_id);
}

JNIEXPORT void JNICALL OS_NATIVE(CFRunLoopRun) (JNIEnv *env, jclass that) {
	CFRunLoopRun();
}

void OurTimerCallback(CFRunLoopTimerRef timer, void *info) {
	JNIEnv *env = get_env(((OurContext *) info)->vm);
	jobject that = ((OurContext *) info)->that;
	jmethodID callback_method_id = ((OurContext *) info)->callback_method_id;
	
	(*env)->CallVoidMethod(env, that, callback_method_id);
	
	if ((*env)->ExceptionOccurred(env)) {
		fprintf(stderr, "* java exception occurred\n");
		(*env)->ExceptionDescribe(env);
		fflush(stdout);
	}
}

JNIEXPORT void JNICALL OS_NATIVE(initializeNatives) (JNIEnv *env, jobject that) {
	CFRunLoopRef loop = CFRunLoopGetCurrent();
	
	jclass that_class = (*env)->GetObjectClass(env, that);
	jfieldID loop_field = (*env)->GetFieldID(env, that_class, "runLoopHandle", "J");
	(*env)->SetLongField(env, that, loop_field, (jlong) loop);
}

JNIEXPORT jboolean JNICALL OS_NATIVE(queueSafeReschedulingRequest) (JNIEnv *env, jobject that) {
	OurContext *our_context = (OurContext *) malloc(sizeof(OurContext));
	(*env)->GetJavaVM(env, &our_context->vm);
	our_context->that = (*env)->NewGlobalRef(env, that);
	
	jclass klass = (*env)->GetObjectClass(env, that);
	our_context->callback_method_id = (*env)->GetMethodID(env, klass, "handleSafeToReschedule", "()V");
	if (NULL == our_context->callback_method_id) {
		free(our_context);
		return 0;
	}
	
	jclass that_class = (*env)->GetObjectClass(env, that);
	jfieldID loop_field = (*env)->GetFieldID(env, that_class, "runLoopHandle", "J");
	CFRunLoopRef loop = (CFRunLoopRef) (*env)->GetLongField(env, that, loop_field);
	
	CFRunLoopTimerContext context = {0};
	context.info = our_context;
	context.release = &OurContextRelease;
	
	CFRunLoopTimerRef timer = CFRunLoopTimerCreate(kCFAllocatorDefault, CFAbsoluteTimeGetCurrent(), 0, 0, 0, &OurTimerCallback, &context);
	CFRunLoopAddTimer(loop, timer, kCFRunLoopCommonModes);
	return true;
}

