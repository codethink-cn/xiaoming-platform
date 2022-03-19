package cn.codethink.xiaoming.platform.permission;

import cn.codethink.xiaoming.code.Code;
import cn.codethink.xiaoming.platform.account.Account;
import cn.codethink.xiaoming.platform.plugin.PluginObject;
import cn.codethink.xiaoming.platform.user.User;

/**
 * 权限控制器
 *
 * @author Chuanwise
 */
public interface PermissionHandler {
    
    /**
     * 计算账户是否具备某权限
     *
     * @param code       账户号
     * @param permission 权限
     * @return 账户是否具备某权限
     */
    PermissionAccessible accessible(Code code, Permission permission);
    
    /**
     * 计算账户是否具备某权限
     *
     * @param user       在线用户
     * @param permission 权限
     * @return 账户是否具备某权限
     */
    default PermissionAccessible accessible(User user, Permission permission) {
        return accessible(user.getCode(), permission);
    }
}
