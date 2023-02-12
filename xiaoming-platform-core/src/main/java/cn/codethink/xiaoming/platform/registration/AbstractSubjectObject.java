package cn.codethink.xiaoming.platform.registration;

import cn.codethink.common.util.Preconditions;
import cn.codethink.xiaoming.platform.Platform;
import cn.codethink.xiaoming.platform.object.AbstractPlatformObject;

import java.util.Objects;

public abstract class AbstractSubjectObject
    implements SubjectObject {
    
    private final Subject subject;
    
    public AbstractSubjectObject(Subject subject) {
        Preconditions.objectNonNull(subject, "Subject");
        
        this.subject = subject;
    }
    
    @Override
    public Subject getSubject() {
        return subject;
    }
    
    @Override
    public Platform getPlatform() {
        return subject.getPlatform();
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
        final AbstractSubjectObject that = (AbstractSubjectObject) object;
        return Objects.equals(subject, that.subject);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(subject);
    }
}
