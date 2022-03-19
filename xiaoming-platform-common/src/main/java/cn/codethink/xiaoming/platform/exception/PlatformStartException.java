package cn.codethink.xiaoming.platform.exception;

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
public class PlatformStartException
    extends PlatformRuntimeException
    implements PlatformObject {
    
    private Platform platform;
    
    public PlatformStartException(Platform platform) {
        super(platform);
        
        this.platform = platform;
    }
    
    public PlatformStartException(Platform platform, Throwable cause) {
        super(platform, cause);
        
        this.platform = platform;
    }
    
    public PlatformStartException(Platform platform, String message) {
        super(platform, message);
        
        this.platform = platform;
    }
    
    public PlatformStartException(Platform platform, String message, Throwable cause) {
        super(platform, message, cause);
        
        this.platform = platform;
    }
}
