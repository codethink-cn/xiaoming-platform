package cn.codethink.xiaoming.platform.plugin;

import cn.codethink.common.util.Preconditions;
import cn.codethink.xiaoming.logger.Logger;
import cn.codethink.xiaoming.platform.Platform;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.io.File;

/**
 * @see cn.codethink.xiaoming.platform.plugin.Plugin
 * @author Chuanwise
 */
@Getter(AccessLevel.PUBLIC)
@Setter(AccessLevel.PACKAGE)
public abstract class AbstractPlugin
    implements Plugin {
    
    private Platform platform;
    
    private PluginHandler pluginHandler;
    
    private File folder;
    
    private Logger logger;
    
    /**
     * 创建插件数据文件夹
     *
     * @return 插件数据文件夹
     */
    public File createFolder() {
        Preconditions.stateNonNull(folder, "folder is null");
        Preconditions.state(!folder.isDirectory() && !folder.mkdirs(), "can not create plugin folder: " + folder.getAbsolutePath());
        
        return folder;
    }
}
