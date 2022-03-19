package cn.codethink.xiaoming.platform.logger;

import cn.codethink.common.util.StaticUtilities;
import org.apache.log4j.PropertyConfigurator;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * 平台日志配置器
 *
 * @author Chuanwise
 */
public class PlatformLoggerConfiguration
    extends StaticUtilities {
    
    /**
     * 调试日志配置
     */
    private static final String PROPERTIES =
        "log4j.rootLogger=INFO,CONSOLE,FILE\n" +
        "\n" +
        "log4j.appender.CONSOLE=org.apache.log4j.ConsoleAppender\n" +
        "log4j.appender.CONSOLE.layout=cn.codethink.xiaoming.platform.logger.ColorfulPatternLayout\n" +
        "\n" +
        "log4j.appender.FILE=org.apache.log4j.FileAppender\n" +
        "log4j.appender.FILE.file=${xiaoming.platform.workingDir}/logs/${xiaoming.platform.log}.log\n" +
        "log4j.appender.FILE.encoding=UTF-8\n" +
        "log4j.appender.FILE.append=false\n" +
        "log4j.appender.FILE.layout=org.apache.log4j.PatternLayout\n" +
        "log4j.appender.FILE.layout.ConversionPattern=[%d{yyyy-MM-dd HH:mm:ss}] [%t] %c | %p : %m%n";
    
    /**
     * 使用调试日志设置当前日志环境
     */
    public static void install() {
        try (InputStream inputStream = new ByteArrayInputStream(PROPERTIES.getBytes())) {
            PropertyConfigurator.configure(inputStream);
        } catch (IOException ignored) {
        }
    }
    
}
