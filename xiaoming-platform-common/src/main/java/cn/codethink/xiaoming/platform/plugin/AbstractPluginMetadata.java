package cn.codethink.xiaoming.platform.plugin;

import cn.codethink.common.util.Arrays;
import cn.codethink.common.util.Preconditions;
import cn.codethink.xiaoming.VersionBranch;
import cn.codethink.xiaoming.platform.util.Plugins;
import lombok.Data;

import java.beans.Transient;
import java.util.Objects;
import java.util.UUID;

/**
 * @see PluginMetadata
 * @author Chuanwise
 */
@Data
public class AbstractPluginMetadata
        implements PluginMetadata {
    
    protected String group = "unknown";

    protected String name = "unknown";
    
    protected String version = "unknown";
    
    protected VersionBranch versionBranch = VersionBranch.SNAPSHOT;
    
    protected transient UUID uuid;
    
    protected String[] depends = new String[0];
    
    protected String[] softDepends = new String[0];
    
    @Override
    public String getCompleteName() {
        return group + ':' + name + ':' + version + '-' + versionBranch + ':' + getUuid();
    }
    
    @Override
    public boolean isDependPlugin(String pluginName) {
        Preconditions.namedArgumentNonEmpty(pluginName, "plugin name");
        return Arrays.contains(depends, pluginName);
    }
    
    @Override
    public boolean isSoftDependPlugin(String pluginName) {
        Preconditions.namedArgumentNonEmpty(pluginName, "plugin name");
        return Arrays.contains(softDepends, pluginName);
    }
    
    @Override
    @Transient
    public UUID getUuid() {
        if (Objects.isNull(uuid)) {
            uuid = Plugins.calculateUUID(
                group,
                name,
                version,
                versionBranch
            );
        }
        return uuid;
    }
}
