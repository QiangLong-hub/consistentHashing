package com.example.consistenthashing.ql.dto;

import com.example.consistenthashing.ql.service.Key;
import com.example.consistenthashing.ql.service.Node;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 虚拟节点
 *
 * @author longqiang
 * @since 2025-09-22
 */
@AllArgsConstructor
@Getter
public class VirtualNode implements Node {
    private long hash;
    /**
     * 虚拟节点本身的key
     */
    private Key key;
    /**
     * 物理节点的key
     */
    private Key pnKey;

    @Override
    public Key key() {
        return key;
    }
}
