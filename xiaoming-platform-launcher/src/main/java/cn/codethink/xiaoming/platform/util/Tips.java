package cn.codethink.xiaoming.platform.util;

import cn.codethink.common.util.Arrays;
import cn.codethink.common.util.StaticUtilities;

import java.util.List;
import java.util.Random;

/**
 * 启动小贴士
 *
 * @author Chuanwise
 */
public class Tips
    extends StaticUtilities {
    
    /**
     * 所有启动小贴士
     */
    public static final List<String> TIPS = Arrays.asUnmodifiableList(
        "你知道吗？每呼吸一分钟，就过去了60秒。",
        "这个点还没有休息的人，一定还没有休息吧。",
        "你知道吗？当你看到这条 TIP，你就看到了一条 TIP。",
        "小明最初只是椽子为了满足个人需要编写的机器人。",
        "你知道吗？第一批使用小明的用户大部分是 Minecraft 玩家。",
        "你知道吗？椽子为了编写启动 TIPS，专门去翻阅了废话文学。",
        "谁能想到当我们还是一个孩子的时候，我们还只是一个孩子！",
        "小明曾经被重构过两次，第一次重构出了插件化框架，第二次认真设计了结构。"
    );
    
    private static final Random RANDOM = new Random();
    
    /**
     * 随机选择一个小贴士
     *
     * @return 小贴士
     */
    public static String select() {
        return TIPS.get(RANDOM.nextInt(TIPS.size()));
    }
}
