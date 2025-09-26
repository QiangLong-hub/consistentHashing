package com.example.consistenthashing.ql.dto;

import com.example.consistenthashing.ql.service.Key;

import lombok.Getter;

/**
 * 虚拟节点
 *
 * @author longqiang
 * @since 2025-09-22
 */
@Getter
public class VirtualNode extends Node {
    private long hash;
    /**
     * 物理节点的key
     */
    private Key pnKey;

    public VirtualNode(long hash, Key key, Key pnKey) {
        super(key);
        this.hash = hash;
        this.pnKey = pnKey;
    }
}
