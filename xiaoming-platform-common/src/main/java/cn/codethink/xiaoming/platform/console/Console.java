package cn.codethink.xiaoming.platform.console;

import cn.codethink.common.util.Preconditions;
import cn.codethink.xiaoming.code.Code;
import cn.codethink.xiaoming.concurrent.BotFuture;
import cn.codethink.xiaoming.contact.Contact;
import cn.codethink.xiaoming.logger.Logger;
import cn.codethink.xiaoming.logger.LoggerFactory;
import cn.codethink.xiaoming.message.Message;
import cn.codethink.xiaoming.message.content.MessageContent;
import cn.codethink.xiaoming.platform.AbstractPlatformObject;
import cn.codethink.xiaoming.platform.Platform;
import cn.codethink.xiaoming.platform.user.User;
import lombok.Data;

/**
 * 控制台会话
 *
 * @author Chuanwise
 */
@Data
@SuppressWarnings("all")
public class Console
    extends AbstractPlatformObject
    implements User {
    
    protected static final Code CODE = Code.ofLong(0);

    protected final Logger logger;
    
    protected final ConsoleContact contact;
    
    public Console(Platform platform) {
        super(platform);
        
        contact = new ConsoleContact(platform);
        logger = contact.getLogger();
    }
    
    @Override
    public Code getCode() {
        return CODE;
    }
    
    @Override
    public Contact getContact() {
        return contact;
    }
    
    @Override
    public BotFuture<Message> sendMessage(MessageContent messageContent) {
        return contact.sendMessage(messageContent);
    }
}
