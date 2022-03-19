package cn.codethink.xiaoming.platform.permission;

import cn.codethink.common.util.Preconditions;
import cn.codethink.xiaoming.code.Code;
import cn.codethink.xiaoming.platform.Platform;
import cn.codethink.xiaoming.platform.user.User;

/**
 * @see cn.codethink.xiaoming.platform.permission.PermissionService
 * @author Chuanwise
 */
public class SimplePermissionService
    extends AbstractPermissionService {
    
    public SimplePermissionService(Platform platform) {
        super(platform);
    }
    
    @Override
    public PermissionAccessible accessible(Code code, Permission permission) {
        Preconditions.namedArgumentNonNull(code, "code");
        Preconditions.namedArgumentNonNull(permission, "permission");
    
        for (PermissionHandlerEntry entry : entries) {
            final PermissionAccessible accessible = entry.getPermissionHandler().accessible(code, permission);
            if (accessible != PermissionAccessible.UNKNOWN) {
                return accessible;
            }
        }
        
        return PermissionAccessible.UNKNOWN;
    }
    
    @Override
    public PermissionAccessible accessible(User user, Permission permission) {
        Preconditions.namedArgumentNonNull(user, "user");
        Preconditions.namedArgumentNonNull(permission, "permission");
    
        for (PermissionHandlerEntry entry : entries) {
            final PermissionAccessible accessible = entry.getPermissionHandler().accessible(user, permission);
            if (accessible != PermissionAccessible.UNKNOWN) {
                return accessible;
            }
        }
    
        return PermissionAccessible.UNKNOWN;
    }
}
