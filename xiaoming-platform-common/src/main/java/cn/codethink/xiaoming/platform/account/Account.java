package cn.codethink.xiaoming.platform.account;

import cn.codethink.xiaoming.code.Code;
import lombok.Data;

/**
 * 账号
 *
 * @author Chuanwise
 */
@Data
public class Account {
    
    /**
     * 用户码
     */
    protected Code code;
    
    /**
     * 是否是管理员
     */
    protected boolean operator;
    
    /**
     * 是否被封禁
     */
    protected boolean banned;
}
