package cn.codethink.xiaoming.platform.util;

import cn.codethink.common.util.StaticUtilities;

/**
 * 小明 Logo
 *
 * @author Chuanwise
 */
public class Logo
    extends StaticUtilities {
    
    /**
     * 字符画 Logo
     */
    public final static String ASCII_LOGO =
        "   ___   ___  __       ___        ______   .___  ___.  __  .__   __.   _______ \n" +
            "   \\  \\ /  / |  |     /   \\      /  __  \\  |   \\/   | |  | |  \\ |  |  /  _____|\n" +
            "    \\  V  /  |  |    /  ^  \\    |  |  |  | |  \\  /  | |  | |   \\|  | |  |  __  \n" +
            "     >   <   |  |   /  /_\\  \\   |  |  |  | |  |\\/|  | |  | |  . `  | |  | |_ | \n" +
            "    /  .  \\  |  |  /  _____  \\  |  `--'  | |  |  |  | |  | |  |\\   | |  |__| | \n" +
            "   /__/ \\__\\ |__| /__/     \\__\\  \\______/  |__|  |__| |__| |__| \\__|  \\______| \n" +
            "                                                                               ";
    
    /**
     * 字符画 Logo 宽度
     */
    public static final int ASCII_WIDTH;
    
    static {
        ASCII_WIDTH = ASCII_LOGO.split("\n")[0].length();
    }
}
