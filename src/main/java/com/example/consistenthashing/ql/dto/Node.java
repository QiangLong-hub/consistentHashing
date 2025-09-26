package com.example.consistenthashing.ql.dto;

import com.example.consistenthashing.ql.service.Key;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 节点
 *
 * @author longqiang
 * @since 2025-09-22
 */
@AllArgsConstructor
@Getter
public abstract class Node {
    private Key key;
}