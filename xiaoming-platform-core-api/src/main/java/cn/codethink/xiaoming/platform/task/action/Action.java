package cn.codethink.xiaoming.platform.task.action;

import cn.codethink.xiaoming.platform.api.APIFactory;

/**
 * <h1>行动</h1>
 *
 * <p>行动是一段代码，做某件事情。与代码执行的时间、执行的次数等无关。</p>
 *
 * @author Chuanwise
 */
@FunctionalInterface
public interface Action {
    
    /**
     * 获取一个空行动
     *
     * @return 空行动
     */
    static Action empty() {
        return APIFactory.getInstance().getEmptyAction();
    }
    
    /**
     * 执行行动
     *
     * @param actionContext 行动环境
     * @throws Exception 行动时抛出的异常
     */
    void action(ActionContext actionContext) throws Exception;
}
