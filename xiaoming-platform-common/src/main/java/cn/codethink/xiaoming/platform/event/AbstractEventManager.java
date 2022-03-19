package cn.codethink.xiaoming.platform.event;

import cn.codethink.common.api.ExceptionConsumer;
import cn.codethink.common.util.Preconditions;
import cn.codethink.xiaoming.Priority;
import cn.codethink.xiaoming.platform.AbstractPlatformObject;
import cn.codethink.xiaoming.platform.Platform;
import cn.codethink.xiaoming.platform.plugin.Plugin;

import java.lang.reflect.Method;

/**
 * @see cn.codethink.xiaoming.platform.event.EventManager
 * @author Chuanwise
 */
public abstract class AbstractEventManager
    extends AbstractPlatformObject
    implements EventManager {
    
    public AbstractEventManager(Platform platform) {
        super(platform);
    }
    
    @Override
    public <T> void registerListener(Class<T> eventClass, ExceptionConsumer<T> action, Priority priority, Plugin plugin) {
        registerListener(eventClass, action, priority, false, plugin);
    }
    
    @Override
    public <T> void registerListener(Class<T> eventClass, ExceptionConsumer<T> action, boolean alwaysValid, Plugin plugin) {
        registerListener(eventClass, action, Priority.NORMAL, alwaysValid, plugin);
    }
    
    @Override
    public <T> void registerListener(Class<T> eventClass, ExceptionConsumer<T> action, Plugin plugin) {
        registerListener(eventClass, action, Priority.NORMAL, false, plugin);
    }
}
