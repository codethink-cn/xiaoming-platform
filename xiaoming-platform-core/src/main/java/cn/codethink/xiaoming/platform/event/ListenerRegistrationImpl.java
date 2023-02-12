package cn.codethink.xiaoming.platform.event;

import cn.codethink.common.util.Preconditions;
import cn.codethink.xiaoming.platform.Platform;
import cn.codethink.xiaoming.platform.registration.AbstractSubjectObject;

public class ListenerRegistrationImpl
    extends AbstractSubjectObject
    implements ListenerRegistration {
    
    private final Listener listener;
    private Priority priority;
    
    public ListenerRegistrationImpl(Platform platform, Listener listener, Priority priority) {
        super(platform);
    
        Preconditions.objectNonNull(listener, "Listener");
        Preconditions.objectNonNull(priority, "Priority");
    
        this.listener = listener;
        this.priority = priority;
    }
    
    @Override
    public Priority getPriority() {
        return priority;
    }
    
    @Override
    public Listener getListener() {
        return listener;
    }
    
    public void setPriority(Priority priority) {
        Preconditions.objectNonNull(priority, "Priority");
        
        this.priority = priority;
    }
}
