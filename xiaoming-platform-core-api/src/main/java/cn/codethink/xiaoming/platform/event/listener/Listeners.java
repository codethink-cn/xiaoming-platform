package cn.codethink.xiaoming.platform.event.listener;

import cn.codethink.xiaoming.platform.api.APIFactory;
import cn.codethink.xiaoming.platform.registration.Subject;
import cn.codethink.xiaoming.platform.registration.SubjectObject;
import cn.codethink.xiaoming.platform.registration.Methods;

import java.util.Set;

/**
 * <h1>监听器组</h1>
 *
 * @author Chuanwise
 */
public interface Listeners
    extends SubjectObject {
    
    /**
     * <h1>监听器组构建器</h1>
     *
     * @author Chuanwise
     */
    interface Builder {
    
        /**
         * 设置监听主体
         *
         * @param subject 监听主体
         * @return 监听器组构建器
         */
        Completed subject(Subject subject);
        
        interface Completed {
    
            /**
             * 添加监听类
             *
             * @param listeners 监听类
             * @return 监听器组构建器
             */
            Completed listeners(Object... listeners);
    
            /**
             * 添加监听类
             *
             * @param listener 监听类
             * @return 监听器组构建器
             */
            Completed listener(Object listener);
    
            /**
             * 添加方法集
             *
             * @param methods 方法集
             * @return 监听器组构建器
             */
            Completed methods(Methods methods);
    
            /**
             * 构建一个监听器组
             *
             * @return 监听器组
             */
            Listeners build();
        }
    }
    
    static Listeners of(Object listeners, Subject subject) {
        return builder().subject(subject).listener(listeners).build();
    }
    
    static Builder builder() {
        return APIFactory.getInstance().getListenersBuilder();
    }
    
    /**
     * 获取所有监听器
     *
     * @return 所有监听器
     */
    Set<Listener> getListeners();
}
