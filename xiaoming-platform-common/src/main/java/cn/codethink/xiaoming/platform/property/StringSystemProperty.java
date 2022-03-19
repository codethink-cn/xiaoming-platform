package cn.codethink.xiaoming.platform.property;

/**
 * @see cn.codethink.xiaoming.platform.property.SystemProperty
 * @author Chuanwise
 */
public class StringSystemProperty
    extends SystemProperty<String> {
    
    public StringSystemProperty(String key) {
        super(key);
    }
    
    @Override
    public String get(String defaultValue) {
        return System.getProperty(key, defaultValue);
    }
}
