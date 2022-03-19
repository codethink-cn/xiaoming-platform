package cn.codethink.xiaoming.platform.user;

import cn.codethink.xiaoming.code.Code;
import cn.codethink.xiaoming.contact.Contact;
import cn.codethink.xiaoming.message.MessagePushable;
import cn.codethink.xiaoming.platform.PlatformObject;

/**
 * 正在使用的用户
 *
 * @author Chuanwise
 */
public interface User
    extends MessagePushable, PlatformObject {
    
    /**
     * 获取用户码
     *
     * @return 用户码
     */
    Code getCode();
    
    /**
     * 获取和用户的会话
     *
     * @return 和用户的会话
     */
    Contact getContact();
    
    
}