package cn.codethink.xiaoming.platform.plugin;

import cn.codethink.xiaoming.logger.Logger;
import cn.codethink.xiaoming.platform.Platform;

/**
 * 对象插件控制器
 *
 * @author Chuanwise
 */
public class ObjectPluginHandler
    extends AbstractPluginHandler {
    
    public ObjectPluginHandler(Platform platform, ObjectPluginMetadata metadata) {
        super(platform, metadata);
    }
    
    @Override
    @SuppressWarnings("all")
    public boolean loadPlugin() {
        switch (state) {
            case LOADING:
            case ENABLING:
            case DISABLING:
            case UNLOADING:
                throw new IllegalStateException("can not change plugin state in parallel");
            case IDLE:
            case LOAD_ERROR:
                break;
            case LOADED:
            case ENABLED:
            case ENABLE_ERROR:
            case DISABLED:
            case DISABLE_ERROR:
            case FATAL_ERROR:
                return false;
            default:
                throw new IllegalStateException();
        }
    
        final PluginManager pluginManager = platform.getPluginManager();
        final Logger logger = pluginManager.getLogger();
    
        final String pluginName = metadata.getName();
        final ObjectPluginMetadata pluginMetadata = (ObjectPluginMetadata) metadata;
        
        plugin = pluginMetadata.plugin;
    
        classLoader = new PluginClassLoader(this, platform.getGlobalClassLoader());
        
        return callbackLoadingMethod(plugin);
    }
}
