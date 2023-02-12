package cn.codethink.xiaoming.platform.event;

import cn.codethink.xiaoming.platform.Platform;
import cn.codethink.xiaoming.platform.object.AbstractPlatformObject;
import cn.codethink.xiaoming.platform.registration.AbstractSubjectObject;

public class ListenerRegistrationImpl
    extends AbstractSubjectObject
    implements ListenerRegistration {
    
    private int priority;
    
    public ListenerRegistrationImpl(Platform platform) {
        super(platform);
    }
    
    @Override
    public int getPriority() {
        return 0;
    }
    
    @Override
    public Listener<?> getListener() {
        return null;
    }
}
