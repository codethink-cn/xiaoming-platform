package cn.codethink.xiaoming.platform.debug;

import cn.codethink.common.util.Preconditions;
import cn.codethink.xiaoming.Bot;
import cn.codethink.xiaoming.MiraiBotFactory;
import cn.codethink.xiaoming.platform.Platform;
import cn.codethink.xiaoming.platform.SimplePlatform;
import cn.codethink.xiaoming.platform.configuration.CoreConfiguration;
import cn.codethink.xiaoming.platform.configuration.PlatformConfiguration;
import cn.codethink.xiaoming.platform.console.JlineConsoleService;
import cn.codethink.xiaoming.platform.exception.PlatformStartException;
import cn.codethink.xiaoming.platform.exception.PlatformStopException;
import cn.codethink.xiaoming.platform.plugin.*;
import org.apache.log4j.Level;
import org.apache.log4j.PropertyConfigurator;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 用 Mirai 实现的插件调试器
 *
 * @see cn.codethink.xiaoming.platform.debug.PluginDebug
 * @author Chuanwise
 */
public class MiraiImplementPluginDebug
    extends AbstractPluginDebug
    implements PluginDebug {
    
    private volatile Platform platform;
    
    private File workingDirectory;
    
    private byte[] passwordHash;
    
    private long qq;
    
    private File logFile;
    
    private List<ObjectPluginMetadata> pluginMetadataList = new ArrayList<>();
    
    @Override
    public Platform getPlatform() {
        return platform;
    }
    
    @Override
    public MiraiImplementPluginDebug workingDirectory(File workingDirectory) {
        Preconditions.namedArgumentNonNull(workingDirectory, "working directory");
    
        this.workingDirectory = workingDirectory;
        
        return this;
    }
    
    public MiraiImplementPluginDebug password(String password) {
        Preconditions.namedArgumentNonEmpty(password, "password");
    
        final MessageDigest digest;
        try {
            digest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("lack MD5 algorithm");
        }
        passwordHash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
        
        return this;
    }
    
    public MiraiImplementPluginDebug passwordHash(byte[] passwordHash) {
        Preconditions.namedArgumentNonNull(passwordHash, "password hash");
    
        this.passwordHash = passwordHash;
        
        return this;
    }
    
    public MiraiImplementPluginDebug qq(long qq) {
        this.qq = qq;
        
        return this;
    }
    
    public MiraiImplementPluginDebug logFile(File logFile) {
        Preconditions.namedArgumentNonNull(logFile, "log file");
    
        this.logFile = logFile;
        
        return this;
    }
    
    public MiraiImplementPluginDebug plugin(ObjectPluginMetadata pluginMetadata) {
        Preconditions.namedArgumentNonNull(pluginMetadata, "plugin metadata");
    
        pluginMetadataList.add(pluginMetadata);
        
        return this;
    }
    
    public MiraiImplementPluginDebug plugin(Plugin plugin) {
        Preconditions.namedArgumentNonNull(plugin, "plugin");
    
        final ObjectPluginMetadata metadata = ObjectPluginMetadata.of(plugin);
        pluginMetadataList.add(metadata);
        
        return this;
    }
    
    @Override
    protected MiraiImplementPluginDebug start0() {
        Preconditions.stateIsNull(platform, "can not run multiply debugs at the same time");
        Preconditions.state(qq != 0, "lack qq");
        Preconditions.stateNonNull(passwordHash, "lack password");
        
        // set working directory
        if (Objects.isNull(workingDirectory)) {
            workingDirectory = new File(System.getProperty("user.dir"), "debug");
        }
        if (!workingDirectory.isDirectory() && !workingDirectory.mkdirs()) {
            throw new RuntimeException("can not create working directory: " + workingDirectory.getAbsolutePath());
        }
        
        // prepare debug logger
        final String propertyName = "xiaoming.platform.debug.log";
        if (Objects.isNull(logFile)) {
            // generate debug logs directory
            final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            final String date = dateFormat.format(System.currentTimeMillis());
    
            final File debugLogDirectory = new File(workingDirectory, "debug");
            if (!debugLogDirectory.isDirectory() && !debugLogDirectory.mkdirs()) {
                throw new RuntimeException("can not create debug logs directory: " + debugLogDirectory.getAbsolutePath());
            }
            int index = 1;
            for (; index < Integer.MAX_VALUE; index++) {
                logFile = new File(debugLogDirectory, date + "-" + index + ".log");
                if (!logFile.isFile()) {
                    break;
                }
            }
        }
        System.setProperty(propertyName, logFile.getAbsolutePath());
    
        // config log
        DebugLoggerConfiguration.install();
    
        // prepare bot
        final Bot bot = MiraiBotFactory.newBot(qq, passwordHash);
        platform = new SimplePlatform();
        
        // add bots
        final PlatformConfiguration platformConfiguration = platform.getPlatformConfiguration();
        platformConfiguration.addBot(bot);
        platformConfiguration.setWorkingDirectory(workingDirectory);
    
        if (!platform.start()) {
            throw new PlatformStartException(platform);
        }
        
        // setup console
        try {
            final JlineConsoleService consoleService = new JlineConsoleService(platform);
            consoleService.open();
            platform.setConsoleService(consoleService);
        } catch (IOException exception) {
            throw new PlatformStartException(platform, "can not open console service");
        }
    
        // set platform to null
        platform.getEventManager().registerListener(PlatformStopException.class, event -> {
            platform = null;
        }, null);
        
        // enable plugins
        final PluginManager pluginManager = platform.getPluginManager();
    
        final List<ObjectPluginHandler> handlerList = pluginMetadataList.stream()
            .map(x -> new ObjectPluginHandler(platform, x))
            .collect(Collectors.toList());
    
        // add all and load all
        for (PluginHandler pluginHandler : handlerList) {
            pluginManager.addPluginHandler(pluginHandler);
            pluginHandler.loadPlugin();
        }
        
        // enable all
        for (PluginHandler pluginHandler : handlerList) {
            pluginHandler.enablePlugin();
        }
    
        return this;
    }
    
    @Override
    public void stop() {
        Preconditions.stateNonNull(platform);
    
        platform.stop();
    }
}
