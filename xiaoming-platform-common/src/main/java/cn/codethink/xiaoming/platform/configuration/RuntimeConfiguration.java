package cn.codethink.xiaoming.platform.configuration;

import cn.codethink.xiaoming.platform.file.StorableFile;
import lombok.Data;

import java.io.File;

/**
 * 运行时设置
 *
 * 虽然是 {@link StorableFile} 的子类，但一般不从文件中加载
 *
 * @author Chuanwise
 */
@Data
public class RuntimeConfiguration
    extends StorableFile {
    
    /**
     * 工作目录
     */
    
}
