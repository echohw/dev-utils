package com.example.devutils.utils.redis;

import com.example.devutils.utils.collection.CollectionUtils;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import org.springframework.data.redis.connection.DataType;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.scripting.ScriptSource;

/**
 * Created by AMe on 2020-06-21 21:13.
 */
public class RedisUtils<K, V> {

    private RedisTemplate<K, V> redisTemplate;

    public RedisUtils(RedisTemplate<K, V> redisTemplate) {
        this.redisTemplate = Objects.requireNonNull(redisTemplate);
    }

    public RedisTemplate<K, V> redisTemplate() {
        return redisTemplate;
    }

    public Boolean del(K key) {
        return redisTemplate.delete(key);
    }

    public Long del(Collection<K> keys) {
        return redisTemplate.delete(keys);
    }

    public Boolean exists(K key) {
        return redisTemplate.hasKey(key);
    }

    public Boolean expire(K key, long timeout, TimeUnit unit) {
        return redisTemplate.expire(key, timeout, unit);
    }

    public Boolean persist(K key) {
        return redisTemplate.persist(key);
    }

    public void rename(K oldKey, K newKey) {
        redisTemplate.rename(oldKey, newKey);
    }

    public Long ttl(K key) {
        return ttl(key, TimeUnit.SECONDS);
    }

    public Long ttl(K key, TimeUnit unit) {
        return redisTemplate.getExpire(key, unit);
    }

    public DataType type(K key) {
        return redisTemplate.type(key);
    }

    /**
     * 执行事务
     */
    public List<Object> execTransaction(List<K> watchKeys, Function<RedisTemplate<K, V>, Boolean> stepExecutorFunc) {
        if (CollectionUtils.isNotEmpty(watchKeys)) {
            redisTemplate.watch(watchKeys);
        }
        redisTemplate.multi();
        Boolean carryOn = stepExecutorFunc.apply(redisTemplate);
        if (carryOn != null && carryOn) {
            return redisTemplate.exec();
        } else {
            redisTemplate.discard();
            return Collections.emptyList();
        }
    }

    /**
     * 执行Lua脚本
     */
    public <T> T execScript(String scriptText, List<K> keys, Object[] args, Class<T> clazz) {
        DefaultRedisScript<T> redisScript = new DefaultRedisScript<>();
        redisScript.setResultType(clazz);
        redisScript.setScriptText(scriptText);
        return redisTemplate.execute(redisScript, keys, args);
    }

    public <T> T execScript(ScriptSource scriptSource, List<K> keys, Object[] args, Class<T> clazz) {
        DefaultRedisScript<T> redisScript = new DefaultRedisScript<>();
        redisScript.setResultType(clazz);
        redisScript.setScriptSource(scriptSource);
        return redisTemplate.execute(redisScript, keys, args);
    }

}
