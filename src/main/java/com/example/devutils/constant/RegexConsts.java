package com.example.devutils.constant;

/**
 * Created by AMe on 2020-06-25 02:36.
 */
public class RegexConsts {

    public static final String NUMBER_CONTAIN = "\\d+";
    public static final String NUMBER_ONLY = only(NUMBER_CONTAIN);
    public static final String HANZI_CONTAIN = "[\u4e00-\u9fa5]+";
    public static final String HANZI_ONLY = only(HANZI_CONTAIN);
    public static final String KANJI_CONTAIN = "[\\u3040-\\u309F\\u30A0-\\u30FF]+";
    public static final String KANJI_ONLY = only(KANJI_CONTAIN);
    public static final String EMAIL_CONTAIN = "[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*+(\\+[_A-Za-z0-9-]+)*@[A-Za-z0-9.-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})";
    public static final String EMAIL_ONLY = only(EMAIL_CONTAIN);
    public static final String PHONE_CONTAIN = "((13[0-9])|(14[579])|(15([0-3]|[5-9]))|(166)|(17[0135678])|(18[0-9])|(19[8|9]))\\d{8}";
    public static final String PHONE_ONLY = only(PHONE_CONTAIN);
    public static final String IDCARD15_CONTAIN = "[1-9]\\d{7}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}";
    public static final String IDCARD15_ONLY = only(IDCARD15_CONTAIN);
    public static final String IDCARD18_CONTAIN = "[1-9]\\d{5}[1-9]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}([0-9]|X)";
    public static final String IDCARD18_ONLY = only(IDCARD18_CONTAIN);

    private static String only(String regex) {
        if (!regex.startsWith("^")) {
            regex = "^".concat(regex);
        }
        if (!regex.endsWith("$")) {
            regex = regex.concat("$");
        }
        return regex;
    }
}
