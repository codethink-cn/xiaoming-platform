package cn.codethink.xiaoming.platform.api;

import cn.codethink.xiaoming.platform.event.Listener;
import cn.codethink.xiaoming.platform.event.ListenerImpl;
import cn.codethink.xiaoming.platform.task.action.Action;

public class APIImpl
    implements API {
    
    @Override
    public Listener.Builder<?> getListenerBuilder() {
        // TODO
        return null;
    }
    
    @Override
    public Action getEmptyAction() {
        return null;
    }
}
