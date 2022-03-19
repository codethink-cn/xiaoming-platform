package cn.codethink.xiaoming.platform.bot;

import cn.codethink.xiaoming.Bot;

import java.util.List;

/**
 * 账号管理器
 *
 * @author Chuanwise
 */
public interface BotManager {
    
    /**
     * 获取所有 Bot
     *
     * @return 所有 Bot
     */
    List<Bot> getBots();
    
    /**
     * 添加 Bot
     *
     * @param bot Bot
     */
    boolean addBot(Bot bot);
    
    /**
     * 删除 Bot
     *
     * @param bot Bot
     */
    boolean removeBot(Bot bot);
}
