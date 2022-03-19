package cn.codethink.xiaoming.platform.user;

import cn.codethink.xiaoming.code.Code;
import cn.codethink.xiaoming.concurrent.BotFuture;
import cn.codethink.xiaoming.contact.Group;
import cn.codethink.xiaoming.contact.Member;
import cn.codethink.xiaoming.message.Message;
import cn.codethink.xiaoming.message.content.MessageContent;
import cn.codethink.xiaoming.platform.Platform;

/**
 * 私聊用户
 *
 * @author Chuanwise
 */
public class MemberUser
    extends AbstractUser {
    
    protected final Member member;
    
    public MemberUser(Platform platform, Member member) {
        super(platform, member.getBot());
        
        this.member = member;
    }
    
    @Override
    public Code getCode() {
        return member.getCode();
    }
    
    @Override
    public Member getContact() {
        return member;
    }
    
    @Override
    public BotFuture<Message> sendMessage(MessageContent messageContent) {
        return member.sendMessage(messageContent);
    }
}
