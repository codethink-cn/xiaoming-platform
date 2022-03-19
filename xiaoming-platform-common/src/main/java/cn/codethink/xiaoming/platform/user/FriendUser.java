package cn.codethink.xiaoming.platform.user;

import cn.codethink.xiaoming.code.Code;
import cn.codethink.xiaoming.concurrent.BotFuture
    ;
import cn.codethink.xiaoming.contact.Friend;
import cn.codethink.xiaoming.message.Message;
import cn.codethink.xiaoming.message.content.MessageContent;
import cn.codethink.xiaoming.platform.Platform;

/**
 * 私聊用户
 *
 * @author Chuanwise
 */
public class FriendUser
    extends AbstractUser {
    
    protected final Friend friend;
    
    public FriendUser(Platform platform, Friend friend) {
        super(platform, friend.getBot());
        
        this.friend = friend;
    }
    
    @Override
    public Code getCode() {
        return friend.getCode();
    }
    
    @Override
    public Friend getContact() {
        return friend;
    }
    
    @Override
    public BotFuture<Message> sendMessage(MessageContent messageContent) {
        return friend.sendMessage(messageContent);
    }
}
