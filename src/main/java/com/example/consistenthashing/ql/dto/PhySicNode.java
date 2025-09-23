package com.example.consistenthashing.ql.dto;

import com.example.consistenthashing.ql.service.Key;
import com.example.consistenthashing.ql.service.Node;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.HashSet;

/**
 * 物理节点
 *
 * @author longqiang
 * @since 2025-09-22
 */
@AllArgsConstructor
@Getter
public class PhySicNode implements Node {
    private Key key;

    /**
     * 虚拟节点
     */
    private HashSet<VirtualNode> vNodes;

    @Override
    public Key key() {
        return key;
    }
}