package cn.codethink.xiaoming.platform.event.listener;

import cn.codethink.common.util.Arrays;
import cn.codethink.common.util.Preconditions;
import cn.codethink.xiaoming.platform.Platform;
import cn.codethink.xiaoming.platform.common.ExceptionBiConsumer;
import cn.codethink.xiaoming.platform.common.ExceptionConsumer;
import cn.codethink.xiaoming.platform.common.ExceptionRunnable;
import cn.codethink.xiaoming.platform.event.EventContext;
import cn.codethink.xiaoming.platform.event.Priority;
import cn.codethink.xiaoming.platform.event.listener.Listener;
import cn.codethink.xiaoming.platform.registration.AbstractSubjectObject;

import java.util.Collections;
import java.util.Set;

public class ListenerImpl
    extends AbstractSubjectObject
    implements Listener {
    
    public static class BuilderImpl<T>
        implements Builder<T> {
        
        private Priority priority = Priority.NORMAL;
        private Set<Class<?>> eventClasses = Collections.singleton(Object.class);
    
        @Override
        public Builder<T> priority(Priority priority) {
            Preconditions.objectNonNull(priority, "Priority");
            
            this.priority = priority;
            
            return this;
        }
    
        @Override
        public Builder<T> eventClasses(Class<?>[] eventClasses) {
            Preconditions.objectNonEmpty(eventClasses, "Event classes");

            this.eventClasses = Arrays.unmodifiableSet(eventClasses);
            
            return this;
        }
    
        @Override
        public <U> Builder<U> eventClass(Class<U> eventClass) {
            return null;
        }
    
        @Override
        public Builder<T> action(ExceptionRunnable action) {
            return null;
        }
    
        @Override
        public Builder<T> action(ExceptionConsumer<T> action) {
            return null;
        }
    
        @Override
        public Builder<T> action(ExceptionBiConsumer<T, EventContext> action) {
            return null;
        }
    
        @Override
        public Builder<T> signature(String signature) {
            return null;
        }
    
        @Override
        public Listener build() {
            return null;
        }
    }
    
    private final Set<Class<?>> eventClasses;
    private Priority priority;
    private String signature;
    
    public ListenerImpl(Platform platform, String signature, Set<Class<?>> eventClasses, Priority priority) {
        super(platform);
    
        Preconditions.objectNonNull(priority, "Priority");
        Preconditions.objectNonEmpty(eventClasses, "Event classes");
        Preconditions.objectNonEmpty(signature, "Event classes");
    
        this.eventClasses = eventClasses;
        this.priority = priority;
        this.signature = signature;
    }
    
    @Override
    public Priority getPriority() {
        return priority;
    }
    
    @Override
    public Set<Class<?>> getEventClasses() {
        return eventClasses;
    }
    
    @Override
    public void listen(EventContext context) throws Exception {
        Preconditions.objectNonNull(context, "Event context");
        
        // TODO
    }
    
    public void setPriority(Priority priority) {
        Preconditions.objectNonNull(priority, "Priority");
        
        this.priority = priority;
    }
    
    @Override
    public String getName() {
        return signature;
    }
}
