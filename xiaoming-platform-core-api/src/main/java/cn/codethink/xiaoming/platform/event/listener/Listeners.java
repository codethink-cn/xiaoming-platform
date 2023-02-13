package cn.codethink.xiaoming.platform.event.listener;

import cn.codethink.xiaoming.platform.api.APIFactory;
import cn.codethink.xiaoming.platform.registration.Subject;
import cn.codethink.xiaoming.platform.registration.SubjectObject;
import cn.codethink.xiaoming.platform.registration.provider.Provider;

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
            Builder listeners(Object... listeners);
    
            /**
             * 添加监听类
             *
             * @param listener 监听类
             * @return 监听器组构建器
             */
            Builder listener(Object listener);
    
            /**
             * 添加提供器
             *
             * @param provider 提供器
             * @return 监听器组构建器
             */
            Builder includes(Provider provider);
    
            /**
             * 构建一个监听器组
             *
             * @return 监听器组
             */
            Listeners build();
        }
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
