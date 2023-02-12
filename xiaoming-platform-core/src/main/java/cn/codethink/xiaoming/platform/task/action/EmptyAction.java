package cn.codethink.xiaoming.platform.task.action;

public class EmptyAction
    implements Action {
    
    private static final EmptyAction INSTANCE = new EmptyAction();
    
    private EmptyAction() {
    }
    
    public static EmptyAction getInstance() {
        return INSTANCE;
    }
    
    @Override
    public void action(ActionContext actionContext) throws Exception {
    }
}
