package cn.codethink.xiaoming.platform.common;

/**
 * <h1>可抛出异常的 Consumer</h1>
 *
 * @param <T> 接受对象
 */
@FunctionalInterface
public interface ExceptionConsumer<T> {
    
    /**
     * 处理对象
     *
     * @param t 对象
     * @throws Exception 处理过程中的异常
     */
    void consume(T t) throws Exception;
}