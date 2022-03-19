package cn.codethink.xiaoming.platform.debug;

import cn.codethink.xiaoming.platform.PlatformObject;

import java.io.File;

/**
 * 插件调试器
 *
 * @author Chuanwise
 */
public interface PluginDebug
    extends PlatformObject {
    
    /**
     * 设置工作目录
     *
     * @param workingDirectory 工作目录
     * @return 插件调试器
     */
    PluginDebug workingDirectory(File workingDirectory);
    
    /**
     * 启动调试
     *
     * @return 插件调试器
     */
    PluginDebug start();
    
    /**
     * 停止调试
     *
     * @return 插件调试器
     */
    void stop();
}
