package com.yoursway.utils;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

public class YsDebugging {
    
    public static String escape(String source) {
        if (source == null)
            return "<null>";
        try {
            StringWriter writer = new StringWriter(source.length() * 2);
            writer.append('"');
            escapeJavaStyleString(writer, source);
            writer.append('"');
            return writer.toString();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
    }
    
    private static void escapeJavaStyleString(Writer out, String str) throws IOException {
        int sz = str.length();
        for (int i = 0; i < sz; i++) {
            char ch = str.charAt(i);

            if (ch < 32) {
                switch (ch) {
                    case '\b':
                        out.write('\\');
                        out.write('b');
                        break;
                    case '\n':
                        out.write('\\');
                        out.write('n');
                        break;
                    case '\t':
                        out.write('\\');
                        out.write('t');
                        break;
                    case '\f':
                        out.write('\\');
                        out.write('f');
                        break;
                    case '\r':
                        out.write('\\');
                        out.write('r');
                        break;
                    default :
                        if (ch > 0xf) {
                            out.write("\\u00" + hex(ch));
                        } else {
                            out.write("\\u000" + hex(ch));
                        }
                        break;
                }
            } else {
                switch (ch) {
                    case '"':
                        out.write('\\');
                        out.write('"');
                        break;
                    case '\\':
                        out.write('\\');
                        out.write('\\');
                        break;
                    default :
                        out.write(ch);
                        break;
                }
            }
        }
    }
    
    private static String hex(char ch) {
        return Integer.toHexString(ch).toUpperCase();
    }
    
}
