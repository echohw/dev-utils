package com.example.devutils.dep;

import java.nio.charset.Charset;

/**
 * Created by AMe on 2020-06-15 03:17.
 */
public class Charsets {

    public static final String UTF8_S = "UTF-8";
    public static final String GBK_S = "GBK";
    public static final String ISO88591_S = "ISO-8859-1";

    public static final Charset UTF8_C = Charset.forName(UTF8_S);
    public static final Charset GBK_C = Charset.forName(GBK_S);
    public static final Charset ISO88591_C = Charset.forName(ISO88591_S);

}
