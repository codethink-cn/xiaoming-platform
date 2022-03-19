package cn.codethink.xiaoming.platform.plugin;

import cn.codethink.common.util.Preconditions;
import cn.codethink.xiaoming.platform.Platform;
import cn.codethink.xiaoming.platform.PlatformObject;
import lombok.Data;

import java.net.URL;
import java.net.URLClassLoader;
import java.util.List;
import java.util.Objects;

/**
 * 全局类加载器
 *
 * @author Chuanwise
 */
@Data
@SuppressWarnings("all")
public class GlobalClassLoader
    extends URLClassLoader
    implements PlatformObject {
    
    protected final Platform platform;
    
    public GlobalClassLoader(ClassLoader parent, Platform platform) {
        super(new URL[0], parent);
    
        Preconditions.namedArgumentNonNull(platform, "platform");
        
        this.platform = platform;
    }
    
    @Override
    public Platform getPlatform() {
        return platform;
    }
    
    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        try {
            return super.findClass(name);
        } catch (ClassNotFoundException ignored) {
        }
        
        // find class on all plugins
        final List<PluginHandler> pluginHandlers = platform.getPluginManager().getPluginHandlers();
        for (PluginHandler pluginHandler : pluginHandlers) {
            try {
                return pluginHandler.getClassLoader().loadClassHere(name);
            } catch (ClassNotFoundException ignored) {
            }
        }
        
        throw new ClassNotFoundException();
    }
    
    /**
     * 以插件身份加载类
     *
     * @param pluginHandler 插件
     * @param className 类名
     * @return 加载到的类
     * @throws ClassNotFoundException 没有找到对应的类时
     */
    public Class<?> loadClassAsPlugin(PluginHandler pluginHandler, String className) throws ClassNotFoundException {
        Preconditions.namedArgumentNonNull(pluginHandler, "plugin handler");
    
        final PluginManager pluginManager = platform.getPluginManager();
    
        // load class from its depend plugin
        final PluginMetadata pluginMetadata = pluginHandler.getMetadata();
        final String[] dependPluginNames = pluginMetadata.getDepends();
        for (String dependPluginName : dependPluginNames) {
    
            // get its depend plugin
            final PluginHandler dependPluginHandler = pluginManager.getPluginHandler(dependPluginName);
            // if plugin not load, ignore
            if (Objects.isNull(dependPluginHandler)
                || !dependPluginHandler.isLoaded()) {
                continue;
            }
            
            // load class
            try {
                return dependPluginHandler.getClassLoader().loadClass(className);
            } catch (ClassNotFoundException ignored) {
            }
        }
    
        // load class from its soft depend plugin
        final String[] softDependPluginNames = pluginMetadata.getSoftDepends();
        for (String softDependPluginName : softDependPluginNames) {
    
            // get its depend plugin
            final PluginHandler dependPluginHandler = pluginManager.getPluginHandler(softDependPluginName);
            // if plugin not load, ignore
            if (Objects.isNull(dependPluginHandler)
                || !dependPluginHandler.isLoaded()) {
                continue;
            }
            
            // load class
            try {
                return dependPluginHandler.getClassLoader().loadClass(className);
            } catch (ClassNotFoundException ignored) {
            }
        }
        
        // load class from other plugin
        for (PluginHandler otherPluginHandler : pluginManager.getPluginHandlers()) {
            // prevent loop
            if (otherPluginHandler == pluginHandler) {
                continue;
            }
            
            // prevent depend
            final PluginMetadata otherPluginHandlerPluginMetadata = otherPluginHandler.getMetadata();
            final String otherPluginName = otherPluginHandlerPluginMetadata.getName();
            if (pluginMetadata.isDependPlugin(otherPluginName)
                || pluginMetadata.isSoftDependPlugin(otherPluginName)) {
                continue;
            }
            
            // try load
            try {
                return otherPluginHandler.getClassLoader().loadClass(className);
            } catch (ClassNotFoundException ignored) {
            }
        }
        
        // call super
        return super.loadClass(className);
    }
}
