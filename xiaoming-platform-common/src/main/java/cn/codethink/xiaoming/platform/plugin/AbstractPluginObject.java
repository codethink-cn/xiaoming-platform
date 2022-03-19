package cn.codethink.xiaoming.platform.plugin;

import cn.codethink.xiaoming.platform.AbstractPlatformObject;
import cn.codethink.xiaoming.platform.Platform;
import cn.codethink.xiaoming.platform.PlatformObject;
import lombok.Data;

/**
 * @see cn.codethink.xiaoming.platform.PlatformObject
 * @author Chuanwise
 */
@Data
@SuppressWarnings("all")
public class AbstractPluginObject
        extends AbstractPlatformObject
        implements PlatformObject {
    
    protected final Plugin plugin;
    
    public AbstractPluginObject(Platform platform, Plugin plugin) {
        super(platform);
        
        this.plugin = plugin;
    }
}
