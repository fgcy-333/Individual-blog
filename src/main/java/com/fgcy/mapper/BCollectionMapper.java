package com.fgcy.mapper;

import com.fgcy.pojo.BCollection;

import java.util.List;

public interface BCollectionMapper {
    int deleteByPrimaryKey(Long id);

    int insert(BCollection record);

    int insertSelective(BCollection record);


    int updateByPrimaryKeySelective(BCollection record);

    int updateByPrimaryKey(BCollection record);

    List<BCollection> getCollectionByUserId(Long userId);
}
