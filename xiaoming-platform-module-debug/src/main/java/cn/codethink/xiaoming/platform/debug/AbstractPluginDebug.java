package cn.codethink.xiaoming.platform.debug;

import cn.codethink.xiaoming.platform.Platform;
import org.fusesource.jansi.Ansi;

/**
 * @see cn.codethink.xiaoming.platform.debug.PluginDebug
 * @author Chuanwise
 */
public abstract class AbstractPluginDebug
    implements PluginDebug {
    
    private static final String ASCII_LOGO =
        "   ___   ___ .___  ___.                _______   _______ .______    __    __    _______ \n" +
        "   \\  \\ /  / |   \\/   |               |       \\ |   ____||   _  \\  |  |  |  |  /  _____|\n" +
        "    \\  V  /  |  \\  /  |     ______    |  .--.  ||  |__   |  |_)  | |  |  |  | |  |  __  \n" +
        "     >   <   |  |\\/|  |    |______|   |  |  |  ||   __|  |   _  <  |  |  |  | |  | |_ | \n" +
        "    /  .  \\  |  |  |  |               |  '--'  ||  |____ |  |_)  | |  `--'  | |  |__| | \n" +
        "   /__/ \\__\\ |__|  |__|               |_______/ |_______||______/   \\______/   \\______| \n" +
        "                                                                                        ";

    private static final int ASCII_WIDTH;
    
    static {
        ASCII_WIDTH = ASCII_LOGO.split("\n")[0].length();
    }
    
    @Override
    public final PluginDebug start() {
        printlnHead();
        return start0();
    }
    
    private void printlnHead() {
        System.out.println(
            Ansi.ansi()
                .fgBrightYellow()
                .a(ASCII_LOGO)
        );
    
        // println group and version
        System.out.println(Ansi.ansi()
            .fgGreen()
            .a("group: ")
            .a(Platform.GROUP)
        
            .fgBrightBlack()
            .a(", ")
            .fgBrightGreen()
        
            .a("platform-version: ")
            .a(Platform.VERSION)
            .a("-")
            .a(Platform.VERSION_BRANCH));
        System.out.println();
    }
    
    protected abstract PluginDebug start0();
}
