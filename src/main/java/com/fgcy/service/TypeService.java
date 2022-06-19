package com.fgcy.service;

import com.fgcy.pojo.Type;
import com.fgcy.util.ResultData;
import com.github.pagehelper.PageInfo;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @Author fgcy
 * @Date 2022/5/21
 */
public interface TypeService {
    ResultData addType(Type type);

    int updateType(Type type);

    ResultData deleteType(Integer id);

    PageInfo<Type> findAllByPage(Integer page, Integer offset);

    Type getTypeById(Integer id);

    ResultData editType(Type type);


    List<Type> getAllTypes();
}
