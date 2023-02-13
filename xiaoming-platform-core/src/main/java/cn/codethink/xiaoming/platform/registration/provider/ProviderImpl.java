package cn.codethink.xiaoming.platform.registration.provider;

import cn.codethink.common.util.Preconditions;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

public class ProviderImpl
    implements Provider {
    
    private final boolean recursive;
    private final boolean accessible;
    private final int modifiers;
    
    public static class BuilderImpl
        implements Builder {
        
        private boolean recursive = false;
        private boolean accessible = false;
        private int modifiers = 0x00000000;
    
        @Override
        public Builder recursive(boolean recursive) {
            this.recursive = recursive;
            return this;
        }
    
        @Override
        public Builder accessible(boolean accessible) {
            this.accessible = accessible;
            return this;
        }
    
        @Override
        public Builder modifiers(int modifiers) {
            this.modifiers |= modifiers;
            return this;
        }
    
        @Override
        public Provider build() {
            return new ProviderImpl(recursive, accessible, modifiers);
        }
    }
    
    private static final ProviderImpl INSTANCE = new ProviderImpl(false, false, 0x00000000);
    
    public static ProviderImpl getInstance() {
        return INSTANCE;
    }
    
    public ProviderImpl(boolean recursive, boolean accessible, int modifiers) {
        this.recursive = recursive;
        this.accessible = accessible;
        this.modifiers = modifiers;
    }
    
    @Override
    public Set<Method> provide(Object object) {
        Preconditions.objectNonNull(object, "Object");
        
        final Class<?> objectClass = object.getClass();
        if (accessible && recursive) {
            return cn.codethink.common.util.Collections.unmodifiableSet(objectClass.getMethods());
        }
    
        final Set<Method> methods = new HashSet<>();
        final Set<Class<?>> classes = recursive ? getParentClasses(objectClass) : Collections.singleton(objectClass);
    
        for (Class<?> currentClass : classes) {
            methods.addAll(
                Arrays.stream(currentClass.getDeclaredMethods())
                    .filter(method -> !accessible || method.isAccessible())
                    .filter(method -> (method.getModifiers() & modifiers) == modifiers)
                    .collect(Collectors.toList())
            );
        }
        
        return Collections.unmodifiableSet(methods);
    }
    
    private static Set<Class<?>> getParentClasses(Class<?> currentClass) {
        if (currentClass == Object.class) {
            return Collections.singleton(currentClass);
        }
        
        final Set<Class<?>> classes = new HashSet<>();
        final Stack<Class<?>> stack = new Stack<>();
        stack.add(currentClass);
        
        while (!stack.isEmpty()) {
            final Class<?> objectClass = stack.pop();
            if (!classes.add(objectClass)) {
                continue;
            }
            
            final Class<?> superclass = objectClass.getSuperclass();
            if (superclass != null) {
                stack.add(superclass);
            }
    
            final Class<?>[] interfaces = objectClass.getInterfaces();
            stack.addAll(Arrays.asList(interfaces));
        }
        
        return classes;
    }
}
