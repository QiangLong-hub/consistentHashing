package com.example.consistenthashing.ql;

import com.example.consistenthashing.ql.impl.PhySicNode;

import java.util.concurrent.ConcurrentHashMap;

/**
 * 一致性哈希算法
 *
 * @author longqiang
 * @since 2025-09-22
 */
public interface ConsistentHashing {
    /**
     * 获取物理节点的键
     *
     * @param key 键
     * @return Key
     */
    Key getNode(Key key);

    /**
     * 插入物理节点(节点上线)
     *
     * @param key
     */
    void addNode(Key key);

    /**
     * 删除物理节点（节点下线）
     *
     * @param key
     */
    void removeNode(Key key);

    /**
     * 获取物理节点
     *
     * @return 物理节点map
     */
    ConcurrentHashMap<String, PhySicNode> getPhySicNodeMap();
}
