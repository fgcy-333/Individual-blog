package com.fgcy.mapper;

import com.fgcy.pojo.BCollection;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @Author fgcy
 * @Date 2022/6/9
 */
@SpringBootTest
class BCollectionMapperTest {

    @Autowired
    private BCollectionMapper bCollectionMapper;

    @Test
    void getCollectionByUserId() {
        List<BCollection> collectionByUserId = bCollectionMapper.getCollectionByUserId(1l);
        System.out.println(collectionByUserId);
    }
}
