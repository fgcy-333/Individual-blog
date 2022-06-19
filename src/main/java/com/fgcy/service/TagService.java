package com.fgcy.service;

import com.fgcy.pojo.Tag;
import com.fgcy.util.ResultData;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * @Author fgcy
 * @Date 2022/5/22
 */

public interface TagService {
    PageInfo<Tag> findPageByPageInfo(Integer page, Integer offset);

    ResultData getTagById(Integer id);

    ResultData deleteTagById(Integer id);

    ResultData editTag(Tag tag);

    ResultData addTag(Tag tag);

    List<Tag> getAllTags();
}
