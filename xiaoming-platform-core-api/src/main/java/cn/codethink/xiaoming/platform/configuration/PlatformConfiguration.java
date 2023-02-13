package cn.codethink.xiaoming.platform.configuration;

public interface PlatformConfiguration {
    
    interface RegistrationConfiguration {
    
        /**
         * 获取最大自旋长度。
         * 最大自旋长度是在注册时尝试获取受影响的节点的锁时最大的自旋等待长度。
         * 超过这一长度仍无法获取锁，则将锁升级为重型锁。
         *
         * @return 最大自旋长度
         */
        long getMaxSpinTime();
    }
}
