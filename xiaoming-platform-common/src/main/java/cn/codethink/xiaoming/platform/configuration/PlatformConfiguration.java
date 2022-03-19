package cn.codethink.xiaoming.platform.configuration;

import cn.codethink.common.util.Preconditions;
import cn.codethink.xiaoming.Bot;
import cn.codethink.xiaoming.exception.BotStartException;
import cn.codethink.xiaoming.exception.BotStopException;
import cn.codethink.xiaoming.logger.LoggerFactory;
import cn.codethink.xiaoming.logger.Slf4jLoggerFactory;
import cn.codethink.xiaoming.platform.Platform;
import cn.codethink.xiaoming.platform.event.BotEventForwarder;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * 平台设置，是多个设置的大杂烩
 *
 * @author Chuanwise
 */
@Data
public class PlatformConfiguration {
    
    /**
     * 平台
     */
    protected volatile Platform platform;
    
    /**
     * @see BotEventForwarder
     */
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private BotEventForwarder botEventForwarder;
    
    /**
     * 设置配置对应的平台
     *
     * @param platform 配置对应的平台
     */
    public void setPlatform(Platform platform) {
        final boolean previousIsNull = Objects.isNull(this.platform);
        final boolean currentIsNull = Objects.isNull(platform);

        // null -> null
        if (previousIsNull && currentIsNull) {
            return;
        }
        
        // nonnull -> nonnull
        if (!previousIsNull && !currentIsNull) {
            if (this.platform == platform) {
                return;
            }
            throw new IllegalStateException("platform configuration can not be shared!");
        }
        
        // nonnull -> null
        // null -> nonnull
        this.platform = platform;
        if (Objects.nonNull(platform)) {
            botEventForwarder = new BotEventForwarder(platform);
            
            // add forwarder
            for (Bot bot : bots) {
    
                final File implementsDirectory = new File(platform.getPlatformConfiguration().getWorkingDirectory(), "implements");
    
                // set working directory
                final String instantMessengerName = bot.getInstantMessenger().name().toLowerCase();
                final File implementDirectory = new File(implementsDirectory, instantMessengerName);
                if (!implementDirectory.isDirectory() && !implementDirectory.mkdirs()) {
                    platform.getLogger().error("can not create implements directory: " + implementDirectory.getAbsolutePath());
                    platform.getLogger().error("无法创建实现文件夹");
                    throw new BotStartException(bot);
                }
    
                bot.getBotConfiguration().setWorkingDirectory(implementDirectory);
    
                // try to enable
                if (platform.isStarted()) {
                    bot.start();
                    eventForwarder = new BotEventForwarder(platform);
                    bot.getEventManager().registerListeners(eventForwarder);
                }
                
                bot.start();
                
                if (bot.isStarted()) {
                    bot.getEventManager().registerListeners(botEventForwarder);
                }
            }
        } else {
            // remove forwarder
            for (Bot bot : bots) {
                bot.stop();
                
                if (bot.isStarted()) {
                    bot.getEventManager().unregisterListeners(botEventForwarder);
                }
            }
            
            botEventForwarder = null;
        }
    }
    
    /**
     * 核心日志工具
     */
    protected LoggerFactory loggerFactory = new Slf4jLoggerFactory();
    
    /**
     * 平台核心配置
     */
    protected CoreConfiguration coreConfiguration = new CoreConfiguration();
    
    /**
     * 工作目录
     */
    protected File workingDirectory = new File(System.getProperty("user.dir"));
    
    /**
     * 统计数据
     */
    protected StatisticianConfiguration statistician = new StatisticianConfiguration();
    
    /**
     * 正在使用的机器人
     */
    protected List<Bot> bots = new ArrayList<>();
    
    /**
     * 事件转发器
     */
    private volatile BotEventForwarder eventForwarder;
    
    /**
     * 获取正在使用的机器人
     *
     * @return 机器人
     */
    public List<Bot> getBots() {
        return Collections.unmodifiableList(bots);
    }
    
    /**
     * 添加一个机器人
     *
     * @param bot 机器人
     * @return 是否成功添加
     */
    public boolean addBot(Bot bot) {
        Preconditions.namedArgumentNonNull(bot, "bot");
        return cn.codethink.common.util.Collections.addDistinctly(bots, bot);
    }
    
    /**
     * 删除一个机器人
     *
     * @param bot 机器人
     * @return 是否成功删除
     */
    public boolean removeBot(Bot bot) {
        Preconditions.namedArgumentNonNull(bot, "bot");
    
        final boolean removed = bots.remove(bot);
    
        // unregister event forwarder
        if (removed && Objects.nonNull(platform) && bot.isStarted()) {
            bot.getEventManager().unregisterListeners(botEventForwarder);
        }
        
        return removed;
    }
}
