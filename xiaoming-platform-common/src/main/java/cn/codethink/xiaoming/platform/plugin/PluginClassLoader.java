package cn.codethink.xiaoming.platform.plugin;

import cn.codethink.common.util.Preconditions;
import cn.codethink.xiaoming.platform.Platform;
import cn.codethink.xiaoming.platform.PlatformObject;
import lombok.Data;

import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLStreamHandlerFactory;

/**
 * 插件类加载器。
 *
 * 当插件直接从自己的 URL 中加载，或从自己的前置插件中加载类时，
 * 不会出现任何提示。
 *
 * 当插件从其他插件中加载类时，将会得到一个警告。
 *
 * @author Chuanwise
 */
@Data
@SuppressWarnings("all")
public class PluginClassLoader
        extends URLClassLoader
        implements PlatformObject {
    
    protected PluginHandler pluginHandler;
    
    public PluginClassLoader(PluginHandler pluginHandler, ClassLoader parent) {
        super(new URL[0], parent);
    
        Preconditions.namedArgumentNonNull(pluginHandler, "plugin handler");
        
        this.pluginHandler = pluginHandler;
    }
    
    @Override
    public Platform getPlatform() {
        return pluginHandler.getPlatform();
    }
    
    @Override
    public void addURL(URL url) {
        super.addURL(url);
    }
    
    /**
     * 仅在插件处尝试加载类，而不查看其依赖插件。
     *
     * @param name 类名
     * @return 加载到的类
     * @throws ClassNotFoundException 缺少类时
     */
    public Class<?> loadClassHere(String name) throws ClassNotFoundException {
        return super.findClass(name);
    }
    
    /**
     * 在插件和插件的依赖中寻找，最后在系统类加载器中寻找
     *
     * @param name 类名
     * @return 加载到的类
     * @throws ClassNotFoundException 缺少类时
     */
    @Override
    public Class<?> loadClass(String name) throws ClassNotFoundException {
        // load class here
        try {
            return loadClassHere(name);
        } catch (ClassNotFoundException ignored) {
        }
        
        // load class from its depend
        final Platform platform = getPlatform();
        final GlobalClassLoader globalClassLoader = platform.getGlobalClassLoader();
        try {
            return globalClassLoader.loadClassAsPlugin(pluginHandler, name);
        } catch (ClassNotFoundException ignored) {
        }
        
        // call super
        return super.loadClass(name);
    }
    
    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        throw new ClassNotFoundException();
    }
}
