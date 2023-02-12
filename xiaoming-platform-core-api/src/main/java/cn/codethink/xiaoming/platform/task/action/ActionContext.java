package cn.codethink.xiaoming.platform.task.action;

import cn.codethink.xiaoming.platform.registration.SubjectObject;
import cn.codethink.xiaoming.platform.task.Task;

/**
 * <h1>行动环境</h1>
 *
 * <p>行动环境说明了行为所属的任务、执行事件等等。</p>
 *
 * @author Chuanwise
 */
public interface ActionContext
    extends SubjectObject {
    
    /**
     * 获取行动
     *
     * @return 行动
     */
    Action getAction();
    
    /**
     * 获取对应的任务
     *
     * @return 对应的任务
     */
    Task getTask();
}
