package cn.codethink.xiaoming.platform.exception;

import cn.codethink.xiaoming.platform.Platform;

/**
 * 缺少必要的 Bot 异常
 *
 * @author Chuanwise
 */
public class BotNotFoundException
    extends PlatformRuntimeException {
    
    public BotNotFoundException(Platform platform) {
        super(platform);
    }
}
