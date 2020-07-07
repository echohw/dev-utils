package com.example.devutils.utils.office;

import com.example.devutils.dep.MediaTypes;
import com.example.devutils.utils.codec.Base64Utils;
import com.example.devutils.utils.text.StringUtils;
import java.io.IOException;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import org.commonmark.node.Image;
import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.AttributeProvider;
import org.commonmark.renderer.html.AttributeProviderFactory;
import org.commonmark.renderer.html.HtmlNodeRendererFactory;
import org.commonmark.renderer.html.HtmlRenderer;
import org.commonmark.renderer.html.HtmlRenderer.Builder;
import org.jooq.lambda.Unchecked;

/**
 * Markdown解析工具类
 * Created by AMe on 2020-07-07 02:48.
 */
public class CommonMarkUtils {

    public static Node getNode(String mark) {
        return getNode(getParser(), mark);
    }

    public static Node getNode(Parser parser, String mark) {
        return parser.parse(mark);
    }

    public static Node getNode(Reader reader) throws IOException {
        return getNode(getParser(), reader);
    }

    public static Node getNode(Parser parser, Reader reader) throws IOException {
        return parser.parseReader(reader);
    }

    public static Parser getParser() {
        return Parser.builder().build();
    }

    public static HtmlRenderer getHtmlRenderer() {
        return HtmlRenderer.builder().build();
    }

    public static HtmlRenderer getHtmlRenderer(AttributeProviderFactory attributeProviderFactory, HtmlNodeRendererFactory htmlNodeRendererFactory) {
        Builder builder = HtmlRenderer.builder();
        Optional.ofNullable(attributeProviderFactory).ifPresent(builder::attributeProviderFactory);
        Optional.ofNullable(htmlNodeRendererFactory).ifPresent(builder::nodeRendererFactory);
        return builder.build();
    }

    public static AttributeProvider getAttributeProvider(Map<String, Map<String, String>> attrMaps) {
        return (node, tagName, attributes) -> {
            attributes.putAll(Optional.ofNullable(attrMaps.get(tagName)).orElseGet(HashMap::new));
        };
    }

    public static AttributeProvider getImageAttributeProvider(Map<String, String> attrs, boolean encodeImage) {
        return (node, tagName, attributes) -> {
            if (node instanceof Image) {
                Optional.ofNullable(attrs).ifPresent(attributes::putAll);
                if (encodeImage) {
                    Optional.ofNullable(attributes.get("src")).filter(StringUtils::isNotBlank)
                        .ifPresent(Unchecked.consumer(path -> attributes.put("src", Base64Utils.encodeImageToString(ImageUtils.readImage(path), MediaTypes.IMAGE_JPEG))));
                }
            }
        };
    }

    public static String renderToHtml(String mark) {
        return renderToHtml(getNode(mark));
    }

    public static String renderToHtml(Node node) {
        return renderToHtml(getHtmlRenderer(), node);
    }

    public static String renderToHtml(HtmlRenderer renderer, Node node) {
        return renderer.render(node);
    }

}
