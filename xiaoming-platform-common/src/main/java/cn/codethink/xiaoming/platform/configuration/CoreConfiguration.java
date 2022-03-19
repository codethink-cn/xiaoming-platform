package cn.codethink.xiaoming.platform.configuration;

import cn.codethink.xiaoming.platform.file.StorableFile;
import lombok.Data;

/**
 * 核心配置
 *
 * @author Chuanwise
 */
@Data
public class CoreConfiguration
    extends StorableFile {
    
    /**
     * 核心线程池大小
     */
    protected int threadCount = 20;
    
    /**
     * 显示事件日志
     */
    protected boolean logEvents = true;
    
    /**
     * 是否启动调试
     */
    protected boolean debug = false;
}
