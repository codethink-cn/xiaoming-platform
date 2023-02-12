package cn.codethink.xiaoming.platform.api;

import cn.codethink.xiaoming.platform.annotation.PlatformInternalAPI;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.ServiceLoader;

/**
 * <h1>API 工厂</h1>
 *
 * <p>API 工厂是获取 {@link API} 实例的类。</p>
 *
 * @author Chuanwise
 */
@PlatformInternalAPI
public class APIFactory {
    private APIFactory() {
    }
    
    /**
     * API 的全局唯一实例
     */
    private static volatile API instance;
    
    public static API getInstance() {
        if (instance == null) {
            synchronized (APIFactory.class) {
                if (instance == null) {
                    final ServiceLoader<API> serviceLoader = ServiceLoader.load(API.class);
                    final Iterator<API> iterator = serviceLoader.iterator();
    
                    if (iterator.hasNext()) {
                        instance = iterator.next();
                    } else {
                        throw new NoSuchElementException("Could not load xiaoming-platform-core!");
                    }
                }
            }
        }
        return instance;
    }
}
