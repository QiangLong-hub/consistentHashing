package com.example.consistenthashing.ql.service;

/**
 * 键-作为哈希的输入
 *
 * @author longqiang
 * @since 2025-09-22
 */
public interface Key {
    /**
     * 作为哈希的输入键： 如服务区的groupId，节点的ip， sc的id等
     *
     * @return 字符串形式
     */
    String key();
}

