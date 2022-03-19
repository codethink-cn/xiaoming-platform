package cn.codethink.xiaoming.platform.event;

import cn.codethink.common.api.ExceptionConsumer;
import cn.codethink.common.util.Collections;
import cn.codethink.common.util.Maps;
import cn.codethink.common.util.Modifiers;
import cn.codethink.common.util.Preconditions;
import cn.codethink.xiaoming.Priority;
import cn.codethink.xiaoming.annotation.EventHandler;
import cn.codethink.xiaoming.event.Listener;
import cn.codethink.xiaoming.event.MethodListener;
import cn.codethink.xiaoming.event.SimpleListener;
import cn.codethink.xiaoming.logger.Logger;
import cn.codethink.xiaoming.logger.LoggerFactory;
import cn.codethink.xiaoming.platform.AbstractPlatformObject;
import cn.codethink.xiaoming.platform.Platform;
import cn.codethink.xiaoming.platform.concurrent.PlatformFuture;
import cn.codethink.xiaoming.platform.plugin.Plugin;
import lombok.Getter;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @see cn.codethink.xiaoming.platform.event.EventManager
 * @author Chuanwise
 */
@Getter
public class SimpleEventManager
    extends AbstractEventManager {
    
    private final Logger logger;
    
    private final Map<Priority, List<PluginListener>> listeners = new ConcurrentHashMap<>();
    
    public SimpleEventManager(Platform platform) {
        super(platform);
    
        logger = platform.getPlatformConfiguration().getLoggerFactory().getLogger("event manager");
    }
    
    @Override
    public Map<Priority, List<PluginListener>> getListeners() {
        return java.util.Collections.unmodifiableMap(listeners);
    }
    
    private boolean handleEvent(Object event, Priority priority) {
        final List<PluginListener> pluginListeners = listeners.get(priority);
    
        boolean handled = false;
        if (Collections.nonEmpty(pluginListeners)) {
            for (PluginListener pluginListener : pluginListeners) {
                if (pluginListener.handleEvent(event)) {
                    handled = true;
                }
            }
        }
        
        return handled;
    }
    
    @Override
    public PlatformFuture<Boolean> handleEvent(Object event) {
        Preconditions.namedArgumentNonNull(event, "event");
    
        return platform.getScheduler().submit(() -> {
            final boolean highest = handleEvent(event, Priority.HIGHEST);
            final boolean high = handleEvent(event, Priority.HIGH);
            final boolean normal = handleEvent(event, Priority.NORMAL);
            final boolean low = handleEvent(event, Priority.LOW);
            final boolean lowest = handleEvent(event, Priority.LOWEST);
        
            return highest || high || normal || low || lowest;
        }, null);
    }
    
    @Override
    public <T> void registerListener(Class<T> eventClass, ExceptionConsumer<T> action, Priority priority, boolean alwaysValid, Plugin plugin) {
        Preconditions.namedArgumentNonNull(eventClass, "event class");
        Preconditions.namedArgumentNonNull(action, "action");
        Preconditions.namedArgumentNonNull(priority, "priority");
    
        final List<PluginListener> samePriorityListeners = Maps.getOrPutGet(listeners, priority, CopyOnWriteArrayList::new);
        final SimpleListener<T> listener = new SimpleListener<>(eventClass, priority, alwaysValid, action);
        samePriorityListeners.add(new PluginListener(platform, plugin, listener));
    }
    
    @Override
    public void registerListeners(Object source, Plugin plugin) {
        Preconditions.namedArgumentNonNull(source, "listeners");
    
        final Method[] declaredMethods = source.getClass().getDeclaredMethods();
    
        for (Method method : declaredMethods) {
            final EventHandler annotation = method.getAnnotation(EventHandler.class);
            if (Objects.isNull(annotation)) {
                continue;
            }
    
            final MethodListener listener;
            if (Modifiers.isStatic(method)) {
                listener = MethodListener.ofStaticMethod(method, annotation.priority(), annotation.alwaysValid());
            } else {
                listener = MethodListener.ofMethod(source, method, annotation.priority(), annotation.alwaysValid());
            }
    
            final List<PluginListener> samePriorityListeners = Maps.getOrPutGet(listeners, annotation.priority(), CopyOnWriteArrayList::new);
            samePriorityListeners.add(new PluginListener(platform, plugin, listener));
        }
    }
    
    @Override
    public List<Listener> unregisterListeners(Plugin plugin) {
        final List<Listener> list = new ArrayList<>();
        
        listeners.values().removeIf(samePriorityListeners -> {
            samePriorityListeners.removeIf(listener -> {
                final boolean shouldRemoved = listener.getPlugin() == plugin;
                if (shouldRemoved) {
                    list.add(listener);
                }
                return shouldRemoved;
            });
    
            return samePriorityListeners.isEmpty();
        });
        
        return java.util.Collections.unmodifiableList(list);
    }
}
