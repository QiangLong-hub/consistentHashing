package com.example.consistenthashing.impl;

import com.example.consistenthashing.ql.service.ConsistentHashing;
import com.example.consistenthashing.ql.service.Key;
import com.example.consistenthashing.ql.config.HashConfig;
import com.example.consistenthashing.ql.dto.ServiceZone;
import com.example.consistenthashing.ql.dto.PhySicNode;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * 一致性哈希环单元测试
 *
 * @author longqiang
 * @since 2025-09-22
 */
@SpringBootTest
public class ConsistentHashingImplTest {
    @Autowired
    private ConsistentHashing ch;

    @Autowired
    private HashConfig hashConfig;

    @Test
    public void test() {
        // 哈希环中加入物理节点
        Key node1 = () -> "192.168.1.1";
        Key node2 = () -> "192.168.1.2";
        Key node3 = () -> "192.168.1.3";
        ch.addNode(node1);
        ch.addNode(node2);
        ch.addNode(node3);
        ConcurrentHashMap<String, PhySicNode> map = ch.getPhySicNodeMap();
        Assertions.assertTrue(map.size() == 3);
        map.values().stream().map(PhySicNode::getVNodes).allMatch(set -> set.size() == hashConfig.getVCount() + 1);
        Assertions.assertTrue(map.keySet().contains(node1.key()));
        Assertions.assertTrue(map.keySet().contains(node2.key()));
        Assertions.assertTrue(map.keySet().contains(node3.key()));

        List<ServiceZone> serviceZoneList = Arrays.asList(new ServiceZone("zone1"), new ServiceZone("zone2"),
                new ServiceZone("zone3"), new ServiceZone("zone4"), new ServiceZone("zone5"));
        // 计算服务区在哈希环上对应的物理节点
        List<String> nodes = serviceZoneList.stream()
                .map(ServiceZone::getId)
                .map(id -> ch.getNode(() -> id))
                .map(Key::key)
                .collect(Collectors.toList());
        // node2下线，原先没有映射在node2上的服务区保持不变，映射在node2上的服务区从哈希环上顺时针找下一个节点
        ch.removeNode(node2);
        List<String> nodesAfterRemove = serviceZoneList.stream()
                .map(ServiceZone::getId)
                .map(id -> ch.getNode(() -> id))
                .map(Key::key)
                .collect(Collectors.toList());
        for (int i = 0; i < nodes.size(); i++) {
            Assertions.assertTrue(!nodes.get(i).equals(node2.key()) == nodes.get(i).equals(nodesAfterRemove.get(i)));
        }
        // 移除node1, 所有服务区都映射在node3上
        ch.removeNode(node1);
        Set<String> nodeSetAfterRemove = serviceZoneList.stream()
                .map(ServiceZone::getId)
                .map(id -> ch.getNode(() -> id))
                .map(Key::key)
                .collect(Collectors.toSet());
        Assertions.assertTrue(nodeSetAfterRemove.size() == 1 && nodeSetAfterRemove.contains(node3.key()));
    }
}
