package com.fgcy.mapper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @Author fgcy
 * @Date 2022/6/13
 */
@SpringBootTest
class TagMapperTest {

    @Autowired
    private TagMapper tagMapper;

    @Test
    void addTagCount() {
        tagMapper.addTagCount(Arrays.asList(1L));
    }
}
