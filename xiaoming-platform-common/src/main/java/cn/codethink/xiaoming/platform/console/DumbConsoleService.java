package cn.codethink.xiaoming.platform.console;

import cn.codethink.common.util.Preconditions;
import cn.codethink.xiaoming.platform.Platform;

/**
 * 没有相关功能的控制台服务
 *
 * @author Chuanwise
 */
public class DumbConsoleService
    extends AbstractConsoleService {
    
    public DumbConsoleService(Platform platform) {
        Preconditions.namedArgumentNonNull(platform, "platform");
        
        this.platform = platform;
    }
}
