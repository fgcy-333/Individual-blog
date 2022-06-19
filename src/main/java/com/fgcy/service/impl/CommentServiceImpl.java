package com.fgcy.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.fgcy.mapper.BCommentMapper;
import com.fgcy.mapper.UserMapper;
import com.fgcy.pojo.BComment;
import com.fgcy.pojo.LoginUser;
import com.fgcy.pojo.User;
import com.fgcy.pojo.vo.BaseUser;
import com.fgcy.pojo.vo.ExtendComment;
import com.fgcy.service.CommentService;
import com.fgcy.util.JwtUtil;
import com.fgcy.util.RedisConst;
import com.fgcy.util.RedisUtil;
import io.jsonwebtoken.Claims;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.fgcy.util.RedisConst.LOGIN_USER;

/**
 * @Author fgcy
 * @Date 2022/6/7
 */
@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    BCommentMapper bCommentMapper;

    @Autowired
    UserMapper userMapper;

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    @Autowired
    private RedisUtil redisUtil;

    @Transactional
    @Override
    public List<ExtendComment> listCommentByblogId(Long blogId) {
        List<ExtendComment> commentList = bCommentMapper.selectCommentByBlogId(blogId);
        //添加@名字
        for (int i = 0; i < commentList.size(); i++) {
            ExtendComment comment = commentList.get(i);
            //获取父评论的用户id
            Long userId = bCommentMapper.getUserIdByCommentId(comment.getParentId());

            String name = userMapper.getUserNameByUserId(userId);
            comment.setParentName(name);
        }
        List<ExtendComment> comments = combineChildren(commentList);
        return comments;
    }


    @Override
    public int saveComment(BComment comment, String token) {
        BaseUser user = authenteLogin(token);
        if (Objects.isNull(user)) return -1;

        comment.setUserId(user.getId());
        comment.setCreated(new Date());
        int i = bCommentMapper.addComment(comment);
        if (i != 1) throw new RuntimeException("新增评论异常");
        redisUtil.increanExtendBlogFieldCount(comment.getBlogId(), RedisUtil.COMMENT);
        return 0;
    }

    private BaseUser authenteLogin(String token) {

        if (StrUtil.isBlankIfStr(token)) return null;

        try {
            Claims claims = JwtUtil.parseJWT(token);
            token = claims.getSubject();

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("无效token");
        }

        String jsonLoginUser = stringRedisTemplate.opsForValue().get(LOGIN_USER + token);
        if (StrUtil.isBlank(jsonLoginUser)) {
            return null;
        }

        //从redis中获取到用户信息，说明已登陆
        LoginUser loginUser = JSONUtil.toBean(jsonLoginUser, LoginUser.class);
        BaseUser user = new BaseUser();
        BeanUtil.copyProperties(loginUser.getUser(), user);
        return user;
    }

    private List<ExtendComment> combineChildren(List<ExtendComment> commentList) {
        //获取顶级节点
        List<ExtendComment> topRoot = commentList.stream().filter(s -> s.getParentId() == -1).collect(Collectors.toList());
        //遍历顶级节点，初始化子评论
        for (ExtendComment extendComment : topRoot) {
            doWithReplys(extendComment.getId(), commentList);
            extendComment.setReplyComments(tempReplyList);
            tempReplyList = new ArrayList<>();
        }
        return topRoot;
    }

    List<ExtendComment> tempReplyList = new ArrayList<>();

    private void doWithReplys(Long id, List<ExtendComment> commentList) {
        //顶级评论下的子评论
        List<ExtendComment> collect = commentList.stream().filter(s -> s.getParentId() == id).collect(Collectors.toList());

        //没有子评论
        if (Objects.isNull(collect) || collect.isEmpty()) return;

        //遍历顶级评论下的子评论
        for (ExtendComment comment : collect) {
            recursively(comment, commentList);
        }
    }

    private void recursively(ExtendComment comment, List<ExtendComment> commentList) {
        //将回复的评论添加到tem集合中
        tempReplyList.add(comment);

        //查看该条评论的子评论
        List<ExtendComment> collect = commentList.stream().filter(s -> s.getParentId() == comment.getId()).collect(Collectors.toList());
        if (Objects.isNull(collect) || collect.isEmpty()) return;

        for (ExtendComment c : collect) {
            recursively(c, commentList);
        }
    }

}
