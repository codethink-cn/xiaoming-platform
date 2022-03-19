package cn.codethink.xiaoming.platform.permission;

import cn.codethink.common.util.Preconditions;
import cn.codethink.xiaoming.code.Code;
import cn.codethink.xiaoming.platform.AbstractPlatformObject;
import cn.codethink.xiaoming.platform.Platform;
import cn.codethink.xiaoming.platform.account.Account;

import java.util.Objects;

/**
 * 基础权限控制器。
 *
 * 当用户不存在，则不知道是否有权限。
 * 当用户被封禁，则无权限。
 * 当用户是管理，则有权限。
 * 其他情况下则不知道。
 *
 * @author Chuanwise
 */
public class BasePermissionHandler
    extends AbstractPlatformObject
    implements PermissionHandler {
    
    public BasePermissionHandler(Platform platform) {
        super(platform);
    }
    
    @Override
    public PermissionAccessible accessible(Code code, Permission permission) {
        Preconditions.namedArgumentNonNull(code, "code");
        Preconditions.namedArgumentNonNull(permission, "permission");
    
        final Account account = platform.getAccountManager().getAccount(code);
        if (Objects.isNull(account)) {
            return PermissionAccessible.UNKNOWN;
        } else if (account.isBanned()) {
            return PermissionAccessible.INACCESSIBLE;
        } else if (account.isOperator()) {
            return PermissionAccessible.ACCESSIBLE;
        } else {
            return PermissionAccessible.UNKNOWN;
        }
    }
}
