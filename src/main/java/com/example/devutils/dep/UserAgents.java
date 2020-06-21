package com.example.devutils.dep;

import com.example.devutils.utils.NumberUtils;
import com.example.devutils.utils.collection.CollectionUtils;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by AMe on 2020-06-14 15:29.
 */
public class UserAgents {

    private Map<String, List<String>> userAgentsMap = new HashMap<>();
    private Map<String, AtomicInteger> groupPointMap = new HashMap<>();

    public static final String DEFAULT_GROUP = "@FztDUcj";

    public UserAgents() {
        userAgentsMap.put(DEFAULT_GROUP, new ArrayList<>());
    }

    public UserAgents(List<String> userAgents) {
        if (CollectionUtils.isNotEmpty(userAgents)) {
            userAgentsMap.put(DEFAULT_GROUP, new ArrayList<>(userAgents));
        }
    }

    public Set<String> groups() {
        return userAgentsMap.keySet();
    }

    public List<String> userAgents(String group) {
        List<String> agents = Optional.ofNullable(userAgentsMap.get(group)).orElseGet(ArrayList::new);
        return new ArrayList<>(agents);
    }

    public boolean add(String userAgent) {
        return add(userAgent, DEFAULT_GROUP);
    }

    public boolean add(String userAgent, String group) {
        return add(Collections.singletonList(userAgent), group);
    }

    public synchronized boolean add(List<String> userAgents, String group) {
        List<String> agents = Optional.ofNullable(userAgentsMap.get(group)).orElseGet(ArrayList::new);
        agents.addAll(userAgents);
        userAgentsMap.put(group, agents);
        return true;
    }

    public boolean remove(String userAgent) {
        return remove(userAgent, DEFAULT_GROUP);
    }

    public boolean remove(String userAgent, String group) {
        return remove(Collections.singletonList(userAgent), group);
    }

    public synchronized boolean remove(List<String> userAgents, String group) {
        List<String> agents = Optional.ofNullable(userAgentsMap.get(group)).orElseGet(ArrayList::new);
        return agents.removeAll(userAgents);
    }

    public String random() {
        return random(DEFAULT_GROUP);
    }

    public synchronized String random(String group) {
        List<String> agents = Optional.ofNullable(userAgentsMap.get(group)).orElseGet(ArrayList::new);
        int size = agents.size();
        if (size == 0) {
            return null;
        } else {
            return agents.get((int) NumberUtils.random(0, size));
        }
    }

    public String next() {
        return next(DEFAULT_GROUP);
    }

    public synchronized String next(String group) {
        List<String> agents = Optional.ofNullable(userAgentsMap.get(group)).orElseGet(ArrayList::new);
        int size = agents.size();
        if (size == 0) {
            return null;
        } else {
            AtomicInteger point = groupPointMap.getOrDefault(group, new AtomicInteger(0));
            groupPointMap.put(group, point);
            return agents.get(point.getAndIncrement() % size);
        }
    }

}
