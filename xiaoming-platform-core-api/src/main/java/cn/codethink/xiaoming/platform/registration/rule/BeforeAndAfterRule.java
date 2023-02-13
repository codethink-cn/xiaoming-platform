package cn.codethink.xiaoming.platform.registration.rule;

import cn.codethink.xiaoming.platform.api.APIFactory;

/**
 * <h1>排序规则</h1>
 *
 * @author Chuanwise
 */
public interface BeforeAndAfterRule
    extends Rule {
    
    /**
     * 构造排序规则
     *
     * @param beforeOneName 前者名
     * @param afterOneName 后者名
     * @return 排序规则
     */
    static BeforeAndAfterRule of(String beforeOneName, String afterOneName) {
        return APIFactory.getInstance().getBeforeAndAfterRule(beforeOneName, afterOneName);
    }
    
    /**
     * 获取前者名
     *
     * @return 前者名
     */
    String getBeforeOneName();
    
    /**
     * 获取后者名
     *
     * @return 后者名
     */
    String getAfterOneName();
    
    /**
     * <h1>排序规则严格程度</h1>
     *
     * <p>严格性用于表示一条规则 {@link Rule} 的严格程度。</p>
     *
     * @author Chuanwise
     */
    enum Level {
        
        /**
         * 宽松
         */
        LOOSE,
        
        /**
         * 普通
         */
        NORMAL,
        
        /**
         * 严格
         */
        STRICT,
        
        /**
         * 紧挨
         */
        TIGHT,
    }
    
    Level getLevel();
}
