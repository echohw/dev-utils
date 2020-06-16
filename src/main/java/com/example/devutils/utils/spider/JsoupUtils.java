package com.example.devutils.utils.spider;

import java.util.ArrayList;
import java.util.List;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * Created by AMe on 2020-06-09 15:23.
 */
public class JsoupUtils {

    public static String getText(String html) {
        return getDocument(html).text();
    }

    public static List<String> getText(String html, String cssQuery) {
        Elements elements = getDocument(html).select(cssQuery);
        ArrayList<String> list = new ArrayList<>(elements.size());
        for (Element element : elements) {
            list.add(element.text());
        }
        return list;
    }

    public static Document getDocument(String html) {
        return Jsoup.parse(html);
    }

}
