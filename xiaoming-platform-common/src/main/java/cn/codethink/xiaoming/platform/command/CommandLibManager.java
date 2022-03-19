package cn.codethink.xiaoming.platform.command;

import cn.chuanwise.commandlib.CommandLib;
import cn.chuanwise.commandlib.handler.CommandSenderHandler;
import cn.codethink.xiaoming.contact.Contact;
import cn.codethink.xiaoming.platform.AbstractPlatformObject;
import cn.codethink.xiaoming.platform.Platform;
import cn.codethink.xiaoming.platform.plugin.Plugin;
import lombok.Data;

/**
 * 使用 command-lib 库实现的指令调度器
 *
 * @author Chuanwise
 */
@Data
@SuppressWarnings("all")
public class CommandLibManager
    extends AbstractPlatformObject
    implements CommandManager {
    
    protected final CommandLib commandLib = new CommandLib();
    
    public CommandLibManager(Platform platform) {
        super(platform);
        
        // command sender handler
        commandLib.pipeline().addLast(new CommandSenderHandler(Contact.class));
    }
    
    @Override
    public void unregisterPlugin(Plugin plugin) {
    
    }
}
