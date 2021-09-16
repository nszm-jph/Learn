package com.learn.thread.pattern.chapter_25;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class LRUCache<K, V> {

    // 用于记录key 值的顺序
    private final LinkedList<K> keyList = new LinkedList<>();

    // 用于存放数据
    private final Map<K, V> cache = new HashMap<>();

    // cache 的最大容量
    private final int capacity;

    //cacheLoader 接口提供了一种加载数据的方式
    private final CacheLoader<K, V> cacheLoader;

    public LRUCache(int capacity, CacheLoader<K, V> cacheLoader) {
        this.capacity = capacity;
        this.cacheLoader = cacheLoader;
    }

    public void put(K key, V value) {
        // 当元素数量超过容量时，将最老的数据清除
        if (keyList.size() >= capacity) {
            K eldestKey = keyList.removeFirst();
            cache.remove(eldestKey);
        }
        // 如果数据已经存在， 则从key 的队列中删除
        keyList.remove(key);
        // 将key 存放至队尾
        keyList.addLast(key);
        cache.put(key, value);
    }

    public V get(K key) {
        V value;
        // 先将key 从key list 中删除
        boolean success = keyList.remove(key);
        // 如果删除失败则表明该数据不存在
        if (!success) {
            // 通过cacheloader 对数据进行加载
            value = cacheLoader.load(key);
            // 调用put 方法cache 数据
            this.put(key, value);
        } else {
            // 如果删除成功，则从cache 中返回数据，并且将key 再次放到队尾
            value = cache.get(key);
            keyList.addLast(key);
        }
        return value;
    }

    @Override
    public String toString() {
        return this.keyList.toString();
    }

    public static void main(String[] args) {
        LRUCache<String, Reference> cache = new LRUCache<>(5, key -> new Reference());
        cache.get("A");
        cache.get("B");
        cache.get("C");
        cache.get("D");
        cache.get("E");

        cache.get("F");
        System.out.println(cache.toString());
    }
}
