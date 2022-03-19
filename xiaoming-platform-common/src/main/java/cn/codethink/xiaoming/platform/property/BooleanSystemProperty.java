package cn.codethink.xiaoming.platform.property;

import java.util.Objects;

/**
 * @see cn.codethink.xiaoming.platform.property.SystemProperty
 * @author Chuanwise
 */
public class BooleanSystemProperty
    extends SystemProperty<Boolean> {
    
    public BooleanSystemProperty(String key) {
        super(key);
    }
    
    @Override
    public Boolean get(Boolean defaultValue) {
        final String property = System.getProperty(key);
        if (Objects.isNull(property)) {
            return defaultValue;
        } else {
            return "true".equalsIgnoreCase(property);
        }
    }
}
