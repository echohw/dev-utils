package com.example.devutils.constant;

import com.example.devutils.utils.collection.CollectionUtils;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by AMe on 2020-10-19 15:42.
 */
public class MediaTypeMaps {

    private static final String MAP_FILE_NAME = "extension_mediatype_map.dic";
    private static final String KEY_VALUE_SEPARATOR = "\\|";
    private static final String MULTI_ITEM_SEPARATOR = ",";
    private static final Map<String, String> EXT_TYPE_MAP = new HashMap<>();
    private static final Map<String, String> TYPE_EXT_MAP = new HashMap<>();

    private static final Logger logger = LoggerFactory.getLogger(MediaTypeMaps.class);

    static {
        initExtTypeMap();
        initTypeExtMap();
    }

    private static void initExtTypeMap() {
        logger.info("Read {}", MAP_FILE_NAME);
        try (
            InputStream inputStream = MediaTypeMaps.class.getClassLoader().getResourceAsStream(MAP_FILE_NAME);
            BufferedReader reader = new BufferedReader(new InputStreamReader(Objects.requireNonNull(inputStream), CharsetConsts.UTF_8));
        ) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] split = line.split(KEY_VALUE_SEPARATOR);
                if (split.length == 2) {
                    EXT_TYPE_MAP.put(split[0].trim(), split[1].trim());
                } else {
                    logger.warn("Extension media type mapping format error. line: {}", line);
                }
            }
        } catch (IOException ex) {
            logger.error("", ex);
        }
    }

    private static void initTypeExtMap() {
        HashMap<String, ArrayList<String>> grouping = CollectionUtils.grouping(
            EXT_TYPE_MAP.entrySet().stream(),
            HashMap::new,
            entry -> Arrays.asList(entry.getValue().split(MULTI_ITEM_SEPARATOR)),
            key -> new ArrayList<>(),
            Entry::getKey
        );
        grouping.forEach((key, values) -> TYPE_EXT_MAP.put(key, String.join(MULTI_ITEM_SEPARATOR, values)));
    }

    public static List<String> getExtensionList() {
        return new ArrayList<>(EXT_TYPE_MAP.keySet());
    }

    public static List<String> getMediaTypeList() {
        return new ArrayList<>(TYPE_EXT_MAP.keySet());
    }

    public static String getExtension(String mediaType) {
        return TYPE_EXT_MAP.get(mediaType);
    }

    public static String getFirstExtension(String mediaType) {
        String extension = getExtension(mediaType);
        String[] split = extension == null ? new String[0] : extension.split(MULTI_ITEM_SEPARATOR);
        return split.length >= 1 ? split[0].trim() : null;
    }

    public static String getMediaType(String extension) {
        return EXT_TYPE_MAP.get(extension);
    }

    public static String getFirstMediaType(String extension) {
        String mediaType = getMediaType(extension);
        String[] split = mediaType == null ? new String[0] : mediaType.split(MULTI_ITEM_SEPARATOR);
        return split.length >= 1 ? split[0].trim() : null;
    }
}
