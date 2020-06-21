package com.example.devutils.dep;

import java.nio.charset.Charset;

/**
 * Created by AMe on 2020-06-15 03:17.
 */
public class Charsets {

    public static final Charset SYSTEM = Charset.forName(System.getProperty("file.encoding"));

    public static final Charset UTF_8 = Charset.forName("UTF-8");
    public static final Charset GBK = Charset.forName("GBK");
    public static final Charset ISO_8859_1 = Charset.forName("ISO-8859-1");

    public static final Charset DEFAULT = Charset.defaultCharset();

}
