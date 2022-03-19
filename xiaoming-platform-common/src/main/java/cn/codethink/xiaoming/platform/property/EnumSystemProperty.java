package cn.codethink.xiaoming.platform.property;

import cn.codethink.common.util.Arrays;
import cn.codethink.common.util.Preconditions;
import lombok.Data;

import java.util.NoSuchElementException;
import java.util.Objects;

/**
 * @see cn.codethink.xiaoming.platform.property.SystemProperty
 * @author Chuanwise
 * @param <T> 枚举类型
 */
@Data
@SuppressWarnings("all")
public class EnumSystemProperty<T extends Enum<T>>
    extends SystemProperty<T> {
    
    private final T[] values;
    
    public EnumSystemProperty(T[] values, String key) {
        super(key);
    
        Preconditions.namedArgumentNonNull(values, "value");
        
        this.values = values;
    }
    
    @Override
    public T get(T defaultValue) {
        final String property = System.getProperty(key);
        if (Objects.isNull(property)) {
            return defaultValue;
        }
    
        final int index = Arrays.indexIf(values, x -> x.toString().equalsIgnoreCase(property));
        if (index == -1) {
            throw new NoSuchElementException("no such property value: " + property + ", for property: " + key + ", available: " + java.util.Arrays.asList(values));
        }
    
        return values[index];
    }
}
