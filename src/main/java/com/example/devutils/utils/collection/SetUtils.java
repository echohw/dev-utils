package com.example.devutils.utils.collection;

import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.stream.Collectors;

/**
 * Created by AMe on 2020-06-13 11:28.
 */
public class SetUtils extends CollectionUtils {

    public static <T> LinkedHashSet<T> sort(HashSet<T> hashSet, Comparator<T> comparator) {
        return hashSet.stream().sorted(comparator).collect(Collectors.toCollection(LinkedHashSet::new));
    }

}
