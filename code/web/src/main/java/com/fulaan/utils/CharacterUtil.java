package com.fulaan.utils;

/**
 * Created by wang_xinxin on 2015/8/3.
 */
public class CharacterUtil {

    public static String CharacterEscape(String message) {
        if (message == null) {
            return null;
        }
        char content[] = new char[message.length()];
        message.getChars(0, message.length(), content, 0);
        StringBuffer result = new StringBuffer(content.length + 100);
        for (int i = 0; i < content.length; i++) {
            switch (content[i]) {
                case '<':
                    result.append("&lt;");
                    break;
                case '>':
                    result.append("&gt;");
                    break;
                default:
                    result.append(content[i]);
                    break;
            }
        }
        return result.toString();
    }
}
