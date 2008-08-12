package com.yoursway.utils.broadcaster;

import static org.objectweb.asm.Opcodes.ACC_PRIVATE;
import static org.objectweb.asm.Opcodes.ACC_PUBLIC;
import static org.objectweb.asm.Opcodes.ALOAD;
import static org.objectweb.asm.Opcodes.ARETURN;
import static org.objectweb.asm.Opcodes.ASTORE;
import static org.objectweb.asm.Opcodes.CHECKCAST;
import static org.objectweb.asm.Opcodes.DUP;
import static org.objectweb.asm.Opcodes.GETFIELD;
import static org.objectweb.asm.Opcodes.GOTO;
import static org.objectweb.asm.Opcodes.IFEQ;
import static org.objectweb.asm.Opcodes.INVOKEINTERFACE;
import static org.objectweb.asm.Opcodes.INVOKESPECIAL;
import static org.objectweb.asm.Opcodes.INVOKESTATIC;
import static org.objectweb.asm.Opcodes.INVOKEVIRTUAL;
import static org.objectweb.asm.Opcodes.NEW;
import static org.objectweb.asm.Opcodes.PUTFIELD;
import static org.objectweb.asm.Opcodes.RETURN;
import static org.objectweb.asm.Opcodes.V1_5;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;

import com.yoursway.utils.Listeners;
import com.yoursway.utils.bugs.Bugs;

public class BroadcasterFactory<Listener> {

    private static final String IMPL_SUFFIX = "_MagicBroadcasterImpl";
    
    private static final String FACTORY_SUFFIX = "_MagicBroadcasterFactory";
    
    private static final String THROWABLE_NAME = "java/lang/Throwable";

    private static final String OBJECT_NAME = "java/lang/Object";
    
    private static final String OBJECT_SIG = "L" + OBJECT_NAME + ";";
    
    private static final String ADD_OR_REMOVE_LISTENER_SIG = "(" + OBJECT_SIG + ")V";
    
    private static final String ITERATOR_NAME = "java/util/Iterator";
    
    private static final String ITERATOR_SIG = "L" + ITERATOR_NAME + ";";
    
    private static final String LISTENERS_FIELD = "listeners";
    
    private static final String LISTENERS_SIG = signatureOf(Listeners.class);
    
    private static final String BUGS_NAME = internalNameOf(Bugs.class);
    
    private static final String FACTORY_INTERFACE_NAME = internalNameOf(InternalBroadcasterFactory.class);
    
    private static final String BROADCASTER_INTERFACE_NAME = internalNameOf(Broadcaster.class);
    
    private static final String LISTENERS_NAME = internalNameOf(Listeners.class);
    
    private static final String INIT = "<init>";
    
    private static final String[] NO_EXCEPTIONS = new String[0];

    private final InternalBroadcasterFactory factory;
    
    public BroadcasterFactory(Class<Listener> listenerInterface, ClassLoader classLoader) {
        if (listenerInterface == null)
            throw new NullPointerException("listenerInterface is null");
        if (classLoader == null)
            throw new NullPointerException("classLoader is null");
        
        String interfaceName = internalNameOf(listenerInterface);
        final String factoryName = interfaceName + FACTORY_SUFFIX;
        final String className = interfaceName + IMPL_SUFFIX;
        final byte[] factoryCode = emitFactory(factoryName, className);
        final byte[] implCode = emitImpl(interfaceName, className, listenerInterface.getDeclaredMethods());
        
//      ClassReader r = new ClassReader(implCode);
//      TraceClassVisitor v = new TraceClassVisitor(new PrintWriter(System.out));
//      r.accept(v, 0);

        ClassLoader loader = new ClassLoader(listenerInterface.getClassLoader()) {
            
            protected Class<?> findClass(String name) throws ClassNotFoundException {
                if (name.equals(className))
                    return defineClass(className.replace('/', '.'), implCode, 0, implCode.length);
                if (name.equals(factoryName))
                    return defineClass(factoryName.replace('/', '.'), factoryCode, 0, factoryCode.length);
                throw new ClassNotFoundException(name);
            }
            
        };
        loadClass(loader, className); // dunno why it's needed, but it is
        Class<? extends InternalBroadcasterFactory> klass = loadClass(loader, factoryName);
        try {
            this.factory = klass.newInstance();
        } catch (InstantiationException e) {
            throw new AssertionError(e);
        } catch (IllegalAccessException e) {
            throw new AssertionError(e);
        }
    }
    
    @SuppressWarnings("unchecked")
    public Broadcaster<Listener> newInstance() {
        return (Broadcaster<Listener>) factory.create();
    }
    
    @SuppressWarnings("unchecked")
    public static <Listener> void addBroadcasterListener(Listener broadcaster, Listener listener) {
        ((Broadcaster) broadcaster).addListener(listener);
    }
    
    @SuppressWarnings("unchecked")
    public static <Listener> void removeBroadcasterListener(Listener broadcaster, Listener listener) {
        ((Broadcaster) broadcaster).removeListener(listener);
    }

    @SuppressWarnings("unchecked")
    private Class<? extends InternalBroadcasterFactory> loadClass(ClassLoader loader, final String className)
            throws AssertionError {
        Class<? extends InternalBroadcasterFactory> klass;
        try {
            klass = (Class<? extends InternalBroadcasterFactory>) loader.loadClass(className);
        } catch (ClassNotFoundException e) {
            throw new AssertionError(e);
        }
        return klass;
    }
    

    private static Map<Class<?>, BroadcasterFactory<?>> broadcasters = new HashMap<Class<?>, BroadcasterFactory<?>>();
    
    public synchronized static <T> Broadcaster<T> newBroadcaster(Class<T> listenerInterface) {
        return newBroadcasterFactory(listenerInterface).newInstance();
    }
    
    /**
     * <code>com.yoursway.utils</code> plugin must be visible from the plugin
     * where the given interface is defined. If not, please use an overloaded
     * version which allows explicit specification of a class loader.
     */
    public synchronized static <T> BroadcasterFactory<T> newBroadcasterFactory(Class<T> listenerInterface) {
        return newBroadcasterFactory(listenerInterface, listenerInterface.getClassLoader());
    }
    
    @SuppressWarnings("unchecked")
    public synchronized static <T> BroadcasterFactory<T> newBroadcasterFactory(Class<T> listenerInterface, ClassLoader classLoader) {
        BroadcasterFactory<T> result = (BroadcasterFactory<T>) broadcasters.get(listenerInterface);
        if (result == null) {
            result = new BroadcasterFactory(listenerInterface, classLoader);
            broadcasters.put(listenerInterface, result);
        }
        return result;
    }
    
    private static <T> byte[] emitFactory(String factoryName, String className) {
        ClassWriter w = new ClassWriter(0);
        w.visit(V1_5, ACC_PUBLIC, factoryName, null, OBJECT_NAME, new String[] { FACTORY_INTERFACE_NAME });
        
        emitDefaultConstructor(className, w);
        emitFactoryMethod(className, w);
        w.visitEnd();
        
        return w.toByteArray();
    }

    private static void emitFactoryMethod(String className, ClassWriter w) {
        MethodVisitor mw = w.visitMethod(ACC_PUBLIC, "create", "()" + OBJECT_SIG, null, NO_EXCEPTIONS);
        mw.visitTypeInsn(NEW, className);
        mw.visitInsn(DUP);
        mw.visitMethodInsn(INVOKESPECIAL, className, INIT, "()V");
        mw.visitInsn(ARETURN);
        mw.visitMaxs(2, 1);
        mw.visitEnd();
    }
    
    private static <T> byte[] emitImpl(String interfaceName, String className, Method[] methods) {
        ClassWriter w = new ClassWriter(0);
        w.visit(V1_5, ACC_PUBLIC, className, null, OBJECT_NAME, new String[] { interfaceName, BROADCASTER_INTERFACE_NAME });
        
        w.visitField(ACC_PRIVATE, LISTENERS_FIELD, LISTENERS_SIG, null, null).visitEnd();
        emitConstructor(className, w);
        emitBroadcasterMethodThunk(className, "addListener", "add", w);
        emitBroadcasterMethodThunk(className, "removeListener", "remove", w);
        emitFireMethod(className, w);

        for (Method method : methods) {
            String name = method.getName();
            if (name.equals("addListener") || name.equals("removeListener"))
                continue;
            emitListenerMethodThunk(interfaceName, className, w, descriptorOf(method), name,
                    exceptionsOf(method), method.getParameterTypes().length);
        }
        
        w.visitEnd();
        
        return w.toByteArray();
    }
    
    private static void emitBroadcasterMethodThunk(String className, String methodName, String targetName, ClassWriter w) {
        MethodVisitor mw = w.visitMethod(ACC_PUBLIC, methodName, ADD_OR_REMOVE_LISTENER_SIG, null, NO_EXCEPTIONS);
        mw.visitVarInsn(ALOAD, 0);
        mw.visitFieldInsn(GETFIELD, className, LISTENERS_FIELD, LISTENERS_SIG);
        mw.visitVarInsn(ALOAD, 1);
        mw.visitMethodInsn(INVOKEVIRTUAL, LISTENERS_NAME, targetName, ADD_OR_REMOVE_LISTENER_SIG);
        mw.visitInsn(RETURN);
        mw.visitMaxs(2, 2);
        mw.visitEnd();
    }
    
    private static void emitFireMethod(String className, ClassWriter w) {
        MethodVisitor mw = w.visitMethod(ACC_PUBLIC, "fire", "()" + OBJECT_SIG, null, NO_EXCEPTIONS);
        mw.visitVarInsn(ALOAD, 0);
        mw.visitInsn(ARETURN);
        mw.visitMaxs(1, 1);
        mw.visitEnd();
    }
    
    private static void emitListenerMethodThunk(String interfaceName, String className, ClassWriter w,
            String signature, String methodName, String[] exceptions, int argCount) {
        MethodVisitor mw = w.visitMethod(ACC_PUBLIC, methodName, signature, null, exceptions);
        int firstLocal = argCount + 1;
        int secondLocal = firstLocal + 1;
        Label startLoop = new Label();
        Label endLoop = new Label();
        Label tryStart = new Label();
        Label catchStart = new Label();
        
        mw.visitVarInsn(ALOAD, 0);
        mw.visitFieldInsn(GETFIELD, className, LISTENERS_FIELD, LISTENERS_SIG);
        mw.visitMethodInsn(INVOKEVIRTUAL, LISTENERS_NAME, "iterator", "()" + ITERATOR_SIG);
        mw.visitVarInsn(ASTORE, firstLocal);
        
        mw.visitLabel(startLoop);
        mw.visitVarInsn(ALOAD, firstLocal);
        mw.visitMethodInsn(INVOKEINTERFACE, ITERATOR_NAME, "hasNext", "()Z");
        mw.visitJumpInsn(IFEQ, endLoop);
        
        mw.visitVarInsn(ALOAD, firstLocal);
        mw.visitMethodInsn(INVOKEINTERFACE, ITERATOR_NAME, "next", "()" + OBJECT_SIG);
        mw.visitTypeInsn(CHECKCAST, interfaceName);
        mw.visitVarInsn(ASTORE, secondLocal);
        
        mw.visitLabel(tryStart);
        mw.visitVarInsn(ALOAD, secondLocal);
        for (int arg = 1; arg <= argCount; arg++)
            mw.visitVarInsn(ALOAD, arg);
        mw.visitMethodInsn(INVOKEINTERFACE, interfaceName, methodName, signature);
        mw.visitJumpInsn(GOTO, startLoop);
        
        mw.visitLabel(catchStart);
        mw.visitTryCatchBlock(tryStart, catchStart, catchStart, THROWABLE_NAME);
        mw.visitVarInsn(ALOAD, secondLocal);
        mw.visitMethodInsn(INVOKESTATIC, BUGS_NAME, "listenerFailed", "(Ljava/lang/Throwable;Ljava/lang/Object;)V");
        mw.visitJumpInsn(GOTO, startLoop);
        
        mw.visitLabel(endLoop);
        mw.visitInsn(RETURN);
        
        mw.visitMaxs(Math.max(2, argCount + 1), argCount + 3);
        mw.visitEnd();
    }
    
    private static void emitConstructor(String className, ClassWriter w) {
        MethodVisitor cw = w.visitMethod(ACC_PUBLIC, INIT, "()V", null, NO_EXCEPTIONS);
        cw.visitVarInsn(ALOAD, 0);
        cw.visitMethodInsn(INVOKESPECIAL, OBJECT_NAME, INIT, "()V");
        cw.visitVarInsn(ALOAD, 0);
        cw.visitTypeInsn(NEW, LISTENERS_NAME);
        cw.visitInsn(DUP);
        cw.visitMethodInsn(INVOKESPECIAL, LISTENERS_NAME, INIT, "()V");
        cw.visitFieldInsn(PUTFIELD, className, LISTENERS_FIELD, LISTENERS_SIG);
        cw.visitInsn(RETURN);
        cw.visitMaxs(3, 1);
        cw.visitEnd();
    }
    
    private static void emitDefaultConstructor(String className, ClassWriter w) {
        MethodVisitor cw = w.visitMethod(ACC_PUBLIC, INIT, "()V", null, NO_EXCEPTIONS);
        cw.visitVarInsn(ALOAD, 0);
        cw.visitMethodInsn(INVOKESPECIAL, OBJECT_NAME, INIT, "()V");
        cw.visitInsn(RETURN);
        cw.visitMaxs(1, 1);
        cw.visitEnd();
    }
    
    public static String signatureOf(Class<?> klass) {
        String name = klass.getName();
        if (name.startsWith("["))
            return name;
        if (name.equals("boolean"))
            return "Z";
        if (name.equals("byte"))
            return "B";
        if (name.equals("char"))
            return "C";
        if (name.equals("double"))
            return "D";
        if (name.equals("float"))
            return "F";
        if (name.equals("int"))
            return "I";
        if (name.equals("long"))
            return "J";
        if (name.equals("short"))
            return "S";
        if (name.equals("void"))
            return "V";
        return "L" + internalNameOf(klass) + ";";
    }
    
    private static String internalNameOf(Class<?> klass) {
        return internalNameOf(klass.getName());
    }
    
    private static String internalNameOf(String name) {
        return name.replace('.', '/');
    }
    
    private static String descriptorOf(Method method) {
        StringBuilder result = new StringBuilder();
        result.append('(');
        for (Class<?> klass : method.getParameterTypes())
            result.append(signatureOf(klass));
        result.append(')');
        result.append(signatureOf(method.getReturnType()));
        return result.toString();
    }
    
    private static String[] exceptionsOf(Method method) {
        Class<?>[] types = method.getExceptionTypes();
        String[] result = new String[types.length];
        for (int i = 0; i < types.length; i++)
            result[i] = internalNameOf(types[i]);
        return result;
    }
    
}
