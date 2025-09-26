package com.example.consistenthashing.ql.dto;

import com.example.consistenthashing.ql.service.Key;
import lombok.Getter;

import java.util.HashSet;

/**
 * 物理节点
 *
 * @author longqiang
 * @since 2025-09-22
 */
@Getter
public class PhySicNode extends Node {
    /**
     * 虚拟节点
     */
    private HashSet<VirtualNode> vNodes;

    public PhySicNode(Key key, HashSet<VirtualNode> vNodes) {
        super(key);
        this.vNodes = vNodes;
    }
}