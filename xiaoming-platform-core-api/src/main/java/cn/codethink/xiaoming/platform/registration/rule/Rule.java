package cn.codethink.xiaoming.platform.registration.rule;

/**
 * <h1>依赖关系规则</h1>
 *
 * @author Chuanwise
 */
public interface Rule {
    
    /**
     * 获取规则名
     *
     * @return 规则名
     */
    String getName();
    
    @Override
    String toString();
}
