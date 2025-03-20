package demo.muhsener01.urlshortener.service;

public interface CacheService<K, V> {


    void set(K key, V value, long ttlInMinutes);

    V get(K key);

    void delete(K cacheKey);

}
