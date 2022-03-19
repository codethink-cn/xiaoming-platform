package cn.codethink.xiaoming.platform.bot;

import cn.codethink.xiaoming.Bot;
import cn.codethink.xiaoming.MiraiBotFactory;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Mirai 的机器人账号
 *
 * @author Chuanwise
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MiraiBotAccount
    implements BotAccount {
    
    protected long qq;
    
    protected byte[] md5;
    
    @Override
    public Bot toBot() {
        return MiraiBotFactory.newBot(qq, md5);
    }
}
