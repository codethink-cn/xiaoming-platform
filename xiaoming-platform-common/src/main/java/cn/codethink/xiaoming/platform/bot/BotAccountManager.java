package cn.codethink.xiaoming.platform.bot;

import cn.codethink.xiaoming.platform.file.StorableFile;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * Bot 账号管理器
 *
 * @author Chuanwise
 */
@Data
public class BotAccountManager
    extends StorableFile {
    
    /**
     * 机器人账户
     */
    List<BotAccount> botAccounts = new ArrayList<>();
}
