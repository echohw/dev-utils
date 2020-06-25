package com.example.devutils.dep;

/**
 * Created by AMe on 2020-06-25 02:36.
 */
public class Regexs {

    public static final String NUMBER_CONTAIN = "\\d+"; // 包含数字
    public static final String NUMBER_ALL = "^" + NUMBER_CONTAIN + "$"; // 仅数字
    public static final String HANZI_CONTAIN = "[\u4e00-\u9fa5]+"; // 包含汉字
    public static final String HANZI_ALL = "^" + HANZI_CONTAIN + "$"; // 仅汉字
    public static final String KANJI_CONTAIN = "[\\u3040-\\u309F\\u30A0-\\u30FF]+"; // 包含日语字符
    public static final String KANJI_ALL = "^" + KANJI_CONTAIN + "$"; // 仅日语字符
    public static final String IPV4_CONTAIN = "\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}"; // 包含IP地址
    public static final String IPV4_ALL = "^" + IPV4_CONTAIN + "$"; // 仅IP地址

}
