package cn.codethink.xiaoming.platform.api;

import cn.codethink.xiaoming.platform.event.listener.Listener;
import cn.codethink.xiaoming.platform.event.listener.Listeners;
import cn.codethink.xiaoming.platform.registration.provider.Provider;
import cn.codethink.xiaoming.platform.registration.rule.BeforeAndAfterRule;
import cn.codethink.xiaoming.platform.task.action.Action;

/**
 * <h1>小明平台 API</h1>
 *
 * <p>小明平台 API 是 xiaoming-platform-api 调用 xiaoming-platform-core 的桥梁。</p>
 *
 * @author Chuanwise
 */
public interface API {
    
    Listener.Builder<?> getListenerBuilder();
    
    Listeners.Builder getListenersBuilder();
    
    Action getEmptyAction();
    
    BeforeAndAfterRule getBeforeAndAfterRule(String beforeOneName, String afterOneName);
    
    Provider getDefaultProvider();
    
    Provider.Builder getProviderBuilder();
}
