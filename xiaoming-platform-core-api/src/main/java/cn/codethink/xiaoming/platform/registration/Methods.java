package cn.codethink.xiaoming.platform.registration;

import cn.codethink.xiaoming.platform.api.APIFactory;

import java.lang.reflect.Method;
import java.util.Set;

/**
 * <h1>方法集</h1>
 *
 * <p>方法集用于提供一些方法，规定了扫描策略。</p>
 *
 * @author Chuanwise
 */
public interface Methods {
    
    /**
     * <h1>方法集构建器</h1>
     *
     * @author Chuanwise
     */
    interface Builder {
    
        /**
         * 设置是否递归搜索所有父类
         *
         * @param recursive 是否递归搜索所有父类
         * @return 方法集构建器
         */
        Builder recursive(boolean recursive);
    
        /**
         * 设置是否只获取可访问的方法
         *
         * @param accessible 是否只获取可访问的方法
         * @return 方法集构建器
         */
        Builder accessible(boolean accessible);
    
        /**
         * 设置方法必须具备的修饰符
         *
         * @param modifiers 方法必须具备的修饰符
         * @return 方法集构建器
         */
        Builder modifiers(int modifiers);
    
        /**
         * 构建一个方法集
         *
         * @return 方法集
         */
        Methods build();
    }
    
    /**
     * 获取方法集构建器
     *
     * @return 方法集构建器
     */
    static Builder builder() {
        return APIFactory.getInstance().getProviderBuilder();
    }
    
    /**
     * 获取默认方法集
     *
     * @return 默认方法集
     */
    static Methods defaults() {
        return APIFactory.getInstance().getDefaultProvider();
    }
    
    /**
     * 提供方法
     *
     * @param object 对象
     * @return 对象中的方法
     */
    Set<Method> provide(Object object);
}
