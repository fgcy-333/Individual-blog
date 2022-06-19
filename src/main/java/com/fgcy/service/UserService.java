package com.fgcy.service;

import com.fgcy.pojo.BCollection;
import com.fgcy.pojo.User;

import java.util.List;
import java.util.Map;

/**
 * @Author fgcy
 * @Date 2022/5/21
 */
public interface UserService {
    User checkUser(String username, String password);

    Map<String, List<BCollection>> getCollections(Long userId);

    boolean checkUsername(String username);

    int getCollectionsCountByUserId(Long userId);
}
