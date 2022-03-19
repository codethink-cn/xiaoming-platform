package cn.codethink.xiaoming.platform.plugin;

import cn.codethink.common.util.Collections;
import cn.codethink.common.util.Preconditions;
import cn.codethink.xiaoming.logger.Logger;
import cn.codethink.xiaoming.platform.AbstractPlatformObject;
import cn.codethink.xiaoming.platform.Platform;
import cn.codethink.xiaoming.platform.concurrent.PlatformTask;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

/**
 * @see PluginHandler
 * @author Chuanwise
 */
@Data
@SuppressWarnings("all")
public abstract class AbstractPluginHandler
        extends AbstractPlatformObject
        implements PluginHandler {
    
    protected final PluginMetadata metadata;
    
    protected Plugin plugin;
    
    protected PluginState state = PluginState.IDLE;
    
    protected PluginClassLoader classLoader;
    
    public AbstractPluginHandler(Platform platform, PluginMetadata metadata) {
        super(platform);
        
        Preconditions.namedArgumentNonNull(metadata, "plugin metadata");
        
        this.metadata = metadata;
    }
    
    @Override
    public boolean isLoaded() {
        return state == PluginState.LOADED
            || state == PluginState.ENABLING
            || state == PluginState.ENABLED
            || state == PluginState.ENABLE_ERROR
            || state == PluginState.DISABLING
            || state == PluginState.DISABLED
            || state == PluginState.DISABLE_ERROR;
    }
    
    @Override
    public boolean isEnabled() {
        return state == PluginState.ENABLED;
    }
    
    @Override
    public boolean isDisabled() {
        return state != PluginState.ENABLED;
    }
    
    /**
     * 是否正在加载依赖。
     *
     * 当值为 true，且再次调用 enablePlugin，将会抛出循环引用异常。
     */
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private PluginHandler enablingDependPluginHandler;
    
    @Override
    public boolean enablePlugin() {
        switch (state) {
            case LOADING:
            case ENABLING:
            case DISABLING:
            case UNLOADING:
                throw new IllegalStateException("can not change plugin state in parallel");
            case LOADED:
            case ENABLE_ERROR:
                break;
            case IDLE:
            case LOAD_ERROR:
            case ENABLED:
            case DISABLED:
            case DISABLE_ERROR:
            case FATAL_ERROR:
                return false;
            default:
                throw new IllegalStateException();
        }
        if (Objects.nonNull(enablingDependPluginHandler)) {
            throw new IllegalStateException("loop dependencies: dependency plugin " + enablingDependPluginHandler.getMetadata().getCompleteName()
                + " denepnd to enabling plugin " + metadata.getCompleteName() + " itself.");
        }
        
        final PluginManager pluginManager = platform.getPluginManager();
        final Logger logger = pluginManager.getLogger();
        
        final String pluginName = metadata.getName();
        
        try {
            // check if all depend plugins is enabled
            final String[] dependPluginNames = metadata.getDepends();
            for (String dependPluginName : dependPluginNames) {
                final PluginHandler pluginHandler = platform.getPluginManager().getPluginHandler(dependPluginName);
                if (Objects.isNull(pluginHandler)) {
                    logger.error("无法启动插件「" + pluginName + "」，缺少必要的前置插件「" + dependPluginName + "」");
                    return false;
                }
    
                enablingDependPluginHandler = pluginHandler;
                if (!pluginHandler.isLoaded() && !pluginHandler.loadPlugin()) {
                    logger.error("无法启动插件「" + pluginName + "」，无法加载前置插件「" + dependPluginName + "」");
                    return false;
                }
        
                if (!pluginHandler.isEnabled() && !pluginHandler.enablePlugin()) {
                    logger.error("无法启动插件「" + pluginName + "」，无法启动前置插件「" + dependPluginName + "」");
                    return false;
                }
            }
    
            // try to enable all soft depend plugins
            final String[] softDependPluginNames = metadata.getSoftDepends();
            for (String softDependPluginName : softDependPluginNames) {
                final PluginHandler pluginHandler = platform.getPluginManager().getPluginHandler(softDependPluginName);
                if (Objects.isNull(pluginHandler)) {
                    continue;
                }
    
                enablingDependPluginHandler = pluginHandler;
                if (!pluginHandler.isLoaded()) {
                    pluginHandler.loadPlugin();
                }
        
                if (!pluginHandler.isEnabled()) {
                    pluginHandler.enablePlugin();
                }
            }
        } finally {
            enablingDependPluginHandler = null;
        }
        
        try {
            state = PluginState.ENABLING;
            
            logger.info("正在启动插件「" + pluginName + "」");
            plugin.onEnable();
            
            state = PluginState.ENABLED;
        } catch (Throwable throwable) {
            state = PluginState.ENABLE_ERROR;
            logger.error("启动插件「" + pluginName + "」时出现异常", throwable);
            return false;
        }
        
        return false;
    }
    
    @Override
    public boolean disablePlugin() {
        switch (state) {
            case LOADING:
            case ENABLING:
            case DISABLING:
            case UNLOADING:
                throw new IllegalStateException("can not change plugin state in parallel");
            case DISABLE_ERROR:
            case ENABLED:
                break;
            case IDLE:
            case LOAD_ERROR:
            case DISABLED:
            case LOADED:
            case ENABLE_ERROR:
            case FATAL_ERROR:
                return false;
            default:
                throw new IllegalStateException();
        }
        
        final PluginManager pluginManager = platform.getPluginManager();
        final Logger logger = pluginManager.getLogger();
        
        final String pluginName = metadata.getName();
        
        // disable all depended plugins
        for (PluginHandler pluginHandler : pluginManager.getPluginHandlers()) {
            if (pluginHandler.getMetadata().isDependPlugin(pluginName)) {
                if (pluginHandler.isEnabled() && !pluginHandler.disablePlugin()) {
                    logger.error("无法关闭插件「" + pluginName + "」的前置插件「" + pluginHandler.getMetadata().getName() + "」，" +
                        "但插件「" + pluginName + "」仍将被关闭，这可能导致意料之外的错误");
                }
            }
        }
        
        try {
            state = PluginState.DISABLING;
            
            plugin.onDisable();
            
            // unregister event listeners
            platform.getEventManager().unregisterListeners(plugin);
            
            platform.getPermissionService().removePermissionHandlers(plugin);
            
            platform.getCommandManager().unregisterPlugin(plugin);
    
            // cancel tasks
            final List<PlatformTask> platformTasks = platform.getScheduler().cancelTasks(plugin, true);
            if (Collections.nonEmpty(platformTasks)) {
                logger.warn("插件「" + pluginName +"」有 " + platformTasks.size() + " 个任务尚未完成");
            }
    
            state = PluginState.DISABLED;
        } catch (Throwable throwable) {
            state = PluginState.DISABLE_ERROR;
            logger.error("关闭插件「" + pluginName +"」时出现异常", throwable);
            return false;
        }
        
        return true;
    }
    
    @Override
    public boolean unloadPlugin() {
        switch (state) {
            case LOADING:
            case ENABLING:
            case DISABLING:
            case UNLOADING:
                throw new IllegalStateException("can not change plugin state in parallel");
            case LOADED:
                break;
            case IDLE:
            case DISABLE_ERROR:
            case ENABLED:
            case LOAD_ERROR:
            case DISABLED:
            case ENABLE_ERROR:
            case FATAL_ERROR:
                return false;
            default:
                throw new IllegalStateException();
        }
        
        final PluginManager pluginManager = platform.getPluginManager();
        final Logger logger = pluginManager.getLogger();
        
        final String pluginName = metadata.getName();
        
        try {
            state = PluginState.UNLOADING;
            
            // remove plugin from plugin manager
            ((SimplePluginManager) pluginManager).pluginHandlers.remove(this);
            
            // close class loader
            try {
                classLoader.close();
            } catch (IOException e) {
                logger.warn("卸载插件「" + pluginName + "」错误，无法关闭类加载器");
            }
            classLoader = null;
            
            // set plugin to null
            plugin = null;
            
            state = PluginState.IDLE;
        } catch (Throwable throwable) {
            state = PluginState.DISABLE_ERROR;
            logger.error("卸载插件「" + pluginName +"」时出现异常", throwable);
            return false;
        }
        
        return true;
    }
    
    protected boolean callbackLoadingMethod(Plugin plugin) {
        final PluginManager pluginManager = platform.getPluginManager();
        final Logger logger = pluginManager.getLogger();
        final String pluginName = metadata.getName();
    
        try {
            state = PluginState.LOADING;
            
            logger.info("正在加载插件「" + pluginName + "」");
            
            // set references
            if (plugin instanceof AbstractPlugin) {
                final AbstractPlugin abstractPlugin = (AbstractPlugin) plugin;
    
                abstractPlugin.setPlatform(platform);
                abstractPlugin.setLogger(platform.getPlatformConfiguration().getLoggerFactory().getLogger(metadata.getName()));
                abstractPlugin.setFolder(new File(pluginManager.getDirectory(), pluginName));
                abstractPlugin.setPluginHandler(this);
            }
            
            plugin.onLoad();
        
            state = PluginState.LOADED;
        } catch (Throwable throwable) {
            state = PluginState.LOAD_ERROR;
            logger.error("加载插件「" + pluginName + "」时出现异常", throwable);
            return false;
        }
        
        return true;
    }
}