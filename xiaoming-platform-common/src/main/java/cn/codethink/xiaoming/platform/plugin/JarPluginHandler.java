package cn.codethink.xiaoming.platform.plugin;

import cn.codethink.common.util.Preconditions;
import cn.codethink.common.util.Reflections;
import cn.codethink.xiaoming.logger.Logger;
import cn.codethink.xiaoming.platform.Platform;
import lombok.Data;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.util.Objects;

/**
 * 从 Jar 文件中载入的插件的控制器
 *
 * @author Chuanwise
 */
@Data
@SuppressWarnings("all")
public class JarPluginHandler
    extends AbstractPluginHandler {
    
    public JarPluginHandler(Platform platform, JarPluginMetadata pluginMetadata) {
        super(platform, pluginMetadata);
    }
    
    @Override
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
    
        // load plugin main class name
        final JarPluginMetadata metadata = (JarPluginMetadata) this.getMetadata();
        final String mainClassName = metadata.getMain();
    
        // change state
        state = PluginState.LOADING;
    
        // extend plugin class loader
        classLoader = new PluginClassLoader(this, platform.getGlobalClassLoader());
        try {
            classLoader.addURL(metadata.getFile().toURI().toURL());
        } catch (MalformedURLException e) {
            state = PluginState.FATAL_ERROR;
            logger.error("加载插件「" + pluginName + "」错误，无法扩展类加载器", e);
            return false;
        }
        
        // load plugin main class
        final Class<?> mainClass;
        try {
            mainClass = classLoader.loadClass(mainClassName);
        } catch (ClassNotFoundException e) {
            state = PluginState.FATAL_ERROR;
            logger.error("加载插件「" + pluginName + "」错误，找不到插件主类「" + mainClassName + "」");
            return false;
        }
        
        // check if main class is a subclass of Plugin
        if (!Plugin.class.isAssignableFrom(mainClass)) {
            state = PluginState.FATAL_ERROR;
            logger.error("插件主类「" + mainClassName + "」没有实现插件接口「" + Plugin.class.getName() + "」");
            return false;
        }
        
        // this loop is set for breaking
        do {
            // check static INSTANCE property
    
            // if there is a static field called 'INSTANCE'
            // check if it's type is subclass of Plugin
            final Field staticInstanceField = Reflections.getDeclaredStaticField(mainClass, "INSTANCE");
            if (Objects.nonNull(staticInstanceField)) {
                final Class<?> staticInstanceFieldType = staticInstanceField.getType();
                if (Plugin.class.isAssignableFrom(staticInstanceFieldType)) {
                    // get its value
                    final Object staticFieldValue = Reflections.getStaticFieldValue(staticInstanceField);
            
                    // check if its null
                    if (staticFieldValue != null) {
                        plugin = (Plugin) staticFieldValue;
    
                        // check if it's an instance of main class
                        if (!mainClass.isInstance(plugin)) {
                            logger.warn("插件主类「" + mainClassName + "」具备静态属性 INSTANCE，但并非插件主类类型或其子类。插件将继续加载");
                        }
                        break;
                    } else {
                        logger.warn("插件主类「" + mainClassName + "」具备静态属性 INSTANCE，但其值为 null");
                    }
                } else {
                    logger.warn("插件主类「" + mainClassName + "」具备静态属性 INSTANCE，但并非插件类型「" + Plugin.class.getName() + "」");
                }
            }
            
            // if there is a static method called 'getInstance'
            // check if it's type is subclass of Plugin
            final Method staticGetInstanceMethod = Reflections.getDeclaredStaticMethod(mainClass, "getInstance");
            if (Objects.nonNull(staticGetInstanceMethod)) {
                final Class<?> returnType = staticGetInstanceMethod.getReturnType();
                if (Plugin.class.isAssignableFrom(returnType)) {
    
                    try {
                        plugin = (Plugin) Reflections.invokeStaticMethod(staticGetInstanceMethod);
    
                        // check if it's an instance of main class
                        if (!mainClass.isInstance(plugin)) {
                            logger.warn("插件主类「" + mainClassName + "」具备静态方法 getInstance()，但并非插件主类类型或其子类。插件将继续加载");
                        }
                        break;
                    } catch (InvocationTargetException e) {
                        final Throwable cause = e.getCause();
                        logger.error("插件主类「" + mainClassName + "」具备静态方法 getInstance()，但调用时出现异常", cause);
                    }
                } else {
                    logger.warn("插件主类「" + mainClassName + "」具备静态方法 getInstance()，但返回值并非插件类型「" + Plugin.class.getName() + "」");
                }
            }
            
            // if there is a non-argument constructor
            final Constructor<?> constructor;
            try {
                constructor = mainClass.getDeclaredConstructor();
            } catch (NoSuchMethodException e) {
                logger.error("插件主类「" + mainClassName + "」缺少无参默认构造函数，无法构造实例");
                state = PluginState.FATAL_ERROR;
                return false;
            }
    
            final boolean accessible = constructor.isAccessible();
            constructor.setAccessible(true);
            try {
                plugin = (Plugin) constructor.newInstance();
            } catch (InstantiationException e) {
                logger.error("插件主类「" + mainClassName + "」无法被构造", e);
                state = PluginState.FATAL_ERROR;
                return false;
            } catch (IllegalAccessException ignored) {
            } catch (InvocationTargetException e) {
                final Throwable cause = e.getCause();
                logger.error("插件主类「" + mainClassName + "」构造时出现异常", cause);
                state = PluginState.FATAL_ERROR;
                return false;
            } catch (Throwable throwable) {
                logger.error("插件主类「" + mainClassName + "」构造时出现未知异常", throwable);
                state = PluginState.FATAL_ERROR;
                return false;
            } finally {
                constructor.setAccessible(accessible);
            }
        } while (false);
        
        return callbackLoadingMethod(plugin);
    }
}
