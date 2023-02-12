package cn.codethink.xiaoming.platform.common;

/**
 * <h1>可抛出异常的 BiConsumer</h1>
 *
 * @param <T> 接受对象 1
 * @param <U> 接受对象 2
 */
@FunctionalInterface
public interface ExceptionBiConsumer<T, U> {
    
    /**
     * 处理对象
     *
     * @param t 对象 1
     * @param u 对象 2
     * @throws Exception 处理过程中的异常
     */
    void consume(T t, U u) throws Exception;
}