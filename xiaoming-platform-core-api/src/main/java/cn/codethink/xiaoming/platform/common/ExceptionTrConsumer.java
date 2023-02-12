package cn.codethink.xiaoming.platform.common;

/**
 * <h1>可抛出异常的 BiConsumer</h1>
 *
 * @param <T> 接受对象 1
 * @param <U> 接受对象 2
 * @param <R> 接受对象 3
 */
@FunctionalInterface
public interface ExceptionTrConsumer<T, U, R> {
    
    /**
     * 处理对象
     *
     * @param t 对象 1
     * @param u 对象 2
     * @param r 对象 3
     * @throws Exception 处理过程中的异常
     */
    void consume(T t, U u, R r) throws Exception;
}