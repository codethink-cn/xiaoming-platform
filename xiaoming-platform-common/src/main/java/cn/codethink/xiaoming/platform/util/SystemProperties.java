package cn.codethink.xiaoming.platform.util;

import cn.codethink.common.util.StaticUtilities;
import cn.codethink.xiaoming.platform.logger.AlignmentPosition;
import cn.codethink.xiaoming.platform.logger.AlignmentType;
import cn.codethink.xiaoming.platform.property.*;

import java.io.File;

/**
 * 系统属性
 *
 * @author Chuanwise
 */
public class SystemProperties
    extends StaticUtilities {
    
    /**
     * 日志日期类型
     */
    public static final SystemProperty<String> LOGGER_DATE_FORMAT = new StringSystemProperty("xiaoming.platform.logger.dateFormat");
    
    /**
     * 日志线程名居中类型
     */
    public static final SystemProperty<AlignmentType> LOGGER_THREAD_ALIGNMENT_TYPE =
        new EnumSystemProperty<>(AlignmentType.values(), "xiaoming.platform.logger.thread.alignment.type");
    
    /**
     * 日志名居中类型
     */
    public static final SystemProperty<AlignmentType> LOGGER_NAME_ALIGNMENT_TYPE =
        new EnumSystemProperty<>(AlignmentType.values(), "xiaoming.platform.logger.name.alignment.type");
    
    /**
     * 日志等级居中类型
     */
    public static final SystemProperty<AlignmentType> LOGGER_LEVEL_ALIGNMENT_TYPE =
        new EnumSystemProperty<>(AlignmentType.values(), "xiaoming.platform.logger.level.alignment.type");
    
    /**
     * 日志线程名居中位置
     */
    public static final SystemProperty<AlignmentPosition> LOGGER_THREAD_ALIGNMENT_POSITION =
        new EnumSystemProperty<>(AlignmentPosition.values(), "xiaoming.platform.logger.thread.alignment.position");
    
    /**
     * 是否最小化日志填充空格
     */
    public static final SystemProperty<Boolean> LOGGER_MINIMIZE_ALIGNMENT_SPACES =
        new BooleanSystemProperty("xiaoming.platform.logger.space.minimize");
    
    /**
     * 是否同意最终用户许可协议
     */
    public static final SystemProperty<Boolean> EULA =
        new BooleanSystemProperty("xiaoming.platform.eula");
    
    /**
     * 平台工作目录
     */
    public static final SystemProperty<File> PLATFORM_WORKING_DIRECTORY = new SimpleSystemProperty<>("xiaoming.platform.workingDirectory",
        File::new);
}
