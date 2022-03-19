package cn.codethink.xiaoming.platform.plugin;

import cn.codethink.common.util.Preconditions;
import cn.codethink.common.util.Strings;
import cn.codethink.xiaoming.logger.Logger;
import cn.codethink.xiaoming.platform.AbstractPlatformObject;
import cn.codethink.xiaoming.platform.Platform;
import lombok.Getter;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * @see PluginManager
 * @author Chuanwise
 */
@Getter
public class SimplePluginManager
        extends AbstractPlatformObject
        implements PluginManager {
    
    protected final Logger logger;
    
    protected final List<PluginHandler> pluginHandlers = new CopyOnWriteArrayList<>();
    
    protected final File directory;
    
    private static final String JAR_FILE_SUFFIX = ".jar";
    
    private static final String[] METADATA_FILE_NAMES = { "xiaoming.yml", "plugin.yml" };
    
    @Override
    public List<PluginHandler> getPluginHandlers() {
        return Collections.unmodifiableList(pluginHandlers);
    }
    
    @Override
    public PluginHandler getPluginHandler(String pluginName) {
        Preconditions.namedArgumentNonEmpty(pluginName, "plugin name");
        
        for (PluginHandler pluginHandler : pluginHandlers) {
            if (Objects.equals(pluginHandler.getMetadata().getName(), pluginName)) {
                return pluginHandler;
            }
        }
        return null;
    }
    
    @Override
    public PluginHandler getPluginHandler(UUID uuid) {
        Preconditions.namedArgumentNonNull(uuid, "uuid");
    
        for (PluginHandler pluginHandler : pluginHandlers) {
            if (Objects.equals(pluginHandler.getMetadata().getUuid(), uuid)) {
                return pluginHandler;
            }
        }
        return null;
    }
    
    @Override
    public void addPluginHandler(PluginHandler pluginHandler) {
        Preconditions.namedArgumentNonNull(pluginHandler, "plugin handler");
    
        final String pluginName = pluginHandler.getMetadata().getName();
        if (Objects.nonNull(getPluginHandler(pluginName))) {
            throw new UnsupportedOperationException("plugin " + pluginName + " already exists");
        }
        pluginHandlers.add(pluginHandler);
    }
    
    @Override
    public void loadPlugins() {
        for (PluginHandler pluginHandler : pluginHandlers) {
            if (!pluginHandler.isLoaded()) {
                pluginHandler.loadPlugin();
            }
        }
    }
    
    @Override
    public void enablePlugins() {
        for (PluginHandler pluginHandler : pluginHandlers) {
            if (!pluginHandler.isEnabled()) {
                pluginHandler.loadPlugin();
            }
        }
    }
    
    @Override
    public void disablePlugins() {
        for (PluginHandler pluginHandler : pluginHandlers) {
            if (pluginHandler.isEnabled()) {
                pluginHandler.disablePlugin();
            }
        }
    }
    
    @Override
    @SuppressWarnings("all")
    public void unloadPlugins() {
        for (int i = 0; i < pluginHandlers.size(); i++) {
            pluginHandlers.get(i).unloadPlugin();
        }
    }
    
    @Override
    public void flushPluginTables() {
        if (!directory.isDirectory() && !directory.mkdirs()) {
            throw new IllegalStateException("can not create the plugins directory: " + directory.getAbsolutePath());
        }
    
        final File[] files = directory.listFiles();
        if (Objects.isNull(files)) {
            logger.error("无法读取本地插件清单");
            return;
        }
    
        final List<PluginHandler> newPluginHandlers = new CopyOnWriteArrayList<>();
    
        // flush jar plugins
        for (File file : files) {
            if (!file.isFile()) {
                continue;
            }
    
            // parse jar name
            final String name = file.getName();
            if (!name.endsWith(JAR_FILE_SUFFIX)) {
                continue;
            }
            final String jarName = name.substring(0, name.length() - JAR_FILE_SUFFIX.length());
            
            // open jar file
            final JarPluginMetadata pluginMetadata;
            try (JarFile jarFile = new JarFile(file)) {
    
                JarPluginMetadata tempPluginMetadata = null;
                for (String metadataFileName : METADATA_FILE_NAMES) {
                    final JarEntry jarEntry = jarFile.getJarEntry(metadataFileName);
                    if (Objects.isNull(jarEntry)) {
                        continue;
                    }
                    
                    try (InputStream inputStream = jarFile.getInputStream(jarEntry)) {
                        tempPluginMetadata = platform.getFileService().load(JarPluginMetadata.class, inputStream);
                        break;
                    }
                }
    
                if (Objects.isNull(tempPluginMetadata)) {
                    logger.error("读取插件文件「" + file.getAbsolutePath() + "」没有元数据文件：xiaoming.yml");
                    continue;
                } else {
                    pluginMetadata = tempPluginMetadata;
                    
                    // 设置默认项目
                    if (Strings.isEmpty(pluginMetadata.name)) {
                        pluginMetadata.name = jarName;
                    }
                    if (Strings.isEmpty(pluginMetadata.main)) {
                        logger.error("插件「" + pluginMetadata.name + "」元数据缺少必要的主类名属性「main」");
                        continue;
                    }
                }
            } catch (IOException exception) {
                logger.error("读取插件文件「" + file.getAbsolutePath() + "」元数据时出现错误", exception);
                continue;
            }
            
            // set jar file
            pluginMetadata.setFile(file);
            
            // add to handler
            final PluginHandler elderPluginHandler = getPluginHandler(pluginMetadata.name);
            if (Objects.isNull(elderPluginHandler)) {
                newPluginHandlers.add(new JarPluginHandler(platform, pluginMetadata));
            } else {
                if (elderPluginHandler instanceof JarPluginMetadata) {
                    final JarPluginMetadata elderPluginMetadata = (JarPluginMetadata) elderPluginHandler;
                    
                    elderPluginMetadata.setDepends(pluginMetadata.depends);
                    elderPluginMetadata.setSoftDepends(pluginMetadata.softDepends);
                    elderPluginMetadata.setGroup(pluginMetadata.group);
                    elderPluginMetadata.setVersion(pluginMetadata.version);
                    elderPluginMetadata.setVersionBranch(pluginMetadata.versionBranch);
                    elderPluginMetadata.setUuid(pluginMetadata.getUuid());
                    elderPluginMetadata.setFile(file);
                    
                    newPluginHandlers.add(elderPluginHandler);
                } else {
                    if (elderPluginHandler.isEnabled()) {
                        if (!elderPluginHandler.disablePlugin()) {
                            logger.error("刷新插件列表时发现插件「" + pluginMetadata.name + "」出现变化，但无法关闭旧插件。这可能导致无法预料的错误。");
                        }
                    }
                    if (elderPluginHandler.isLoaded()) {
                        if (!elderPluginHandler.unloadPlugin()) {
                            logger.error("刷新插件列表时发现插件「" + pluginMetadata.name + "」出现变化，但无法卸载旧插件。这可能导致无法预料的错误。");
                        }
                    }
                    
                    newPluginHandlers.add(new JarPluginHandler(platform, pluginMetadata));
                }
            }
        }
    
        // add object plugins
        for (PluginHandler pluginHandler : pluginHandlers) {
            final int index = cn.codethink.common.util.Collections.indexIf(newPluginHandlers, x -> Objects.equals(x.getMetadata().getName(), x.getMetadata().getName()));
            if (index == -1) {
                if (!(pluginHandler instanceof JarPluginHandler)) {
                    newPluginHandlers.add(pluginHandler);
                }
                continue;
            }
            final PluginHandler elderPluginHandler = newPluginHandlers.get(index);
    
            if (elderPluginHandler == pluginHandler) {
                continue;
            }
        }
        
        pluginHandlers.clear();
        pluginHandlers.addAll(newPluginHandlers);
    }
    
    public SimplePluginManager(Platform platform, File directory) {
        super(platform);
    
        Preconditions.namedArgumentNonNull(directory, "directory");
    
        this.directory = directory;
    
        logger = platform.getPlatformConfiguration().getLoggerFactory().getLogger("plugin manager");
    }
}
