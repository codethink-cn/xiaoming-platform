package cn.codethink.xiaoming.platform.logger;

import cn.codethink.common.util.Strings;
import cn.codethink.xiaoming.platform.util.SystemProperties;
import lombok.Data;
import org.apache.log4j.Category;
import org.apache.log4j.Level;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.spi.LoggingEvent;
import org.fusesource.jansi.Ansi;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.NoSuchElementException;

/**
 * 彩色模式布局
 *
 * @author Chuanwise
 */
public class ColorfulPatternLayout
    extends PatternLayout {
    
    private final Boolean minimizeAlignmentSpaces;
    
    /**
     * 日志日期格式
     */
    protected final DateFormat dateFormat;
    
    @SuppressWarnings("all")
    protected Ansi getMessageColor(int levelInt) {
        switch (levelInt) {
            case Level.TRACE_INT:
                return Ansi.ansi().fgBrightBlack();
            case Level.DEBUG_INT:
                return Ansi.ansi().fgBrightBlue();
            case Level.INFO_INT:
                return Ansi.ansi().reset();
            case Level.ERROR_INT:
                return Ansi.ansi().fgBrightRed();
            case Level.FATAL_INT:
                return Ansi.ansi().fgBrightMagenta();
            default:
            case Level.WARN_INT:
                return Ansi.ansi().fgBrightYellow();
        }
    }
    
    @SuppressWarnings("all")
    protected Ansi getLevelColor(int levelInt) {
        switch (levelInt) {
            case Level.TRACE_INT:
                return Ansi.ansi().fgBlack();
            case Level.DEBUG_INT:
                return Ansi.ansi().fgBlue();
            case Level.INFO_INT:
                return Ansi.ansi().fgCyan();
            case Level.ERROR_INT:
                return Ansi.ansi().fgRed();
            case Level.FATAL_INT:
                return Ansi.ansi().fgMagenta();
            default:
            case Level.WARN_INT:
                return Ansi.ansi().fgYellow();
        }
    }
    
    /**
     * 日志名对齐方案
     */
    private QuotedAlignment threadNameAlignment = new QuotedAlignment(
        SystemProperties.LOGGER_THREAD_ALIGNMENT_TYPE.get(AlignmentType.LEFT),
        SystemProperties.LOGGER_THREAD_ALIGNMENT_POSITION.get(AlignmentPosition.INTERNAL)
    );
    
    /**
     * 日志等级对齐方案
     */
    private SimpleAlignment loggerLevelAlignment = new SimpleAlignment(
        SystemProperties.LOGGER_LEVEL_ALIGNMENT_TYPE.get(AlignmentType.LEFT)
    );
    
    /**
     * 日志名对齐方案
     */
    private SimpleAlignment loggerNameAlignment = new SimpleAlignment(
        SystemProperties.LOGGER_NAME_ALIGNMENT_TYPE.get(AlignmentType.RIGHT)
    );
    
    public ColorfulPatternLayout() {
        dateFormat = new SimpleDateFormat(SystemProperties.LOGGER_DATE_FORMAT.get("yyyy-MM-dd HH:mm:ss"));
    
        minimizeAlignmentSpaces = SystemProperties.LOGGER_MINIMIZE_ALIGNMENT_SPACES.get(true);
    }
    
    private class Spacer {
        
        private int maxWidth;
        
        public int spaceCount(String value) {
            final int length = value.length();
            maxWidth = Math.max(maxWidth, length);
            
            int spaceCount = maxWidth - length;
            final int halfLength = length / 2;
            while (minimizeAlignmentSpaces && spaceCount >= halfLength) {
                spaceCount /= 2;
                maxWidth /= 2;
            }
            
            return spaceCount;
        }
    }
    
    private String minimizeLoggerName(String loggerName) {
        int index = -1;
        final int length = loggerName.length();
        final StringBuilder stringBuilder = new StringBuilder();
    
        for (int i = 0; i < length; i++) {
            final char ch = loggerName.charAt(i);
            if (ch == '.') {
                index = i;
            } else if (index == i - 1) {
                if (i != 0) {
                    stringBuilder.append('.');
                }
                stringBuilder.append(ch);
            }
        }
        
        stringBuilder.append(loggerName.substring(index + 2));
        
        return stringBuilder.toString();
    }
    
    @Data
    private class QuotedAlignment {
        
        private final Spacer spacer = new Spacer();
        
        private final AlignmentType type;
    
        private final AlignmentPosition position;
        
        public Ansi format(Ansi ansi, String value) {
            final int spaceCount = spacer.spaceCount(value);
    
            switch (position) {
                case EXTERNAL: {
                    switch (type) {
                        case LEFT: {
                            final String spaces = Strings.repeat(" ", spaceCount);
                            ansi = ansi.fgBrightBlack()
                                .a("[")
                                .a(value)
                                .a("]")
                                .a(spaces);
                            break;
                        }
                        case RIGHT: {
                            final String spaces = Strings.repeat(" ", spaceCount);
                            ansi = ansi.a(spaces)
                                .fgBrightBlack()
                                .a("[")
                                .a(value)
                                .a("]");
                            break;
                        }
                        case CENTER: {
                            final String spaces = Strings.repeat(" ", spaceCount / 2);
                            ansi = ansi.a(spaces)
                                .fgBrightBlack()
                                .a("[")
                                .a(value)
                                .a("]")
                                .a(spaces);
                            break;
                        }
                        default:
                            throw new IllegalStateException();
                    }
                    break;
                }
                case INTERNAL: {
                    switch (type) {
                        case LEFT: {
                            final String spaces = Strings.repeat(" ", spaceCount);
                            ansi = ansi.fgBrightBlack()
                                .a("[")
                                .a(value)
                                .a(spaces)
                                .a("]");
                            break;
                        }
                        case RIGHT: {
                            final String spaces = Strings.repeat(" ", spaceCount);
                            ansi = ansi.fgBrightBlack()
                                .a("[")
                                .a(spaces)
                                .a(value)
                                .a("]");
                            break;
                        }
                        case CENTER: {
                            final String spaces = Strings.repeat(" ", spaceCount / 2);
                            ansi = ansi.fgBrightBlack()
                                .a("[")
                                .a(spaces)
                                .a(value)
                                .a(spaces)
                                .a("]");
                            break;
                        }
                        default:
                            throw new IllegalStateException();
                    }
                    break;
                }
                default:
                    throw new IllegalStateException();
            }
            
            return ansi;
        }
    }
    
    @Data
    private class SimpleAlignment {
    
        private final Spacer spacer = new Spacer();
        
        private final AlignmentType type;
    
        public Ansi format(Ansi ansi, String value) {
            final int spaceCount = spacer.spaceCount(value);
    
            switch (type) {
                case LEFT: {
                    final String spaces = Strings.repeat(" ", spaceCount);
                    ansi = ansi.a(value).a(spaces);
                    break;
                }
                case RIGHT: {
                    final String spaces = Strings.repeat(" ", spaceCount);
                    ansi = ansi.a(spaces).a(value);
                    break;
                }
                case CENTER: {
                    final String spaces = Strings.repeat(" ", spaceCount / 2);
                    ansi = ansi.a(spaces).a(value).a(spaces);
                    break;
                }
                default:
                    throw new NoSuchElementException();
            }
            
            return ansi;
        }
    }
    
    @Override
    public String format(LoggingEvent event) {
        final Category logger = event.getLogger();
        final Level level = event.getLevel();
        final int levelInt = level.toInt();
        
        final String threadName = Thread.currentThread().getName();
        final String loggerName = minimizeLoggerName(logger.getName());
        final String loggerLevelName = event.getLevel().toString();
    
        Ansi ansi = Ansi.ansi()
            .fgBrightBlack()
            .a("[")
            .a(dateFormat.format(System.currentTimeMillis()))
            .a("]")
            .a(" ");
    
        // thread name
        ansi = threadNameAlignment.format(ansi, threadName);
        ansi = ansi.a(" ");
        
        // logger name
        ansi = ansi.reset();
        ansi = loggerNameAlignment.format(ansi, loggerName);
        ansi = ansi.fgBrightBlack().a(" | ");
    
        // level name
        ansi = ansi.a(getLevelColor(levelInt));
        ansi = loggerLevelAlignment.format(ansi, loggerLevelName);
        ansi = ansi.fgBrightBlack().a(" : ");
    
        // message
        ansi = ansi.a(getMessageColor(levelInt))
            .a(event.getMessage())
            .reset()
            .a("\n");
        
        return ansi.toString();
    }
}
