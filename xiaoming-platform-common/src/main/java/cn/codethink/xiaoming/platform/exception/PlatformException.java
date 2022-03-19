package cn.codethink.xiaoming.platform.exception;

import cn.codethink.common.util.Preconditions;
import cn.codethink.xiaoming.platform.Platform;
import cn.codethink.xiaoming.platform.PlatformObject;
import lombok.Data;

/**
 * 平台相关异常
 *
 * @author Chuanwise
 */
@Data
@SuppressWarnings("all")
public class PlatformException
    extends Exception
    implements PlatformObject {
    
    private Platform platform;
    
    public PlatformException(Platform platform) {
        Preconditions.namedArgumentNonNull(platform, "platform");
        
        this.platform = platform;
    }
    
    public PlatformException(Platform platform, Throwable cause) {
        super(cause);
    
        Preconditions.namedArgumentNonNull(platform, "platform");

        this.platform = platform;
    }
    
    public PlatformException(Platform platform, String message) {
        super(message);
    
        Preconditions.namedArgumentNonNull(platform, "platform");

        this.platform = platform;
    }
    
    public PlatformException(Platform platform, String message, Throwable cause) {
        super(message, cause);
    
        Preconditions.namedArgumentNonNull(platform, "platform");

        this.platform = platform;
    }
}
