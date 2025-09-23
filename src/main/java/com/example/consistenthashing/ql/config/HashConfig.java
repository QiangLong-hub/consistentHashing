package com.example.consistenthashing.ql.config;

import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;
import lombok.Getter;
import org.springframework.stereotype.Component;

/**
 * 一致性哈希算法配置
 *
 * @author longqiang
 * @since 2025-09-22
 */
@Getter
@Component
public class HashConfig {

    /**
     * 哈希算法
     */
    private HashFunction hashAlgo = Hashing.murmur3_128();

    /**
     * 虚拟节点数量
     */
    private int vCount = 1000;

    /**
     * 哈希
     *
     * @param key 键
     * @return 哈希值
     */
    public long hash(String key) {
        return this.hashAlgo.hashUnencodedChars(key).asLong();
    }
}

