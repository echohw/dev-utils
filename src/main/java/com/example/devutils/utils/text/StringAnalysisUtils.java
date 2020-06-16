package com.example.devutils.utils.text;


import com.example.devutils.utils.collection.CollectionUtils;
import com.example.devutils.utils.collection.MapUtils;
import java.io.IOException;
import java.util.HashMap;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.wltea.analyzer.lucene.IKAnalyzer;

/**
 * 字符串分析工具类
 * Created by AMe on 2020-06-09 13:51.
 */
public class StringAnalysisUtils {

    /**
     * 词组统计
     */
    public static HashMap<String, Integer> termCount(String str, boolean useSmart) throws IOException {
        HashMap<String, Integer> countMap = new HashMap<>();
        IKAnalyzer analyzer = getIKAnalyzer();
        analyzer.setUseSmart(useSmart);
        TokenStream tokenStream = analyzer.tokenStream("default", str);
        CharTermAttribute charTermAttribute = tokenStream.addAttribute(CharTermAttribute.class);
        tokenStream.reset();
        while (tokenStream.incrementToken()) {
            String termStr = charTermAttribute.toString();
            countMap.put(termStr, countMap.getOrDefault(termStr, 0) + 1);
        }
        return MapUtils.sort(countMap, (o1, o2) -> o2.getValue().compareTo(o1.getValue()));
    }

    private static IKAnalyzer getIKAnalyzer() {
        return new IKAnalyzer();
    }

}
