package cn.codethink.xiaoming.platform.console;

import cn.chuanwise.common.util.Arrays;
import cn.codethink.common.util.Preconditions;
import lombok.Getter;
import org.jline.reader.LineReader;
import org.jline.utils.InfoCmp;

import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * 控制台
 *
 * @author Chuanwise
 */
public class JlineConsoleOutputStream
    extends OutputStream {
    
    protected final List<Integer> characters = new ArrayList<>();
    
    @Getter
    protected final LineReader reader;
    
    public JlineConsoleOutputStream(LineReader reader) {
        Preconditions.argumentNonNull(reader);
        
        this.reader = reader;
    }
    
    @Override
    public void write(int b) {
        if (b == '\n') {
            this.characters.add(b);
            
            final Integer[] characters = this.characters.toArray(new Integer[0]);
            final int[] ints = Arrays.unbox(characters);
            
            final byte[] bytes = new byte[ints.length];
            for (int i = 0; i < ints.length; i++) {
                bytes[i] = (byte) ints[i];
            }
            
            final PrintWriter writer = reader.getTerminal().writer();
            final String line = new String(bytes);
            this.characters.clear();
            
            writer.write(process(line));
            if (reader.isReading()) {
                reader.callWidget(LineReader.REDRAW_LINE);
                reader.callWidget(LineReader.REDISPLAY);
            }
            writer.flush();
        } else {
            if (characters.isEmpty()) {
                reader.getTerminal().puts(InfoCmp.Capability.carriage_return);
            }
            characters.add(b);
        }
    }
    
    protected String process(String line) {
        return line;
    }
}
