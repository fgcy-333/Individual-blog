package com.fgcy.service.impl;

import com.fgcy.mapper.TypeMapper;
import com.fgcy.pojo.Type;
import com.fgcy.service.TypeService;
import com.fgcy.util.ResultData;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Author fgcy
 * @Date 2022/5/21
 */
@Service
public class TypeServiceImpl implements TypeService {
    private static final int OFFSET = 7;


    @Autowired
    TypeMapper typeMapper;

    /*
     *
     * @since: 1.8
     * @description：添加博客类型
     * @author: fgcy
     * @date: 2022/5/24
     */
    @Transactional
    @Override
    public ResultData addType(Type type) {
        if (type == null || type.getName() == null || type.getName().trim() == "") {
            return ResultData.fail().setMsg("该博客类型没有名称！");
        }

        int i = typeMapper.findTypeByName(type.getName());
        if (i != 0) {
            return ResultData.fail().setMsg("该博客类型已重复！");
        }

        type.setCount(0);
        int insert = typeMapper.insert(type);
        return ResultData.success(insert);
    }

    /*
     *
     * @since: 1.8
     * @description：更新博客类型
     * @author: fgcy
     * @date: 2022/5/24
     */
    @Override
    public int updateType(Type type) {
        if (type.getId() == null) throw new RuntimeException("更新类型 没有id");
        if (type.getName() == null || "".equals(type)) throw new RuntimeException("更新类型 名字为空");
        return typeMapper.updateByPrimaryKey(type);
    }

    @Transactional
    @Override
    public ResultData deleteType(Integer id) {
        if (id == null) return ResultData.fail().setMsg("要删除博客类型的id为空");
        int i = typeMapper.deleteByPrimaryKey(id);

        if (i != 1) return ResultData.fail().setMsg("博客类型删除失败");

        return ResultData.success(null);
    }

    /*
     *
     * @since: 1.8
     * @description：分页展示博客类型
     * @author: fgcy
     * @date: 2022/5/24
     */
    @Override
    public PageInfo<Type> findAllByPage(Integer page, Integer offset) {
        if (page == null) {
            page = 1;
        }
        if (offset == null) {
            offset = OFFSET;
        }
        PageHelper.startPage(page, offset);
        List<Type> all = typeMapper.findAllByPage();
        return new PageInfo<Type>(all);
    }


    @Override
    public Type getTypeById(Integer id) {
        if (id == null) throw new RuntimeException("根据id查询类型 参数为空");
        return typeMapper.selectByPrimaryKey(id);
    }


    @Transactional
    @Override
    public ResultData editType(Type type) {
        if (type.getId() == null) return ResultData.fail().setMsg("没有该博客类型的id！");

        int i = typeMapper.findTypeByName(type.getName());
        if (i != 0) {
            return ResultData.fail().setMsg("该博客类型已重复！");
        }
        int r = typeMapper.updateByPrimaryKeySelective(type);
        if (r != 1) return ResultData.fail().setMsg("该博客类型更新失败！");
        return ResultData.success(null);
    }

    @Override
    public List<Type> getAllTypes() {
        return typeMapper.findAllByPage();
    }


}

