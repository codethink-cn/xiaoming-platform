package cn.codethink.xiaoming.platform.plugin;

import cn.codethink.xiaoming.VersionBranch;

import java.beans.Transient;
import java.util.UUID;

/**
 * 插件元数据
 *
 * @author Chuanwise
 */
public interface PluginMetadata {
    
    /**
     * 获取插件全名
     *
     * @return 插件全名
     */
    String getCompleteName();
    
    /**
     * 获取插件名
     *
     * @return 插件名
     */
    String getName();
    
    /**
     * 获取包名
     *
     * @return 包名
     */
    String getGroup();
    
    /**
     * 获取版本号
     *
     * @return 版本号
     */
    String getVersion();
    
    /**
     * 获取版本分支
     *
     * @return 版本分支
     */
    VersionBranch getVersionBranch();
    
    /**
     * 获取插件唯一标识符
     *
     * @return 插件唯一标识符
     */
    @Transient
    UUID getUuid();
    
    /**
     * 获取前置插件名
     *
     * @return 前置插件名
     */
    String[] getDepends();
    
    /**
     * 获取软前置插件名
     *
     * @return 软前置插件名
     */
    String[] getSoftDepends();
    
    /**
     * 检查插件是否是该插件的依赖
     *
     * @param pluginName 插件名
     * @return 插件是否是该插件的依赖
     */
    boolean isDependPlugin(String pluginName);
    
    /**
     * 检查插件是否是该插件的软依赖
     *
     * @param pluginName 插件名
     * @return 插件是否是该插件的软依赖
     */
    boolean isSoftDependPlugin(String pluginName);
}
