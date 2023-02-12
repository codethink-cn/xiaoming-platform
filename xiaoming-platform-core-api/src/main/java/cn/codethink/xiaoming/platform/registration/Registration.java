package cn.codethink.xiaoming.platform.registration;

/**
 * <h1>注册</h1>
 *
 * <p>注册的某种对象</p>
 *
 * @author Chuanwise
 */
public interface Registration
    extends SubjectObject {
    
    /**
     * 获取注册签名
     *
     * @return 注册签名
     */
    String getSignature();
}
