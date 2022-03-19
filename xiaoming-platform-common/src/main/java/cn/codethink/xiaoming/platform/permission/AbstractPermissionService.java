package cn.codethink.xiaoming.platform.permission;

import cn.codethink.common.util.Preconditions;
import cn.codethink.xiaoming.platform.AbstractPlatformObject;
import cn.codethink.xiaoming.platform.Platform;
import cn.codethink.xiaoming.platform.plugin.AbstractPluginObject;
import cn.codethink.xiaoming.platform.plugin.Plugin;
import lombok.Data;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

/**
 * @see cn.codethink.xiaoming.platform.permission.PermissionService
 * @author Chuanwise
 */
public abstract class AbstractPermissionService
    extends AbstractPlatformObject
    implements PermissionService {
    
    @Data
    protected class PermissionHandlerEntry {
        
        protected final PermissionHandler permissionHandler;
    
        protected final Plugin plugin;
    }
    
    protected final List<PermissionHandlerEntry> entries = new CopyOnWriteArrayList<>();
    
    public AbstractPermissionService(Platform platform) {
        super(platform);
    }
    
    @Override
    public List<PermissionHandler> getPermissionHandler() {
        return Collections.unmodifiableList(
            entries.stream()
                .map(PermissionHandlerEntry::getPermissionHandler)
                .collect(Collectors.toList())
        );
    }
    
    @Override
    public boolean addPermissionHandler(PermissionHandler permissionHandler, Plugin plugin) {
        Preconditions.namedArgumentNonNull(permissionHandler, "permission handler");
        
        return cn.codethink.common.util.Collections.addDistinctly(entries, new PermissionHandlerEntry(permissionHandler, plugin));
    }
    
    @Override
    public boolean removePermissionHandler(PermissionHandler permissionHandler) {
        Preconditions.namedArgumentNonNull(permissionHandler, "permission handler");

        return entries.removeIf(x -> Objects.equals(x.permissionHandler, permissionHandler));
    }
    
    @Override
    public boolean removePermissionHandlers(Plugin plugin) {
        return entries.removeIf(x -> x.plugin == plugin);
    }
}
