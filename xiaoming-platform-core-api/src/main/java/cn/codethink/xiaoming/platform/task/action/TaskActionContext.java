package cn.codethink.xiaoming.platform.task.action;

import cn.codethink.xiaoming.platform.task.Task;

/**
 * <h1>和任务对应的动作</h1>
 */
public interface TaskActionContext
    extends ActionContext {
    
    /**
     * 获取对应的任务
     *
     * @return 对应的任务
     */
    Task getTask();
}
