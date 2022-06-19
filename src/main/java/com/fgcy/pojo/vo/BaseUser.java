package com.fgcy.pojo.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

/**
 * @Author fgcy
 * @Date 2022/6/8
 */
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class BaseUser {
    private Long id;
    private String username;
    private String avatar;
}
