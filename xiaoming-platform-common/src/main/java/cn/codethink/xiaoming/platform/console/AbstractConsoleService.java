package cn.codethink.xiaoming.platform.console;

import cn.codethink.xiaoming.platform.Platform;
import lombok.Getter;

import java.util.Objects;

/**
 * @see cn.codethink.xiaoming.platform.console.ConsoleService
 * @author Chuanwise
 */
@Getter
public abstract class AbstractConsoleService
    implements ConsoleService {
    
    protected Platform platform;
    
    protected Console console;
    
    @Override
    public Console getConsole() {
        if (Objects.isNull(console)) {
            console = new Console(platform);
        }
        return console;
    }
    
    @Override
    public void open() {
    }
    
    @Override
    public void close() {
    }
}
