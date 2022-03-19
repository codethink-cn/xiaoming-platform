package cn.codethink.xiaoming.platform.console;

import cn.codethink.xiaoming.platform.PlatformObject;

import java.io.IOException;

/**
 * 控制台服务
 *
 * @author Chuanwise
 */
public interface ConsoleService
    extends PlatformObject, AutoCloseable {
    
    /**
     * 获取控制台会话
     *
     * @return 控制台会话
     */
    Console getConsole();
    
    /**
     * 启动控制台服务
     */
    void open() throws IOException;
    
    /**
     * 关闭控制台服务
     */
    @Override
    void close();
}
