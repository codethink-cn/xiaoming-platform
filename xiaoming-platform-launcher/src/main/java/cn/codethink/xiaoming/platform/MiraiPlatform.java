package cn.codethink.xiaoming.platform;

import cn.codethink.xiaoming.InstantMessenger;
import cn.codethink.xiaoming.platform.configuration.PlatformConfiguration;

/**
 * @see Platform
 * @author Chuanwise
 */
public class MiraiPlatform
    extends AbstractPlatform {
    
    static {
        // register driver
        Platforms.registerDriver(InstantMessenger.QQ, MiraiPlatform::new);
    }
    
    public MiraiPlatform(PlatformConfiguration platformConfiguration) {
        super(platformConfiguration);
    }
    
    public MiraiPlatform() {
        super();
    }
    
    @Override
    protected void setupPlatformConfiguration(PlatformConfiguration platformConfiguration) {
    }
}
