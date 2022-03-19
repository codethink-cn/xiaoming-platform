package cn.codethink.xiaoming.platform.property;

import cn.codethink.common.util.Preconditions;
import lombok.Data;

import java.util.Objects;
import java.util.function.Function;

/**
 * @see cn.codethink.xiaoming.platform.property.SystemProperty
 * @param <T> 属性值类型
 */
@Data
@SuppressWarnings("all")
public class SimpleSystemProperty<T>
    extends SystemProperty<T> {
    
    private final Function<String, T> translator;
    
    public SimpleSystemProperty(String key, Function<String, T> translator) {
        super(key);
    
        Preconditions.namedArgumentNonNull(translator, "translator");
        
        this.translator = translator;
    }
    
    @Override
    public T get(T defaultValue) {
        final String property = System.getProperty(key);
        if (Objects.isNull(property)) {
            return defaultValue;
        } else {
            return translator.apply(property);
        }
    }
}
