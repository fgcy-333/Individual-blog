package com.fgcy.mapper;

import com.fgcy.pojo.User;
import com.fgcy.pojo.vo.BaseUser;
import org.apache.ibatis.annotations.Param;

import java.util.UUID;

public interface UserMapper {
    int deleteByPrimaryKey(Long id);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Long id);

    User getBaseUserByUserId(Long userId);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);

    User selectUserByNameAndPassword(@Param("username") String username, @Param("password") String password);

    User getUserByName(String username);

    BaseUser getbaseUserByUserId(Long userId);

    String getUserNameByUserId(Long id);

    int checkUserName(String username);

    void addUser(User user);

    void addRoleBindUser(@Param("userId") Long userId, @Param("roldId") Long roleId);

    int getCollectionsCountByUserId(Long userId);


}
