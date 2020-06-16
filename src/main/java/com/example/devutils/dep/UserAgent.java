package com.example.devutils.dep;

import com.example.devutils.utils.NumberUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * Created by AMe on 2020-06-14 15:29.
 */
public class UserAgent {

    private Map<String, List<String>> userAgentsMap = new HashMap<>();

    private static final String DEFAULT_GROUP = "@FztDUcj";

    private int point = 0;

    public UserAgent(List<String> userAgents) {
        Optional.ofNullable(userAgents).ifPresent(agents -> {
            agents.forEach(this::add);
        });
    }

    public Set<String> groups() {
        return userAgentsMap.keySet();
    }

    public boolean add(String userAgent) {
        return add(userAgent, DEFAULT_GROUP);
    }

    public synchronized boolean add(String userAgent, String group) {
        List<String> agents = Optional.ofNullable(userAgentsMap.get(group)).orElseGet(ArrayList::new);
        if (!agents.contains(userAgent)) {
            agents.add(userAgent);
            userAgentsMap.put(group, agents);
        }
        return true;
    }

    public boolean remove(String userAgent) {
        return remove(userAgent, DEFAULT_GROUP);
    }

    public synchronized boolean remove(String userAgent, String group) {
        List<String> agents = Optional.ofNullable(userAgentsMap.get(group)).orElseGet(ArrayList::new);
        agents.remove(userAgent);
        return true;
    }

    public Optional<String> random() {
        return random(DEFAULT_GROUP);
    }

    public synchronized Optional<String> random(String group) {
        List<String> agents = Optional.ofNullable(userAgentsMap.get(group)).orElseGet(ArrayList::new);
        int size = agents.size();
        if (size == 0) {
            return Optional.empty();
        } else {
            return Optional.ofNullable(agents.get((int) NumberUtils.random(0, size)));
        }
    }

    public Optional<String> next() {
        return next(DEFAULT_GROUP);
    }

    public synchronized Optional<String> next(String group) {
        List<String> agents = Optional.ofNullable(userAgentsMap.get(group)).orElseGet(ArrayList::new);
        int size = agents.size();
        if (size == 0) {
            return Optional.empty();
        } else {
            return Optional.ofNullable(agents.get(point++ % size));
        }
    }

}
