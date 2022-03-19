package cn.codethink.xiaoming.platform.configuration;

import cn.codethink.xiaoming.platform.file.StorableFile;
import lombok.Data;

import java.nio.charset.Charset;

/**
 * 序列化设置
 *
 * @author Chuanwise
 */
@Data
public class FileServiceConfiguration
    extends StorableFile {
    
    /**
     * 编码
     */
    protected Charset charset = Charset.defaultCharset();
    
    /**
     * 是否启用自动注解
     */
    protected boolean enableComment = true;
    

}
