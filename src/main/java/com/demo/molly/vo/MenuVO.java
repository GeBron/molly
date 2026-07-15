package com.demo.molly.vo;

import java.util.List;

/**
 * 菜单视图
 */
public record MenuVO(
        Long id,
        String name,
        String path,
        Integer type,
        String permCode,
        String icon,
        List<MenuVO> children
) {
}
