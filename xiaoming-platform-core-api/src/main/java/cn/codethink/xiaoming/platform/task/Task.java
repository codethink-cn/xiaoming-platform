package cn.codethink.xiaoming.platform.task;

import cn.codethink.xiaoming.platform.task.action.Action;

/**
 * <h1>任务</h1>
 *
 * <p>任务是一段代码，其执行由小明平台调度。</p>
 *
 * @author Chuanwise
 */
public interface Task {
    
    /**
     * 判断任务是否是持久化的
     *
     * @return 任务是否是持久化的
     */
    boolean isPersistent();
    
    /**
     * 获取行动
     *
     * @return 行动
     */
    Action getAction();
    
    /**
     * 设置行动
     *
     * @param action 行动
     */
    void setAction(Action action);
}
