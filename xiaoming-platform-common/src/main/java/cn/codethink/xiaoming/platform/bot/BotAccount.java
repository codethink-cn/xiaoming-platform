package cn.codethink.xiaoming.platform.bot;

import cn.codethink.xiaoming.Bot;

/**
 * 表示一个机器人账号
 *
 * @author Chuanwise
 */
public interface BotAccount {
    
    /**
     * 构造一个 Bot
     *
     * @return Bot
     */
    Bot toBot();
}
