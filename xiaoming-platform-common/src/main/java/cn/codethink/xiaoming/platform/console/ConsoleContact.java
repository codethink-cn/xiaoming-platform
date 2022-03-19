package cn.codethink.xiaoming.platform.console;

import cn.codethink.common.util.Preconditions;
import cn.codethink.xiaoming.AbstractBot;
import cn.codethink.xiaoming.Bot;
import cn.codethink.xiaoming.code.Code;
import cn.codethink.xiaoming.concurrent.BotFuture;
import cn.codethink.xiaoming.concurrent.SucceedBotFuture;
import cn.codethink.xiaoming.contact.Contact;
import cn.codethink.xiaoming.logger.Logger;
import cn.codethink.xiaoming.message.AbstractMessage;
import cn.codethink.xiaoming.message.Message;
import cn.codethink.xiaoming.message.ResourcePool;
import cn.codethink.xiaoming.message.content.MessageContent;
import cn.codethink.xiaoming.platform.AbstractPlatformObject;
import cn.codethink.xiaoming.platform.Platform;
import cn.codethink.xiaoming.platform.exception.BotNotFoundException;
import lombok.Getter;

import java.util.List;

/**
 * 控制台虚拟会话
 *
 * @author Chuanwise
 */
@Getter
public class ConsoleContact
    extends AbstractPlatformObject
    implements Contact {
    
    protected static final Code CODE = Code.ofLong(0);
    
    protected final Logger logger;
    
    public ConsoleContact(Platform platform) {
        super(platform);
    
        logger = platform.getPlatformConfiguration().getLoggerFactory().getLogger("console");
    }
    
    @Override
    public Code getCode() {
        return CODE;
    }
    
    @Override
    public Bot getBot() {
        final List<Bot> bots = platform.getPlatformConfiguration().getBots();
        if (bots.isEmpty()) {
            throw new BotNotFoundException(platform);
        }
        return bots.get(0);
    }
    
    @Override
    public BotFuture<Message> sendMessage(MessageContent messageContent) {
        Preconditions.namedArgumentNonNull(messageContent, "message content");
    
        logger.info(messageContent.toMessageCode());
    
        final AbstractBot bot = (AbstractBot) getBot();
    
        // build console message
        final ResourcePool resourcePool = bot.getResourcePool();
        final Code code = resourcePool.allocateMessageCode();
        final AbstractMessage consoleMessage = new AbstractMessage(
            code,
            messageContent,
            System.currentTimeMillis()
        );
    
        return new SucceedBotFuture<>(bot, resourcePool.cacheMessage(consoleMessage));
    }
}
