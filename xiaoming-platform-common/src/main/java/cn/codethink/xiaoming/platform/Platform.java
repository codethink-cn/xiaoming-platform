package cn.codethink.xiaoming.platform;

import cn.codethink.xiaoming.VersionBranch;
import cn.codethink.xiaoming.logger.Logger;
import cn.codethink.xiaoming.platform.account.AccountManager;
import cn.codethink.xiaoming.platform.command.CommandManager;
import cn.codethink.xiaoming.platform.concurrent.Scheduler;
import cn.codethink.xiaoming.platform.configuration.PlatformConfiguration;
import cn.codethink.xiaoming.platform.console.ConsoleService;
import cn.codethink.xiaoming.platform.event.EventManager;
import cn.codethink.xiaoming.platform.file.FileService;
import cn.codethink.xiaoming.platform.permission.PermissionService;
import cn.codethink.xiaoming.platform.plugin.GlobalClassLoader;
import cn.codethink.xiaoming.platform.plugin.PluginManager;

/**
 * 小明平台
 *
 * @author Chuanwise
 */
public interface Platform {
    
    /**
     * 平台版本
     */
    String VERSION = "5.0";
    
    /**
     * 版本分支
     */
    VersionBranch VERSION_BRANCH = VersionBranch.SNAPSHOT;
    
    /**
     * 开发团队 ID
     */
    String GROUP = "CodeThink";
    
    /**
     * 获取平台状态
     *
     * @return 平台状态
     */
    PlatformState getState();
    
    /**
     * 获取序列化器
     *
     * @return 序列化器
     */
    FileService getFileService();
    
    /**
     * 获取控制台服务
     *
     * @return 控制台服务
     */
    ConsoleService getConsoleService();
    
    /**
     * 设置控制台服务
     *
     * @param consoleService 控制台服务
     */
    void setConsoleService(ConsoleService consoleService);
    
    /**
     * 获取指令调度器
     *
     * @return 指令调度器
     */
    CommandManager getCommandManager();
    
    /**
     * 获取插件管理器
     *
     * @return 插件管理器
     */
    PluginManager getPluginManager();
    
    /**
     * 获取全局类加载器
     *
     * @return 全局类加载器
     */
    GlobalClassLoader getGlobalClassLoader();
    
    /**
     * 获取事件管理器
     *
     * @return 事件管理器
     */
    EventManager getEventManager();
    
    /**
     * 获取调度器
     *
     * @return 调度器
     */
    Scheduler getScheduler();
    
    /**
     * 获取平台设置
     *
     * @return 平台设置
     */
    PlatformConfiguration getPlatformConfiguration();
    
    /**
     * 修改平台设置
     *
     * @param platformConfiguration 平台设置
     */
    void setPlatformConfiguration(PlatformConfiguration platformConfiguration);
    
    /**
     * 获取权限服务
     *
     * @return 权限服务
     */
    PermissionService getPermissionService();
    
    /**
     * 获取账号管理器
     *
     * @return 账号管理器
     */
    AccountManager getAccountManager();
    
    /**
     * 启动平台
     *
     * @return 是否启动平台
     */
    boolean start();
    
    /**
     * 关闭平台
     *
     * @return 是否关闭平台
     */
    boolean stop();
    
    /**
     * 判断平台是否启动
     *
     * @return 平台是否启动
     */
    boolean isStarted();
    
    /**
     * 判断平台是否关闭
     *
     * @return 平台是否关闭
     */
    boolean isStopped();
    
    /**
     * 获取日志记录器
     *
     * @return 日志记录器
     */
    Logger getLogger();
}
