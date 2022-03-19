package cn.codethink.xiaoming.platform.event;

import cn.codethink.common.util.Preconditions;
import cn.codethink.xiaoming.Priority;
import cn.codethink.xiaoming.event.Listener;
import cn.codethink.xiaoming.logger.Logger;
import cn.codethink.xiaoming.platform.Platform;
import cn.codethink.xiaoming.platform.plugin.AbstractPluginObject;
import cn.codethink.xiaoming.platform.plugin.Plugin;
import cn.codethink.xiaoming.platform.util.Plugins;

/**
 * 插件监听器
 *
 * @author Chuanwise
 */
public class PluginListener
    extends AbstractPluginObject
    implements Listener {
    
    private final Listener listener;
    
    public PluginListener(Platform platform, Plugin plugin, Listener listener) {
        super(platform, plugin);
    
        Preconditions.namedArgumentNonNull(listener, "listener");
        
        this.listener = listener;
    }
    
    @Override
    public boolean handleEvent(Object o) {
        try {
            return listener.handleEvent(o);
        } catch (Throwable throwable) {
            final Logger logger = platform.getEventManager().getLogger();
            logger.error("「" + Plugins.getPluginName(plugin) + "」注册的针对事件「" + getEventClass().getName() + "」的监听器出现异常", throwable);
            return false;
        }
    }
    
    @Override
    public Class<?> getEventClass() {
        return listener.getEventClass();
    }
    
    @Override
    public Priority getPriority() {
        return listener.getPriority();
    }
    
    @Override
    public boolean isAlwaysValid() {
        return listener.isAlwaysValid();
    }
}
