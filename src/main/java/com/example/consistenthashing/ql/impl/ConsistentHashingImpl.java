package com.example.consistenthashing.ql.impl;

import com.example.consistenthashing.ql.ConsistentHashing;
import com.example.consistenthashing.ql.Key;
import com.example.consistenthashing.ql.config.HashConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;

/**
 * 一致性哈希环实现
 *
 * @author longqiang
 * @since 2025-09-22
 *
 */
@Service
@RequiredArgsConstructor
class ConsistentHashingImpl implements ConsistentHashing {
    private final HashConfig hashConfig;
    private ConcurrentHashMap<String, PhySicNode> physicNodeMap = new ConcurrentHashMap<>();
    private ConcurrentSkipListMap<Long, VirtualNode> virtualNodeMap = new ConcurrentSkipListMap<>();
    @Override
    public Key getNode(Key key) {
        long hash = hashConfig.hash(key.key());
        if (virtualNodeMap.isEmpty()) {
            return null;
        }
        ConcurrentNavigableMap<Long, VirtualNode> map = virtualNodeMap.tailMap(hash);
        return map.isEmpty() ? virtualNodeMap.firstEntry().getValue().getPnKey()
                : map.firstEntry().getValue().getPnKey();
    }

    @Override
    public void addNode(Key key) {
        String keyStr = key.key();
        if (Objects.nonNull(physicNodeMap.get(keyStr))) {
            return;
        }
        HashSet<VirtualNode> virtualNodes = new HashSet<>();
        long hash = hashConfig.hash(keyStr);
        // 插入物理节点本身
        VirtualNode pNode = new VirtualNode(hash, key, key);
        virtualNodeMap.put(hash, pNode);
        virtualNodes.add(pNode);
        // 插入虚拟节点
        for (int i = 0; i < hashConfig.getVCount(); i++) {
            String vStr = keyStr + "_" + i;
            long vHash = hashConfig.hash(vStr);
            // 解决哈希碰撞
            while (Objects.nonNull(virtualNodeMap.get(vHash))) {
                vHash = hashConfig.hash(vStr + "_" + System.nanoTime());
            }
            VirtualNode vNode = new VirtualNode(vHash, () -> vStr, key);
            virtualNodeMap.put(vHash, vNode);
            virtualNodes.add(vNode);
        }
        physicNodeMap.put(keyStr, new PhySicNode(key, virtualNodes));
    }

    @Override
    public void removeNode(Key key) {
        if (Objects.isNull(physicNodeMap.get(key.key()))) {
            return;
        }
        PhySicNode pNode = physicNodeMap.remove(key.key());
        for (VirtualNode vNode : pNode.getVNodes()) {
            virtualNodeMap.remove(hashConfig.hash(vNode.getKey().key()));
        }
    }

    @Override
    public ConcurrentHashMap<String, PhySicNode> getPhySicNodeMap() {
        return this.physicNodeMap;
    }
}

