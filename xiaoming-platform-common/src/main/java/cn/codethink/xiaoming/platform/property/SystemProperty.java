package cn.codethink.xiaoming.platform.property;

import cn.codethink.common.util.Preconditions;
import lombok.Data;

import java.util.Objects;
import java.util.function.Supplier;

/**
 * 表示某种系统属性
 *
 * @author Chuanwise
 * @param <T> 属性类型
 */
@Data
public abstract class SystemProperty<T> {
    
    /**
     * 属性名
     */
    protected final String key;
    
    /**
     * 获取属性值
     *
     * @param defaultValue 默认属性值
     * @return 属性值
     */
    public abstract T get(T defaultValue);
    
    /**
     * 获取属性值，或构造一个默认属性
     *
     * @param supplier 默认属性值构造器
     * @return 属性值
     */
    public T getOrElseGet(Supplier<T> supplier) {
        Preconditions.namedArgumentNonNull(supplier, "suppiler");
    
        final String property = System.getProperty(key);
        if (Objects.isNull(property)) {
            return supplier.get();
        } else {
            return get(null);
        }
    }
    
    /**
     * 获取属性值
     *
     * @return 属性值或 null
     */
    public T get() {
        return get(null);
    }
    
    /**
     * 判断是否存在该属性
     *
     * @return 是否存在该属性
     */
    public boolean isExists() {
        return Objects.nonNull(System.getProperty(key));
    }
}
