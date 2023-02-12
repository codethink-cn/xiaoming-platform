package cn.codethink.xiaoming.platform.event;

import cn.codethink.xiaoming.platform.Platform;
import cn.codethink.xiaoming.platform.object.AbstractPlatformObject;

import java.util.Map;

public class EventManagerImpl
    extends AbstractPlatformObject
    implements EventManager {
    
    public EventManagerImpl(Platform platform) {
        super(platform);
    }
}
