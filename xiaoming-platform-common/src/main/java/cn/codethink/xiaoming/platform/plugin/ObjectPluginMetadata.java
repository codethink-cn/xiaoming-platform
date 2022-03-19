package cn.codethink.xiaoming.platform.plugin;

import cn.codethink.common.util.Preconditions;
import cn.codethink.common.util.Strings;
import cn.codethink.xiaoming.VersionBranch;
import lombok.Data;

/**
 * 从对象中加载插件
 *
 * @author Chuanwise
 */
@Data
public class ObjectPluginMetadata
    extends AbstractPluginMetadata {
    
    protected final Plugin plugin;
    
    protected ObjectPluginMetadata(
        Plugin plugin,
        String name,
        String group,
        String version,
        VersionBranch versionBranch,
        String[] depends,
        String[] softDepends
    ) {
        Preconditions.namedArgumentNonNull(plugin, "plugin");
        Preconditions.namedArgumentNonEmpty(name, "name");
        Preconditions.namedArgumentNonEmpty(group, "group");
        Preconditions.namedArgumentNonEmpty(version, "version");
        Preconditions.namedArgumentNonNull(versionBranch, "versionBranch");
        Preconditions.namedArgumentNonNull(depends, "depends");
        Preconditions.namedArgumentNonNull(softDepends, "softDepends");
        
        this.plugin = plugin;
        
        this.name = name;
        this.group = group;
        this.version = version;
        this.versionBranch = versionBranch;
        this.depends = depends;
        this.softDepends = softDepends;
    }
    
    public static class ObjectPluginMetadataBuilder {
        
        protected Plugin plugin;
    
        protected String name;
    
        protected String group;
    
        protected String version;
    
        protected VersionBranch versionBranch = VersionBranch.SNAPSHOT;
    
        protected String[] depends = new String[0];
    
        protected String[] softDepends = new String[0];
    
        public ObjectPluginMetadataBuilder(Plugin plugin) {
            Preconditions.namedArgumentNonNull(plugin, "plugin");
            
            this.plugin = plugin;
        }
        
        public ObjectPluginMetadataBuilder plugin(Plugin plugin) {
            Preconditions.namedArgumentNonNull(plugin, "plugin");
    
            this.plugin = plugin;
            
            return this;
        }
        
        public ObjectPluginMetadataBuilder name(String name) {
            Preconditions.namedArgumentNonNull(name, "name");
    
            this.name = name;
            
            return this;
        }
        
        public ObjectPluginMetadataBuilder group(String group) {
            Preconditions.namedArgumentNonNull(group, "group");
    
            this.group = group;
            
            return this;
        }
        
        public ObjectPluginMetadataBuilder version(String version) {
            Preconditions.namedArgumentNonNull(version, "version");
    
            this.version = version;
            
            return this;
        }
        
        public ObjectPluginMetadataBuilder versionBranch(VersionBranch versionBranch) {
            Preconditions.namedArgumentNonNull(versionBranch, "version branch");
    
            this.versionBranch = versionBranch;
            
            return this;
        }
    
        public ObjectPluginMetadataBuilder depends(String[] depends) {
            Preconditions.namedArgumentNonNull(depends, "depends");
            
            this.depends = depends;
            
            return this;
        }
    
        public ObjectPluginMetadataBuilder softDepends(String[] softDepends) {
            Preconditions.namedArgumentNonNull(softDepends, "soft depends");
            
            this.softDepends = softDepends;
            
            return this;
        }
        
        public ObjectPluginMetadata build() {
            if (Strings.isEmpty(name)) {
                name = plugin.getClass().getSimpleName();
            }
    
            if (Strings.isEmpty(version)) {
                version = "unknown";
            }
    
            if (Strings.isEmpty(group)) {
                group = "unknown";
            }
            
            return new ObjectPluginMetadata(
                plugin,
                name,
                group,
                version,
                versionBranch,
                depends,
                softDepends
            );
        }
    }
    
    public static ObjectPluginMetadataBuilder builder(Plugin plugin) {
        return new ObjectPluginMetadataBuilder(plugin);
    }
    
    public static ObjectPluginMetadata of(Plugin plugin) {
        return builder(plugin).build();
    }
}
