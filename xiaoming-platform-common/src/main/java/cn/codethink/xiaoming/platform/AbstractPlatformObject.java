package cn.codethink.xiaoming.platform;

import cn.codethink.common.util.Preconditions;
import lombok.Data;

/**
 * @see PlatformObject
 * @author Chuanwise
 */
@Data
public class AbstractPlatformObject
        implements PlatformObject {
    
    protected final Platform platform;
    
    public AbstractPlatformObject(Platform platform) {
        Preconditions.namedArgumentNonNull(platform, "platform");
        
        this.platform = platform;
    }
}
