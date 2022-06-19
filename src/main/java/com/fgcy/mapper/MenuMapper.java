package com.fgcy.mapper;

import java.util.List;

/**
 * @Author fgcy
 * @Date 2022/6/1
 */
public interface MenuMapper {
    List<String> getPermsByUserId(Long userId);
}
