package cn.codethink.xiaoming.platform.permission;

import cn.codethink.xiaoming.platform.plugin.Plugin;

import java.util.List;

/**
 * 权限服务
 *
 * @author Chuanwise
 */
public interface PermissionService
    extends PermissionHandler {
    
    /**
     * 获取权限处理器
     *
     * @return 权限处理器
     */
    List<PermissionHandler> getPermissionHandler();
    
    /**
     * 添加权限处理器
     *
     * @param permissionHandler 权限处理器
     * @param plugin 插件
     * @return 是否添加该权限处理器
     */
    boolean addPermissionHandler(PermissionHandler permissionHandler, Plugin plugin);
    
    /**
     * 删除权限处理器
     *
     * @param permissionHandler 权限处理器
     * @return 是否删除该权限处理器
     */
    boolean removePermissionHandler(PermissionHandler permissionHandler);
    
    /**
     * 删除插件的权限处理器
     *
     * @param plugin 插件
     * @return 是否删除该权限处理器
     */
    boolean removePermissionHandlers(Plugin plugin);
}
