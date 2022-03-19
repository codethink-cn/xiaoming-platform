package cn.codethink.xiaoming.platform.event;

import cn.codethink.xiaoming.annotation.EventHandler;
import cn.codethink.xiaoming.platform.AbstractPlatformObject;
import cn.codethink.xiaoming.platform.Platform;

/**
 * 将小明内部的事件转发到平台上
 *
 * @author Chuanwise
 */
public class BotEventForwarder
    extends AbstractPlatformObject {
    
    public BotEventForwarder(Platform platform) {
        super(platform);
    }
    
    @EventHandler
    public void handleEvent(Object event) {
        platform.getEventManager().handleEvent(event);
    }
}
