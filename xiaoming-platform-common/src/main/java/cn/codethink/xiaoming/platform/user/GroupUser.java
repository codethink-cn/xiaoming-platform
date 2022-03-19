package cn.codethink.xiaoming.platform.user;

import cn.codethink.common.util.Preconditions;
import cn.codethink.xiaoming.code.Code;
import cn.codethink.xiaoming.concurrent.BotFuture;
import cn.codethink.xiaoming.contact.Group;
import cn.codethink.xiaoming.contact.Member;
import cn.codethink.xiaoming.message.Message;
import cn.codethink.xiaoming.message.content.MessageContent;
import cn.codethink.xiaoming.message.content.MessageContentBuildable;
import cn.codethink.xiaoming.message.element.AccountAt;
import cn.codethink.xiaoming.platform.Platform;

/**
 * 私聊用户
 *
 * @author Chuanwise
 */
public class GroupUser
    extends AbstractUser {
    
    protected final Member member;
    
    public GroupUser(Platform platform, Member member) {
        super(platform, member.getBot());
        
        this.member = member;
    }
    
    @Override
    public Code getCode() {
        return member.getCode();
    }
    
    @Override
    public Group getContact() {
        return (Group) member.getScope();
    }
    
    @Override
    public BotFuture<Message> sendMessage(MessageContent messageContent) {
        return getContact().sendMessage(messageContent);
    }
}
