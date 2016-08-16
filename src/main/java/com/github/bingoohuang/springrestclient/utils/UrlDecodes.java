package com.github.bingoohuang.springrestclient.utils;

import com.google.common.base.Charsets;
import lombok.experimental.UtilityClass;
import lombok.val;

@UtilityClass
public class UrlDecodes {
    public String decodeQuietly(String s) {
        boolean needToChange = false;
        int numChars = s.length();
        val sb = new StringBuffer(numChars > 500 ? numChars / 2 : numChars);
        int i = 0;

        char c;
        byte[] bytes = null;
        while (i < numChars) {
            c = s.charAt(i);
            switch (c) {
                case '+':
                    sb.append(' ');
                    i++;
                    needToChange = true;
                    break;
                case '%':
                /*
                 * Starting with this instance of %, process all
                 * consecutive substrings of the form %xy. Each
                 * substring %xy will yield a byte. Convert all
                 * consecutive  bytes obtained this way to whatever
                 * character(s) they represent in the provided
                 * encoding.
                 */

                    // (numChars-i)/3 is an upper bound for the number
                    // of remaining bytes
                    if (bytes == null)
                        bytes = new byte[(numChars - i) / 3];
                    int pos = 0;

                    while (((i + 2) < numChars) &&
                        (c == '%')) {
                        int v = parseInt(s.substring(i + 1, i + 3), 16);
                        if (v < 0) {
                            sb.append('%').append(s.substring(i + 1, i + 3));
                        } else {
                            bytes[pos++] = (byte) v;
                        }
                        i += 3;
                        if (i < numChars)
                            c = s.charAt(i);
                    }

                    // A trailing, incomplete byte encoding such as
                    // "%x" will cause an exception to be thrown

                    if ((i < numChars) && (c == '%')) {
                        sb.append('%').append(s.substring(i + 1));
                    } else {
                        sb.append(new String(bytes, 0, pos, Charsets.UTF_8));
                    }
                    needToChange = true;
                    break;
                default:
                    sb.append(c);
                    i++;
                    break;
            }
        }

        return (needToChange ? sb.toString() : s);
    }

    private int parseInt(String str, int base) {
        try {
            return Integer.parseInt(str, base);
        } catch (NumberFormatException e) {
            return -1;
        }
    }
}
