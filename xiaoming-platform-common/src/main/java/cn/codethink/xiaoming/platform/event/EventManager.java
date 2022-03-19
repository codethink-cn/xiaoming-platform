package cn.codethink.xiaoming.platform.event;

import cn.codethink.common.api.ExceptionConsumer;
import cn.codethink.xiaoming.Priority;
import cn.codethink.xiaoming.event.Listener;
import cn.codethink.xiaoming.logger.Logger;
import cn.codethink.xiaoming.platform.concurrent.PlatformFuture;
import cn.codethink.xiaoming.platform.plugin.Plugin;

import java.util.List;
import java.util.Map;

/**
 * 事件管理器
 *
 * @author Chuanwise
 */
public interface EventManager {
    
    /**
     * 触发一个事件
     *
     * @param event 事件
     * @return 是否有事件监听器处理
     */
    PlatformFuture<Boolean> handleEvent(Object event);
    
    /**
     * 获取日志记录器
     *
     * @return 日志记录器
     */
    Logger getLogger();
    
    /**
     * 注册一个事件监听器
     *
     * @param eventClass  事件类型
     * @param action      监听动作
     * @param priority    优先级
     * @param alwaysValid 该监听器是否总是生效
     * @param plugin      注册插件
     * @param <T>         事件类型
     */
    <T>
    
    void registerListener(Class<T> eventClass, ExceptionConsumer<T> action, Priority priority, boolean alwaysValid, Plugin plugin);
    
    /**
     * 注册一个事件监听器
     *
     * @param eventClass 事件类型
     * @param action     监听动作
     * @param priority   优先级
     * @param plugin     注册插件
     * @param <T>        事件类型
     */
    <T> void registerListener(Class<T> eventClass, ExceptionConsumer<T> action, Priority priority, Plugin plugin);
    
    /**
     * 注册一个事件监听器
     *
     * @param eventClass  事件类型
     * @param action      监听动作
     * @param alwaysValid 该监听器是否总是生效
     * @param plugin      注册插件
     * @param <T>         事件类型
     */
    <T> void registerListener(Class<T> eventClass, ExceptionConsumer<T> action, boolean alwaysValid, Plugin plugin);
    
    /**
     * 注册一个事件监听器
     *
     * @param eventClass 事件类型
     * @param action     监听动作
     * @param plugin     注册插件
     * @param <T>        事件类型
     */
    <T> void registerListener(Class<T> eventClass, ExceptionConsumer<T> action, Plugin plugin);
    
    /**
     * 注册一个事件监听器
     *
     * @param source 监听类对象
     * @param plugin 注册插件
     */
    void registerListeners(Object source, Plugin plugin);
    
    /**
     * 取消注册插件的监听器
     *
     * @param plugin 插件
     * @return 取消的插件监听器
     */
    List<Listener> unregisterListeners(Plugin plugin);
    
    /**
     * 获得所有注册的监听器
     *
     * @return 所有注册的监听器
     */
    Map<Priority, List<PluginListener>> getListeners();
}