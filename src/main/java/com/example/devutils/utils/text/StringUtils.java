package com.example.devutils.utils.text;

import java.util.Arrays;
import java.util.Collection;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by AMe on 2020-06-09 13:09.
 */
public class StringUtils {

    public static boolean isBlank(String str) {
        return !isNotBlank(str);
    }

    public static boolean isNotBlank(String str) {
        return str != null && !"".equals(str.trim());
    }

    public static String capitalize(final String str) {
        int strLen;
        if (str == null || (strLen = str.length()) == 0) {
            return str;
        }

        final int firstCodepoint = str.codePointAt(0);
        final int newCodePoint = Character.toTitleCase(firstCodepoint);
        if (firstCodepoint == newCodePoint) {
            // already capitalized
            return str;
        }

        final int newCodePoints[] = new int[strLen]; // cannot be longer than the char array
        int outOffset = 0;
        newCodePoints[outOffset++] = newCodePoint; // copy the first codepoint
        for (int inOffset = Character.charCount(firstCodepoint); inOffset < strLen; ) {
            final int codepoint = str.codePointAt(inOffset);
            newCodePoints[outOffset++] = codepoint; // copy the remaining ones
            inOffset += Character.charCount(codepoint);
        }
        return new String(newCodePoints, 0, outOffset);
    }

    public static String repeat(String str, int cycle, String separator) {
        int length = str.concat(separator).length() * Math.max(cycle, 0);
        StringBuilder stringBuilder = new StringBuilder(length);
        for (int i = 0; i < cycle; i++) {
            stringBuilder.append(str);
            if (i < cycle - 1) {
                stringBuilder.append(separator);
            }
        }
        return stringBuilder.toString();
    }

    /**
     * 字符串序列拼接
     * @param delimiter 分隔符
     * @param prefix 前缀
     * @param suffix 后缀
     * @param alwaysPreAndSuf 是否总是拼接前缀及后缀
     * @param stream 字符串序列
     * @return 拼接后的字符串
     */
    public static String charSequenceJoin(String delimiter, String prefix, String suffix, boolean alwaysPreAndSuf, Stream<String> stream) {
        String join = stream.filter(item -> item != null && !"".equals(item.trim())).collect(Collectors.joining(delimiter));
        if (alwaysPreAndSuf || !"".equals(join)) {
            join =  prefix + join + suffix;
        }
        return join;
    }

    public static String charSequenceJoin(String delimiter, String prefix, String suffix, boolean alwaysPreAndSuf, String... strArr) {
        return charSequenceJoin(delimiter, prefix, suffix, alwaysPreAndSuf, Arrays.stream(strArr));
    }

    public static String charSequenceJoin(String delimiter, String prefix, String suffix, boolean alwaysPreAndSuf, Collection<String> collection) {
        return charSequenceJoin(delimiter, prefix, suffix, alwaysPreAndSuf, collection.stream());
    }

    public static <T> String charSequenceJoin(String delimiter, String prefix, String suffix, boolean alwaysPreAndSuf, Collection<T> collection, Function<T, String> func) {
        return charSequenceJoin(delimiter, prefix, suffix, alwaysPreAndSuf, collection.stream().map(func));
    }
}
