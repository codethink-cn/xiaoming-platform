package cn.codethink.xiaoming.platform;

import cn.codethink.common.util.Preconditions;
import cn.codethink.xiaoming.Bot;
import cn.codethink.xiaoming.logger.Logger;
import cn.codethink.xiaoming.platform.account.AccountManager;
import cn.codethink.xiaoming.platform.account.SimpleAccountManager;
import cn.codethink.xiaoming.platform.command.CommandManager;
import cn.codethink.xiaoming.platform.command.CommandLibManager;
import cn.codethink.xiaoming.platform.concurrent.Scheduler;
import cn.codethink.xiaoming.platform.concurrent.ThreadPoolScheduler;
import cn.codethink.xiaoming.platform.configuration.CoreConfiguration;
import cn.codethink.xiaoming.platform.configuration.FileServiceConfiguration;
import cn.codethink.xiaoming.platform.configuration.PlatformConfiguration;
import cn.codethink.xiaoming.platform.console.ConsoleService;
import cn.codethink.xiaoming.platform.console.DumbConsoleService;
import cn.codethink.xiaoming.platform.event.EventManager;
import cn.codethink.xiaoming.platform.event.SimpleEventManager;
import cn.codethink.xiaoming.platform.exception.BotNotFoundException;
import cn.codethink.xiaoming.platform.exception.PlatformStartException;
import cn.codethink.xiaoming.platform.exception.PlatformStopException;
import cn.codethink.xiaoming.platform.permission.PermissionService;
import cn.codethink.xiaoming.platform.permission.SimplePermissionService;
import cn.codethink.xiaoming.platform.plugin.GlobalClassLoader;
import cn.codethink.xiaoming.platform.plugin.PluginManager;
import cn.codethink.xiaoming.platform.file.FileService;
import cn.codethink.xiaoming.platform.file.YamlFileService;
import cn.codethink.xiaoming.platform.plugin.SimplePluginManager;
import cn.codethink.xiaoming.platform.util.SystemProperties;
import lombok.Getter;

import java.io.*;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;

/**
 * @see cn.codethink.xiaoming.platform.Platform
 * @author Chuanwise
 */
@Getter
public abstract class AbstractPlatform
    implements Platform {
    
    protected volatile PlatformState state = PlatformState.IDLE;
    
    protected GlobalClassLoader globalClassLoader;
    
    protected EventManager eventManager;
    
    protected Scheduler scheduler;
    
    protected FileService fileService;
    
    protected ConsoleService consoleService = new DumbConsoleService(this);
    
    protected CommandManager commandManager;
    
    protected PluginManager pluginManager;
    
    protected PermissionService permissionService;
    
    protected AccountManager accountManager;
    
    protected PlatformConfiguration platformConfiguration;
    
    protected Logger logger;
    
    @SuppressWarnings("all")
    public AbstractPlatform(PlatformConfiguration platformConfiguration) {
        Preconditions.namedArgumentNonNull(platformConfiguration, "platform configuration");
        
        this.platformConfiguration = platformConfiguration;
        
        Runtime.getRuntime().addShutdownHook(new Thread(this::stop));
    }
    
    public AbstractPlatform() {
        this(new PlatformConfiguration());
    }
    
    @Override
    public void setConsoleService(ConsoleService consoleService) {
        Preconditions.namedArgumentNonNull(consoleService, "console service");
        
        this.consoleService = consoleService;
    }
    
    @Override
    public void setPlatformConfiguration(PlatformConfiguration platformConfiguration) {
        Preconditions.namedArgumentNonNull(platformConfiguration, "platform configuration");
        
        this.platformConfiguration = platformConfiguration;
        
        setupPlatformConfiguration(platformConfiguration);
    }
    
    protected abstract void setupPlatformConfiguration(PlatformConfiguration platformConfiguration);
    
    @Override
    public PlatformState getState() {
        return state;
    }

    @Override
    @SuppressWarnings("all")
    public boolean start() {
        switch (state) {
            case STARTING:
            case STOPPING:
                throw new IllegalStateException("can not change platform state in parallel");
            case IDLE:
            case START_ERROR:
                break;
            case STARTED:
            case STOP_ERROR:
            case FATAL_ERROR:
                return false;
            default:
                throw new IllegalStateException();
        }
    
        final List<Bot> bots = platformConfiguration.getBots();
        if (bots.isEmpty()) {
            throw new BotNotFoundException(this);
        }
    
        try {
            state = PlatformState.STARTING;
    
            logger = platformConfiguration.getLoggerFactory().getLogger("platform");
            
            logger.info("????????????????????????");
            
            // class loader
            globalClassLoader = new GlobalClassLoader(getClass().getClassLoader(), this);
            
            // create working directory
            final File workingDirectory = platformConfiguration.getWorkingDirectory();
            if (!workingDirectory.isDirectory()) {
                if (!workingDirectory.mkdirs()) {
                    throw new PlatformStartException(this, "can not create the working directory: " + workingDirectory.getAbsolutePath());
                }
            }
            
            // load fileService
            fileService = new YamlFileService(this, new FileServiceConfiguration());
    
            // check eula
            
            // set eula by system property
            if (!SystemProperties.EULA.get(false)) {
                final File eulaFile = new File(workingDirectory, "eula.yml");
                if (!eulaFile.isFile()) {
                    eulaFile.createNewFile();
                    try (OutputStream outputStream = new FileOutputStream(eulaFile)) {
                        // write
                        outputStream.write(Eula.AUTHORIZATION_CONTENT.getBytes(Charset.defaultCharset()));
                    }
        
                    logger.warn("????????????????????????????????????????????? eula.yml???????????? " + eulaFile.getAbsolutePath());
                    logger.warn("??????????????????????????????????????????????????????????????????????????????????????? eula: false ?????? eula: true ?????????????????????");
        
                    state = PlatformState.START_ERROR;
                    return false;
                } else {
                    final Eula eula;
                    try {
                        eula = fileService.load(Eula.class, eulaFile);
                    } catch (Throwable throwable) {
                        logger.error("???????????????????????????????????????????????? eula.yml", throwable);
                        
                        logger.error("???????????????????????????????????? eula.yml ??????????????????");
                        logger.error("??????????????? " + eulaFile.getAbsolutePath() + " ?????????????????????????????????????????????????????? eula.yml");
    
                        state = PlatformState.START_ERROR;
                        return false;
                    }
    
                    if (!eula.isEula()) {
                        logger.error("?????????????????????????????????????????????????????? eula.yml?????????????????????????????????????????????");
                        logger.error("????????? " + eulaFile.getAbsolutePath() + "??????????????????????????????????????? eula: false ?????? eula: true ?????????????????????");
    
                        state = PlatformState.START_ERROR;
                        return false;
                    }
                }
            }
    
            // configuration directory
            final File configurationsDirectory = new File(workingDirectory, "configurations");
            if (!configurationsDirectory.isDirectory()) {
                if (!configurationsDirectory.mkdirs()) {
                    throw new PlatformStartException(this, "can not create the configurations directory: " + configurationsDirectory.getAbsolutePath());
                }
            }
    
            // load core config
            final CoreConfiguration coreConfiguration = fileService.loadOrElseGet(CoreConfiguration.class, new File(configurationsDirectory, "core.yml"), CoreConfiguration::new);
            platformConfiguration.setCoreConfiguration(coreConfiguration);
            
            // set debug mode
            // TODO: 2022/3/19 change logger level at runtime
//            if (coreConfiguration.isDebug()) {
//                platformConfiguration.getLoggerFactory().
//                System.setProperty(org.slf4j.impl.SimpleLogger.DEFAULT_LOG_LEVEL_KEY, "Info");
//            } else {
//                Loggers.setGlobalLoggersLevel(Level.INFO);
//            }
    
            // load platform configuration
            platformConfiguration.setPlatform(this);
            
            final File accountFile = new File(configurationsDirectory, "accounts.yml");
            logger.info("??????????????????...");
            accountManager = fileService.loadOrElseGet(SimpleAccountManager.class, accountFile, SimpleAccountManager::new);
    
            // enable scheduler
            scheduler = new ThreadPoolScheduler(this, coreConfiguration.getThreadCount());
    
            // enable event manager
            // forward all event to here
            eventManager = new SimpleEventManager(this);
            
            eventManager.registerListener(Object.class, event -> {
                if (getPlatformConfiguration().getCoreConfiguration().isLogEvents()) {
                    logger.info("event: " + event);
                }
            }, null);
            
            // start all bots
            for (Bot bot : bots) {
                bot.start();
            }
            
            permissionService = new SimplePermissionService(this);
    
            // command dispatcher
            commandManager = new CommandLibManager(this);
            
            // enable plugins
            final File pluginsDirectory = new File(workingDirectory, "plugins");
            if (!pluginsDirectory.isDirectory()) {
                if (!pluginsDirectory.mkdirs()) {
                    throw new PlatformStartException(this, "can not create the plugins directory: " + pluginsDirectory.getAbsolutePath());
                }
            }
            pluginManager = new SimplePluginManager(this, pluginsDirectory);
            
            logger.info("??????????????????...");
    
            // flush local plugin tables
            pluginManager.flushPluginTables();
    
            // load all plugins
            pluginManager.loadPlugins();
            pluginManager.enablePlugins();
            
            state = PlatformState.STARTED;
    
            logger.info("????????????????????????");
            return true;
        } catch (Throwable throwable) {
            state = PlatformState.START_ERROR;
            
            throw new PlatformStartException(this, throwable);
        }
    }
    
    @Override
    public boolean stop() {
        switch (state) {
            case STARTING:
            case STOPPING:
                throw new IllegalStateException("can not change platform state in parallel");
            case STARTED:
            case STOP_ERROR:
                break;
            case IDLE:
            case START_ERROR:
            case FATAL_ERROR:
                return false;
            default:
                throw new IllegalStateException();
        }
        
        try {
            state = PlatformState.STOPPING;
    
            logger.info("????????????????????????");

            // unload all plugins
            logger.info("??????????????????...");
            pluginManager.disablePlugins();
            pluginManager.unloadPlugins();
            
            platformConfiguration.setPlatform(null);
    
            consoleService.close();

            // stop scheduler
            try {
                scheduler.shutdownSync();
            } catch (InterruptedException e) {
                scheduler.shutdownImmediately();
            }

            commandManager = null;
    
            pluginManager = null;
    
            eventManager = null;
    
            fileService = null;
            
            accountManager = null;
            
            permissionService = null;
    
            globalClassLoader = null;
    
            state = PlatformState.IDLE;
    
            logger = null;
            
            return true;
        } catch (Throwable throwable) {
            state = PlatformState.STOP_ERROR;
            
            throw new PlatformStopException(this, throwable);
        }
    }
    
    @Override
    public boolean isStarted() {
        return state == PlatformState.STARTED;
    }
    
    @Override
    public boolean isStopped() {
        return state != PlatformState.STARTED;
    }
}
