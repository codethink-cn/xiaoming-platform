package cn.codethink.xiaoming.platform.user;

import cn.codethink.common.util.Preconditions;
import cn.codethink.xiaoming.Bot;
import cn.codethink.xiaoming.BotObject;
import cn.codethink.xiaoming.platform.AbstractPlatformObject;
import cn.codethink.xiaoming.platform.Platform;
import lombok.Data;

/**
 * @see cn.codethink.xiaoming.platform.user.User
 * @author Chuanwise
 */
@Data
@SuppressWarnings("all")
public abstract class AbstractUser
    extends AbstractPlatformObject
    implements BotObject, User {
    
    protected final Bot bot;
    
    public AbstractUser(Platform platform, Bot bot) {
        super(platform);
    
        Preconditions.namedArgumentNonNull(bot, "bot");
        
        this.bot = bot;
    }
}
