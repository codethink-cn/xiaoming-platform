package cn.codethink.xiaoming.platform.object;

import cn.codethink.common.util.Preconditions;
import cn.codethink.xiaoming.platform.Platform;

import java.util.Objects;

public abstract class AbstractPlatformObject
    implements PlatformObject {
    
    private final Platform platform;
    
    public AbstractPlatformObject(Platform platform) {
        Preconditions.objectNonNull(platform, "Platform");
        
        this.platform = platform;
    }
    
    @Override
    public Platform getPlatform() {
        return platform;
    }
    
    protected boolean canEqual(Object object) {
        return object instanceof AbstractPlatformObject;
    }
    
    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (!canEqual(object)) {
            return false;
        }
        final AbstractPlatformObject that = (AbstractPlatformObject) object;
        return Objects.equals(platform, that.platform);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(platform);
    }
}
