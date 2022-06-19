package com.fgcy.service.impl;

import cn.hutool.core.util.StrUtil;
import com.fgcy.mapper.BCollectionMapper;
import com.fgcy.mapper.UserMapper;
import com.fgcy.pojo.BCollection;
import com.fgcy.pojo.Blog;
import com.fgcy.pojo.User;
import com.fgcy.service.UserService;
import com.fgcy.util.MD5Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @Author fgcy
 * @Date 2022/5/21
 */
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    BCollectionMapper bCollectionMapper;

    @Override
    public User checkUser(String username, String password) {
        username = username.trim();
        password = MD5Utils.code(password.trim());
        if (username == null || username == "" || password == null || password == "") {
            return null;
        }
        return userMapper.selectUserByNameAndPassword(username, password);
    }


    /*
     *
     * @since: 1.8
     * @description：收藏列表
     * @author: fgcy
     * @date: 2022/6/12
     */
    @Override
    public Map<String, List<BCollection>> getCollections(Long userId) {
        List<BCollection> collections = bCollectionMapper.getCollectionByUserId(userId);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
        Map<String, List<BCollection>> map = new TreeMap<>(Comparator.reverseOrder());
        for (BCollection collection : collections) {
            String format = sdf.format(collection.getUpdateTime());
            if (Objects.isNull(map.get(format))) map.put(format, new ArrayList<>());
            map.get(format).add(collection);
        }
        return map;
    }

    @Override
    public boolean checkUsername(String username) {
        username = username.trim();
        int i = userMapper.checkUserName(username);
        if (i == 0) return true;
        return false;
    }

    @Override
    public int getCollectionsCountByUserId(Long userId) {
        if (Objects.isNull(userId)) throw new RuntimeException("userId为空");
        return userMapper.getCollectionsCountByUserId(userId);
    }
}

