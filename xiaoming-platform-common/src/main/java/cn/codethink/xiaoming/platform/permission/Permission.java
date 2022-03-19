package cn.codethink.xiaoming.platform.permission;

import cn.chuanwise.common.util.Maps;
import cn.codethink.common.util.Preconditions;
import lombok.Data;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 权限
 *
 * @author Chuanwise
 */
@Data
public class Permission {
    
    /**
     * 权限节点
     */
    protected final String string;
    
    /**
     * 缓存的权限节点
     */
    private static final Map<String, Permission> INSTANCES = new ConcurrentHashMap<>();

    private Permission(String string) {
        Preconditions.namedArgumentNonEmpty(string, "string");
        
        this.string = string;
    }
    
    public static Permission valueOf(String string) {
        return Maps.getOrPutSupply(INSTANCES, string, () -> new Permission(string));
    }
    
    @SuppressWarnings("all")
    public PermissionAccessible accessible(Permission permission) {
        Preconditions.namedArgumentNonNull(permission, "permission");
    
        final String thatString = permission.string;
        if (thatString.startsWith(string)) {
            return PermissionAccessible.ACCESSIBLE;
        } else if (thatString.startsWith("-" + string)) {
            return PermissionAccessible.INACCESSIBLE;
        } else {
            return PermissionAccessible.UNKNOWN;
        }
    }
}
