package com.example.devutils.utils.spider;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.stream.Collectors;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * Created by AMe on 2020-06-09 15:23.
 */
public class JsoupUtils {

    public static String getText(String html) {
        return parse(html).text();
    }

    public static List<String> getText(String html, String cssQuery) {
        Elements elements = parse(html).select(cssQuery);
        return elements.stream().map(Element::text).collect(Collectors.toList());
    }

    public static Document parse(String html) {
        return Jsoup.parse(html);
    }

    public static Document parse(File file, String charsetName) throws IOException {
        return Jsoup.parse(file, charsetName);
    }

    public static Document parse(URL url, int timeoutMillis) throws IOException {
        return Jsoup.parse(url, timeoutMillis);
    }
}
