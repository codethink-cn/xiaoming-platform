package cn.codethink.xiaoming.platform.account;

import cn.chuanwise.common.util.Maps;
import cn.codethink.common.util.Preconditions;
import cn.codethink.xiaoming.code.Code;
import cn.codethink.xiaoming.platform.file.StorableFile;
import lombok.Getter;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @see cn.codethink.xiaoming.platform.account.AccountManager
 * @author Chuanwise
 */
@Getter
public abstract class AbstractAccountManager
    extends StorableFile
    implements AccountManager {
    
    protected final Map<Code, Account> accounts = new HashMap<>();
    
    @Override
    public Map<Code, Account> getAccounts() {
        return Collections.unmodifiableMap(accounts);
    }
    
    @Override
    public Account getAccount(Code code) {
        Preconditions.namedArgumentNonNull(code, "code");
        
        return accounts.get(code);
    }
    
    @Override
    public Account createAccount(Code code) {
        Preconditions.namedArgumentNonNull(code, "code");

        return Maps.getOrPutSupply(accounts, code, () -> {
            final Account account = new Account();
            account.code = code;
            return account;
        });
    }
}
