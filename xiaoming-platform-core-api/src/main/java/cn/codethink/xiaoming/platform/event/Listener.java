package cn.codethink.xiaoming.platform.event;

import cn.codethink.xiaoming.platform.api.API;
import cn.codethink.xiaoming.platform.api.APIFactory;
import cn.codethink.xiaoming.platform.common.ExceptionBiConsumer;
import cn.codethink.xiaoming.platform.common.ExceptionConsumer;
import cn.codethink.xiaoming.platform.common.ExceptionRunnable;
import cn.codethink.xiaoming.platform.registration.Registration;

import java.util.Set;

/**
 * <h1>监听器</h1>
 *
 * <p>在事件发生时执行的一段代码</p>
 *
 * @author Chuanwise
 */
public interface Listener
    extends Registration {
    
    interface Builder<T> {
        
        Builder<T> priority(Priority priority);
        
        Builder<T> eventClasses(Class<?>[] eventClasses);
        
        <U> Builder<U> eventClass(Class<U> eventClass);
        
        Builder<T> action(ExceptionRunnable action);
        
        Builder<T> action(ExceptionConsumer<T> action);
        
        Builder<T> action(ExceptionBiConsumer<T, EventContext> action);
        
        Builder<T> signature(String signature);
        
        Listener build();
    }
    
    static Builder<?> builder() {
        return APIFactory.getInstance().getListenerBuilder();
    }
    
    /**
     * 获取监听器优先级
     *
     * @return 监听器优先级
     */
    Priority getPriority();
    
    /**
     * 获取监听的类
     *
     * @return 监听的类
     */
    Set<Class<?>> getEventClasses();
    
    /**
     * 监听事件
     *
     * @param context 事件环境
     * @throws Exception 监听时抛出的异常
     */
    void listen(EventContext context) throws Exception;
}