package cn.codethink.xiaoming.platform.account;

import cn.codethink.xiaoming.code.Code;
import cn.codethink.xiaoming.platform.file.Storable;

import java.util.Map;

/**
 * 用户管理器
 *
 * @author Chuanwise
 */
public interface AccountManager
    extends Storable {
    
    /**
     * 获取某个缓存的用户
     *
     * @param code 用户码
     * @return 缓存的用户，或 null
     */
    Account getAccount(Code code);
    
    /**
     * 获取或创建一个新的用户
     *
     * @param code 用户码
     * @return 用户
     */
    Account createAccount(Code code);
    
    /**
     * 获取所有缓存的用户
     *
     * @return 所有缓存的用户
     */
    Map<Code, Account> getAccounts();
}
