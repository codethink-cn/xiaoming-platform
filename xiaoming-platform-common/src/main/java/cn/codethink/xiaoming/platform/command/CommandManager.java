package cn.codethink.xiaoming.platform.command;

import cn.chuanwise.commandlib.CommandLib;
import cn.codethink.xiaoming.platform.PlatformObject;
import cn.codethink.xiaoming.platform.plugin.Plugin;

/**
 * 指令调度器
 *
 * @author Chuanwise
 */
public interface CommandManager
        extends PlatformObject {
    
    /**
     * 获取指令库
     *
     * @return 指令库
     */
    CommandLib getCommandLib();
    
    /**
     * 取消插件注册的所有相关组件
     *
     * @param plugin 插件
     */
    void unregisterPlugin(Plugin plugin);
}
