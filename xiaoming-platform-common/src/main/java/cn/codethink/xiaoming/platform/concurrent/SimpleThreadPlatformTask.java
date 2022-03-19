package cn.codethink.xiaoming.platform.concurrent;

import cn.codethink.common.api.ExceptionRunnable;
import cn.codethink.common.util.Preconditions;
import cn.codethink.xiaoming.platform.Platform;
import cn.codethink.xiaoming.platform.plugin.Plugin;
import lombok.Data;

/**
 * 用 ExceptionRunnable 实现的平台任务
 *
 * @author Chuanwise
 */
@Data
@SuppressWarnings("all")
public class SimpleThreadPlatformTask
        extends AbstractThreadPlatformTask {
    
    private final ExceptionRunnable action;
    
    public SimpleThreadPlatformTask(Platform platform, Plugin plugin, ExceptionRunnable action) {
        super(platform, plugin);
    
        Preconditions.namedArgumentNonNull(action, "action");
        
        this.action = action;
    }
    
    @Override
    protected void run0() throws Exception {
        action.exceptRun();
    }
}
