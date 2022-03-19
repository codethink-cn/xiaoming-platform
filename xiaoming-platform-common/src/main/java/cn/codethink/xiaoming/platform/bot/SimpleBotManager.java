package cn.codethink.xiaoming.platform.bot;

import cn.codethink.xiaoming.Bot;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @see cn.codethink.xiaoming.platform.bot.BotManager
 * @author Chuanwise
 */
public class SimpleBotManager
    implements BotManager {
    
    protected final List<Bot> bots = new CopyOnWriteArrayList<>();
    
    @Override
    public List<Bot> getBots() {
        return Collections.unmodifiableList(bots);
    }
    
    @Override
    public boolean addBot(Bot bot) {
        return cn.codethink.common.util.Collections.addDistinctly(bots, bot);
    }
    
    @Override
    public boolean removeBot(Bot bot) {
        return bots.remove(bot);
    }
}
