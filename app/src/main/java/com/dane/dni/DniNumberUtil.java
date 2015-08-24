package com.dane.dni;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Dane on 8/23/2015.
 */
public class DniNumberUtil {

    private static Map<Character, Character> DIGIT_MAP;

    static {
        DIGIT_MAP = new HashMap<Character, Character>();
        DIGIT_MAP.put('a', ')');
        DIGIT_MAP.put('b', '!');
        DIGIT_MAP.put('c', '@');
        DIGIT_MAP.put('d', '#');
        DIGIT_MAP.put('e', '$');
        DIGIT_MAP.put('f', '%');
        DIGIT_MAP.put('g', '^');
        DIGIT_MAP.put('h', '&');
        DIGIT_MAP.put('i', '*');
        DIGIT_MAP.put('j', '(');
        DIGIT_MAP.put('k', '[');
        DIGIT_MAP.put('l', ']');
        DIGIT_MAP.put('m', '{');
        DIGIT_MAP.put('n', '}');
        DIGIT_MAP.put('o', '\\');
        DIGIT_MAP.put('p', '|');
    }


    public static String convertToDni(long val) {
        String base25 = Long.toString(val, 25);
        String rv = base25;
        for (Map.Entry<Character, Character> mapping : DIGIT_MAP.entrySet()) {
            rv = rv.replace(mapping.getKey(), mapping.getValue());
        }
        return rv;
    }

}
