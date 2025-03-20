package demo.muhsener01.urlshortener.service.impl;

import demo.muhsener01.urlshortener.service.CacheService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;


@Service
@RequiredArgsConstructor
public class CacheServiceImpl<K, V> implements CacheService<K, V> {

    private final RedisTemplate<K, V> redisTemplate;

    public void set(K key, V value, long ttlInMinutes) {
        redisTemplate.opsForValue().set(key, value, ttlInMinutes, TimeUnit.MINUTES);
    }

    public V get(K key) {
        return redisTemplate.opsForValue().get(key);
    }

    @Override
    public void delete(K cacheKey) {
        redisTemplate.opsForValue().getAndDelete(cacheKey);
    }
}
