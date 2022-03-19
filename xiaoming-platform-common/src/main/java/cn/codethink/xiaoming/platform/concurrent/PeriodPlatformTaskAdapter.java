package cn.codethink.xiaoming.platform.concurrent;

/**
 * @see cn.codethink.xiaoming.platform.concurrent.PeriodPlatformTask
 * @author Chuanwise
 */
public class PeriodPlatformTaskAdapter
    extends PlatformTaskAdapter
    implements PeriodPlatformTask {
    
    public PeriodPlatformTaskAdapter(PlatformTask task) {
        super(task);
    }
    
    @Override
    public boolean skip() {
        return ((PeriodPlatformTask) task).skip();
    }
    
    @Override
    public boolean isSkipping() {
        return ((PeriodPlatformTask) task).isSkipping();
    }
}
