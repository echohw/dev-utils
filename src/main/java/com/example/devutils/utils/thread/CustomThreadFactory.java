package com.example.devutils.utils.thread;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by AMe on 2020-09-02 02:18.
 */
public class CustomThreadFactory implements ThreadFactory {

    private final String namePrefix;
    private final ThreadGroup threadGroup;
    private final AtomicInteger nextId = new AtomicInteger(1);

    public CustomThreadFactory(String groupFeature) {
        this(null, groupFeature);
    }

    public CustomThreadFactory(ThreadGroup threadGroup, String groupFeature) {
        this.threadGroup = threadGroup;
        this.namePrefix = String.format("From %s's %s-Worker-", CustomThreadFactory.class.getSimpleName(), groupFeature);
    }

    @Override
    public Thread newThread(Runnable task) {
        String name = namePrefix + nextId.getAndIncrement();
        return new Thread(threadGroup, task, name);
    }
}
