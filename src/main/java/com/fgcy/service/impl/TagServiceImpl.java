package com.fgcy.service.impl;

import com.fgcy.mapper.TagMapper;
import com.fgcy.pojo.Tag;
import com.fgcy.service.TagService;
import com.fgcy.util.RedisConst;
import com.fgcy.util.RedisUtil;
import com.fgcy.util.ResultData;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author fgcy
 * @Date 2022/5/22
 */
@Service
public class TagServiceImpl implements TagService {
    private static final int OFFSET = 7;
    @Autowired
    private TagMapper tagMapper;
    @Autowired
    StringRedisTemplate stringRedisTemplate;

    @Autowired
    RedisUtil redisUtil;

    @Override
    public PageInfo<Tag> findPageByPageInfo(Integer page, Integer offset) {
        if (page == null) page = 1;
        if (offset == null) offset = OFFSET;

        PageHelper.startPage(page, offset);
        List<Tag> all = tagMapper.findAll();
        return new PageInfo<>(all);
    }

    @Override
    public ResultData getTagById(Integer id) {
        if (id == null) return ResultData.fail().setMsg("查询标签id为空");

        Tag tag = tagMapper.selectByPrimaryKey(id);

        if (tag == null) return ResultData.fail().setMsg("找不到该id对应标签的");

        return ResultData.success(tag);
    }

    /*
     *
     * @since: 1.8
     * @description：删除标签 时删除标签缓存
     * @author: fgcy
     * @date: 2022/6/13
     */
    @Override
    public ResultData deleteTagById(Integer id) {
        if (id == null) return ResultData.fail().setMsg("删除标签id为空");
        stringRedisTemplate.delete(RedisConst.ALL_TAGS);
        int i = tagMapper.deleteByPrimaryKey(id);
        if (i != 1) return ResultData.fail().setMsg("删除失败！");

        return ResultData.success(null);
    }

    @Override
    public ResultData editTag(Tag tag) {
        if (tag == null || tag.getId() == null) return ResultData.fail().setMsg("编辑的标签id为空");
        if (tag.getName() == null || "".equals(tag.getName().trim())) return ResultData.fail().setMsg("编辑的标签名称为空");
        tag.setName(tag.getName().trim());
        int count = tagMapper.getCountByName(tag.getName());
        if (count != 0) return ResultData.fail().setMsg("标签名称已重复");
        int i = tagMapper.updateByPrimaryKeySelective(tag);
        stringRedisTemplate.delete(RedisConst.ALL_TAGS);
        if (i != 1) return ResultData.fail().setMsg("标签编辑失败");
        return ResultData.success(null);

    }

    @Override
    public ResultData addTag(Tag tag) {
        if (tag == null || tag.getName() == null || "".equals(tag.getName().trim())) {
            return ResultData.fail().setMsg("标签名字为空");
        }
        tag.setName(tag.getName().trim());
        int count = tagMapper.getCountByName(tag.getName());
        if (count != 0) return ResultData.fail().setMsg("标签名称已重复");
        tag.setId(null);
        tag.setCount(0);
        int i = tagMapper.insertSelective(tag);
        stringRedisTemplate.delete(RedisConst.ALL_TAGS);
        if (i != 1) return ResultData.fail();
        return ResultData.success(null);
    }

    @Override
    public List<Tag> getAllTags() {
        return tagMapper.findAll();
    }
}
