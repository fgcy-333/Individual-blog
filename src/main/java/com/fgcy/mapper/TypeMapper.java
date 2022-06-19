package com.fgcy.mapper;

import com.fgcy.pojo.Type;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface TypeMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Type record);

    int insertSelective(Type record);

    Type selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Type record);

    int updateByPrimaryKey(Type record);

    @Select("select id, name, count from t_type")
    List<Type> findAllByPage();

    @Select("select count(1) from t_type where name=#{name}")
    int findTypeByName(String name);

    @Select("select id, name, count from t_type where id=#{id}")
    Type findTypeById(Integer id);

    void addTypeCount(Integer typeId);

    void reduceTypeCount(Integer olds);
}
