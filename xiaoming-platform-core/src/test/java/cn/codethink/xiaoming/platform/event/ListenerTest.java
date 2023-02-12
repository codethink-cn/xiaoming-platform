package cn.codethink.xiaoming.platform.event;

import cn.codethink.xiaoming.platform.annotation.Asynchronous;
import cn.codethink.xiaoming.platform.annotation.Listener;
import org.junit.jupiter.api.Test;

public class ListenerTest {
    
    private class Listeners {
        
        @Listener
        @Asynchronous
        public void onBuildInEvent(BuiltInEvent event) {
        
        }
    }
    
    @Test
    public void testSynchronousCallback() {
    
    }
}
