package cn.codethink.xiaoming.platform.exception;

import cn.codethink.common.util.Preconditions;
import cn.codethink.xiaoming.exception.BotException;
import cn.codethink.xiaoming.exception.BotRuntimeException;
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
public class PlatformRuntimeException
    extends RuntimeException
    implements PlatformObject {
    
    private Platform platform;
    
    public PlatformRuntimeException(Platform platform) {
        Preconditions.namedArgumentNonNull(platform, "platform");
        
        this.platform = platform;
    }
    
    public PlatformRuntimeException(Platform platform, Throwable cause) {
        super(cause);
    
        Preconditions.namedArgumentNonNull(platform, "platform");
     
        this.platform = platform;
    }
    
    public PlatformRuntimeException(Platform platform, String message) {
        super(message);
    
        Preconditions.namedArgumentNonNull(platform, "platform");
    
        this.platform = platform;
    }
    
    public PlatformRuntimeException(Platform platform, String message, Throwable cause) {
        super(message, cause);
    
        Preconditions.namedArgumentNonNull(platform, "platform");

        this.platform = platform;
    }
}
