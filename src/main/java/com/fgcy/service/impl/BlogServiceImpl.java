package com.fgcy.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.fgcy.mapper.BlogMapper;
import com.fgcy.mapper.TagMapper;
import com.fgcy.mapper.TypeMapper;
import com.fgcy.pojo.*;
import com.fgcy.pojo.vo.*;
import com.fgcy.service.BlogService;
import com.fgcy.util.MarkDownUtil;
import com.fgcy.util.RedisConst;
import com.fgcy.util.RedisUtil;
import com.fgcy.util.ResultData;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.fgcy.util.RedisConst.*;


/**
 * @Author fgcy
 * @Date 2022/5/22
 */
@Service
public class BlogServiceImpl implements BlogService {
    @Autowired
    private BlogMapper blogMapper;
    @Autowired
    private TypeMapper typeMapper;
    @Autowired
    private TagMapper tagMapper;
    @Autowired
    StringRedisTemplate stringRedisTemplate;

    @Autowired
    RedisUtil redisUtil;

    private static int OFFSET = 7;//管理页面一页几条

    private static int INDEX_SIZE = 7;//博客首页一页几条


    /*
     *
     * @since: 1.8
     * @description：博客管理分页查询 默认找第一页 一页四条 不推荐
     * @author: fgcy
     * @date: 2022/5/23
     */
    @Override
    public TempBlog selectPage(Integer page, Integer offset) {
        if (page == null) page = 1;
        if (offset == null) offset = OFFSET;
        List<Type> types = typeMapper.findAllByPage();
        List<Tag> tags = tagMapper.findAll();
        TempBlog mBlog = new TempBlog();
        mBlog.setType(types);
        mBlog.setTags(tags);
        PageHelper.startPage(page, offset);
        Blog blog = new Blog();
        //blog.setCommentabled(false);
        List<ExtendBlog> all = blogMapper.getBlogByCondition(blog);
        PageInfo<ExtendBlog> blogs = new PageInfo<>(all);
        mBlog.setBlogs(blogs);
        return mBlog;
    }


    /*
     *
     * @since: 1.8
     * @description：获取所有的标签和类型
     * @author: fgcy
     * @date: 2022/5/23
     */
    @Override
    public TwoLables getAllTagsAndAllType() {
        TwoLables twoLables = new TwoLables();
        List<Tag> allTags = redisUtil.getIfNotExistsThenSetForList(ALL_TAGS, null,
                Tag.class, s -> tagMapper.findAll(), 10L, TimeUnit.DAYS);
        twoLables.setTags(allTags);

        List<Type> allTypes = redisUtil.getIfNotExistsThenSetForList(ALL_TYPES, null, Type.class,
                s -> typeMapper.findAllByPage(), 10L, TimeUnit.DAYS);
        twoLables.setTypes(allTypes);
        return twoLables;
    }

    /*
     *
     * @since: 1.8
     * @description：博客添加
     * @author: fgcy
     * @date: 2022/5/23
     */
    @Transactional
    @Override
    public ResultData addBlog(Blog blog) {
        boolean f = validBlogInfo(blog);
        if (!f) return ResultData.fail().setMsg("请检查参数是否为空！");
        int i = blogMapper.insertSelective(blog);
        //在中间表中添加博客标签[与数量]
        boolean success = addBlogTags(blog);

        //添加博客类型数量
        typeMapper.addTypeCount(blog.getTypeId());
        redisUtil.clearAllTypes();
        //清除博客缓存
        redisUtil.clearIndexCache();

        if (!success) return ResultData.fail();
        if (i != 1) return ResultData.fail();
        return ResultData.success(null);
    }

    /*
     *
     * @since: 1.8
     * @description：博客关联标签添加【中间表】
     * @author: fgcy
     * @date: 2022/5/23
     */
    private boolean addBlogTags(Blog blog) {
        Long id = blog.getId();
        String tagIds = blog.getTagIds();
        List<Long> tagIdsList = Arrays.asList(tagIds.split(",")).stream().map(Long::new).collect(Collectors.toList());
        tagMapper.addTagCount(tagIdsList);
        int i = blogMapper.addBlogTags(tagIdsList, id);
        if (i == 0) return false;
        redisUtil.clearAllTags();
        return true;
    }

    /*
     *
     * @since: 1.8
     * @description：博客管理搜索
     * @author: fgcy
     * @date: 2022/5/23
     */
    @Override
    public TempBlog searchPageByConditions(Integer page, String title, Integer typeId, Boolean recommend) {
        if (page == null) page = 1;
        Blog blog = new Blog();
        if (title != null || typeId != null || recommend != null) {
            blog.setTitle(title);
            blog.setRecommend(recommend);
            blog.setTypeId(typeId);
        }
        PageHelper.startPage(page, OFFSET);
        List<ExtendBlog> extendBlogs = blogMapper.getBlogByCondition(blog);
        PageInfo<ExtendBlog> pageInfo = new PageInfo<>(extendBlogs);
        TempBlog tempBlog = new TempBlog();
        tempBlog.setBlogs(pageInfo);
        return tempBlog;
    }


    /*
     *
     * @since: 1.8
     * @description：根据id获取博客
     * @author: fgcy
     * @date: 2022/5/23
     */
    @Override
    public ExtendBlog getBlogById(Long id) {
        if (Objects.isNull(id)) throw new RuntimeException("修改博客获取博客信息时博客id为空");
        ExtendBlog blog = blogMapper.getUpdateBlogInfoByBlogId(id);
        if (Objects.isNull(blog)) throw new RuntimeException("没有该博客");
        return blog;
    }


    /*
     *
     * @since: 1.8
     * @description：根据浏览量排序博客数据
     * @author: fgcy
     * @date: 2022/5/23
     */
    @Override
    public PageInfo<ExtendBlog> getIndexPage(Integer page) {
        if (page == null) page = 1;

        //页面数据缓存
        String jsonPageInfo = stringRedisTemplate.opsForValue().get("index:page:" + page);
        CacheBlogPageInfo cacheBlogPageInfo = null;
        PageInfo<ExtendBlog> finalPageInfo = new PageInfo<>();

        // TODO: 2022/5/29 redis中有没有cachePageInfo
        //没有页面数据缓存
        if (StrUtil.isBlank(jsonPageInfo)) {
            PageHelper.startPage(page, INDEX_SIZE);
            List<ExtendBlog> all = blogMapper.getExtendBlogOrderByCondition("view DESC,comment_count DESC,collect_count DESC");
            PageInfo<ExtendBlog> pageInfo = new PageInfo<>(all);
            pageInfo.setList(null);

            //从数据库中获取的cacheBlogPageInfo信息
            cacheBlogPageInfo = BeanUtil.copyProperties(pageInfo, CacheBlogPageInfo.class);

            List<Long> blogIds = all.stream().map(blog -> new Long(blog.getId())).collect(Collectors.toList());
            cacheBlogPageInfo.setList(blogIds);

            // TODO: 2022/5/29 从数据库中拿到，博客列表和分页信息 存储到redis中
            stringRedisTemplate.opsForValue().set("index:page:" + page, JSONUtil.toJsonStr(cacheBlogPageInfo));

        } else {
            //从redis中获得的cacheBlogPageInfo信息
            cacheBlogPageInfo = JSONUtil.toBean(jsonPageInfo, CacheBlogPageInfo.class);
            Assert.notNull(cacheBlogPageInfo, "从redis中获取的CacheBlogPageInfo为null");
            if (cacheBlogPageInfo.getList() == null || cacheBlogPageInfo.getList().isEmpty())
                throw new RuntimeException("从redis中获取的CacheBlogPageInfo的List为空");
        }

        //finalPageInfo是要返回的对象
        BeanUtil.copyProperties(cacheBlogPageInfo, finalPageInfo);
        finalPageInfo.getList().clear();
        List<Long> list = cacheBlogPageInfo.getList();

        for (Long blogId : list) {
            //存  取
            ExtendBlog extendBlog = redisUtil.getIfNotExistsThenSet(INDEX_BLOG + blogId, blogId, ExtendBlog.class,
                    id -> blogMapper.getExtendBlogByBlogId(id), 3L, TimeUnit.DAYS);
            finalPageInfo.getList().add(extendBlog);
        }

        return finalPageInfo;
    }


    /*
     *
     * @since: 1.8
     * @description：博客更新
     * @author: fgcy
     * @date: 2022/5/23
     */
    @Transactional
    @Override
    public ResultData updateBlogById(Blog blog) {
        Boolean flag = validUpdateBlogInfo(blog);
        if (!flag) throw new RuntimeException("更新博客数据验证失败!");
        int i = blogMapper.updateByPrimaryKeySelective(blog);
        if (i != 1) return ResultData.fail();

        stringRedisTemplate.delete(RedisConst.INDEX_BLOG + blog.getId());

        return ResultData.success(null);
    }


    /*
     *
     * @since: 1.8
     * @description：验证 限制更新的博客信息
     * @author: fgcy
     * @date: 2022/5/23
     */

    private Boolean validUpdateBlogInfo(Blog blog) {
        if (blog == null) return false;
        if (Objects.isNull(blog.getTitle()) || Objects.isNull(blog.getContent()) || Objects.isNull(blog.getFirstPicture()) || Objects.isNull(blog.getId()))
            return false;

        boolean flag = checkTags(blog);
        if (!flag) throw new RuntimeException("更新博客时，标签更新失败！");
        checkType(blog);

        blog.setUserId(null);
        blog.setGmtCreate(null);
        blog.setGmtModified(new Date());
        blog.setView(null);
        blog.setCollectCount(null);
        blog.setCollectCount(null);
        return true;
    }

    private void checkType(Blog blog) {
        Integer news = blog.getTypeId();
        Integer olds = getBlogById(blog.getId()).getTypeId();
        typeMapper.addTypeCount(news);
        typeMapper.reduceTypeCount(olds);
    }

    /*
     *
     * @since: 1.8
     * @description：检查是否更改标签
     * @author: fgcy
     * @date: 2022/5/25
     */
    private boolean checkTags(Blog blog) {
        //获取该博客为更改前的tagIds
        List<Integer> tags = tagMapper.findTagIdsByBlogId(blog.getId());
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < tags.size(); i++) {
            sb.append(tags.get(i)).append(i == tags.size() - 1 ? "" : ",");
        }


        //对比 看tagId是否被修改
        if (!(blog.getTagIds().equals(sb.toString()))) {
            //tagMapper.deleteBlogTagsByBlogId(blog.getId());

            //将新的tagIds加入中间表
            String tagIds = blog.getTagIds();
            String[] split = tagIds.split(",");
            List<String> asList = Arrays.asList(split);
            updateTagCount(asList, tags, blog.getId());
        }


        return true;
    }

    /*
     *
     * @since: 1.8
     * @description：更新博客后，更新标签
     * @author: fgcy
     * @date: 2022/5/27
     */
    public void updateTagCount(List<String> newTag, List<Integer> oldTags, Long blogId) {
        List<Long> news = new ArrayList<>();
        newTag.stream().filter(s -> {
            //去掉交集
            if (oldTags.contains(Integer.valueOf(s))) {
                oldTags.remove(Integer.valueOf(s));
                return false;
            }
            return true;
        }).forEach(s -> news.add(Long.valueOf(s)));

        tagMapper.addTagCount(news);
        tagMapper.reduceTagCount(oldTags);
        if (!oldTags.isEmpty()) blogMapper.deleteBlogTagByTagIds(oldTags, blogId);
        blogMapper.addBlogTags(newTag, blogId);
    }


    /*
     *
     * @since: 1.8
     * @description：添加博客时的数据校验和初始化
     * @author: fgcy
     * @date: 2022/5/23
     */
    private boolean validBlogInfo(Blog blog) {
        if (blog == null) return false;
        if (blog.getTitle() == null || blog.getContent() == null || blog.getFirstPicture() == null)
            return false;
        LoginUser user = (LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Assert.notNull(user, "未登录");
        blog.setUserId(user.getUser().getId());
        blog.setGmtCreate(new Date());
        blog.setGmtModified(new Date());
        blog.setCommentCount(0L);
        blog.setView(0);
        blog.setCollectCount(0L);
        blog.setStatus((byte) 0);
        if (StrUtil.isBlank(blog.getFlag())) blog.setFlag("原创");
        return true;
    }

    /*
     * 获取最新【10】的博客
     * @since: 1.8
     * @description：
     * @author: fgcy
     * @date: 2022/5/24
     */
    @Override
    public List<ExtendBlog> getLatestBlog(Integer size) {
        String order = "gmt_modified desc";
        List<Long> blogIds = null;
        // TODO: 2022/5/29 获取最新十条博客id
        String jsonBlogIds = stringRedisTemplate.opsForValue().get(INDEX_LATEST_TEN);
        List<ExtendBlog> indexPageBlogList = new ArrayList<>();

        // TODO: 2022/5/29 查数据库
        if (StrUtil.isBlank(jsonBlogIds)) {
            blogIds = blogMapper.getBlogsBySize(size, order);

            // TODO: 2022/5/29 将从数据库中的查到的博客ids放到redis
            stringRedisTemplate.opsForValue().set(INDEX_LATEST_TEN, JSONUtil.toJsonStr(blogIds));
        } else {
            blogIds = JSONUtil.toList(jsonBlogIds, Long.class);
        }

        // TODO: 2022/5/29 根据博客id查信息（redis或数据库）
        for (Long blogId : blogIds) {
            ExtendBlog extendBlog = redisUtil.getIfNotExistsThenSet(INDEX_BLOG + blogId, blogId, ExtendBlog.class,
                    id -> blogMapper.getExtendBlogByBlogId(id), 3L, TimeUnit.DAYS);
            indexPageBlogList.add(extendBlog);
        }
        return indexPageBlogList;
    }

    /*
     * 根据关键字搜索 标题 描述 内容
     * @since: 1.8
     * @description：
     * @author: fgcy
     * @date: 2022/5/24
     */

    @Override
    public PageInfo<ExtendBlog> searchBlogPageByQueryContent(String query, Integer page) {
        if (page == null) page = 1;
        PageHelper.startPage(page, INDEX_SIZE);
        List<ExtendBlog> all = blogMapper.queryByTitleOrContentAndDescription(query);
        PageInfo<ExtendBlog> pageInfo = new PageInfo<>(all);
        return pageInfo;
    }


    /*
     *
     * @since: 1.8
     * @description：根据博客id获取详细信息
     * @author: fgcy
     * @date: 2022/6/11
     */
    @Transactional
    @Override
    public ExtendBlog getDetailBlogById(Long id, HttpServletRequest request) {
        ExtendBlog blog = redisUtil.getIfNotExistsThenSet(INDEX_BLOG + id, id, ExtendBlog.class,
                blogId -> blogMapper.getExtendBlogByBlogId(blogId), 3L, TimeUnit.DAYS);

        if (blog == null) throw new RuntimeException("该id对应的博客不存在");

        Boolean isExists = stringRedisTemplate.opsForSet().isMember(IP + request.getRemoteAddr(), id.toString());
        // TODO: 2022/5/29 观看数加一
        if (!isExists) {
            stringRedisTemplate.opsForSet().add(IP + request.getRemoteAddr(), id.toString());
            //10分鐘内訪問同一篇博客觀看數仅仅加一
            stringRedisTemplate.expire(IP + request.getRemoteAddr(), 10L, TimeUnit.MINUTES);
            redisUtil.increanExtendBlogFieldCount(id, RedisUtil.VIEW);
        }

        String s = MarkDownUtil.markdownToHtmlExtensions(blog.getContent());
        blog.setContent(s);

        return blog;
    }


    /*
     *
     * @since: 1.8
     * @description：根据tagId查找博客
     * @author: fgcy
     * @date: 2022/6/11
     */
    @Override
    public PageInfo<ExtendBlog> getBlogPageByTagId(Integer id, Integer page) {
        if (id == null) throw new RuntimeException("根据标签id查找博客 id为空");
        if (page == null) page = 1;
        List<Long> blogIds = tagMapper.getBlogIdsByTagId(id);
        PageHelper.startPage(page, INDEX_SIZE);
        List<ExtendBlog> all = blogMapper.getBlogsByBlogIds(blogIds);
        PageInfo<ExtendBlog> pageInfo = new PageInfo<>(all);
        return pageInfo;
    }


    /*
     *
     * @since: 1.8
     * @description：根据typeId查找博客
     * @author: fgcy
     * @date: 2022/5/25
     */
    @Override
    public PageInfo<ExtendBlog> getBlogPageByTypeId(Long id, Integer page) {
        if (id == null) throw new RuntimeException("根据类型查找博客 id为空");
        if (page == null) page = 1;
        PageHelper.startPage(page, INDEX_SIZE);
        Blog blog = new Blog();
        blog.setTypeId(id.intValue());
        List<ExtendBlog> all = blogMapper.getBlogsByConditionId(blog);
        PageInfo<ExtendBlog> pageInfo = new PageInfo<>(all);
        return pageInfo;
    }


    /*
     *
     * @since: 1.8
     * @description：博客归档
     * @author: fgcy
     * @date: 2022/6/11
     */
    @Override
    public Map<String, List<Blog>> archiveBlog() {

        List<Blog> blogs = redisUtil.getIfNotExistsThenSetForList(INTERFILE, null,
                Blog.class, id -> {
                    List<Blog> allBlogs = blogMapper.getAllBlogs();
                    for (Blog blog : allBlogs) {
                        blog.setContent(null);
                        blog.setFirstPicture(null);
                        blog.setDescription(null);
                    }
                    return allBlogs;
                }, 10L, TimeUnit.DAYS);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy");

        Map<String, List<Blog>> map = new TreeMap<>(Comparator.reverseOrder());

        for (Blog blog : blogs) {
            String format = sdf.format(blog.getGmtModified());
            if (map.get(format) == null) {
                map.put(format, new ArrayList());
            }
            map.get(format).add(blog);
        }

        return map;
    }


    /*
     *
     * @since: 1.8
     * @description：获取全部博客数量
     * @author: fgcy
     * @date: 2022/6/11
     */
    @Override
    public Long getAllBlogsCount() {
        return blogMapper.getAllBlogCount();
    }


    @Transactional
    @Override
    public ResultData deleteBlogsByBlogId(Long id) {
        if (Objects.isNull(id)) return ResultData.fail().setMsg("逻辑删除博客时博客id为空");
        Blog blog = blogMapper.getBaseBlogForDelete(id);
        //删除博客时减少标签数量
        reduceTagcount(blog.getTagIds());

        //删除博客时减少类型数量
        reduceTypeCount(blog.getTypeId());

        //删除中间表blog_tag
        reducetagBlog(blog.getTagIds(), blog.getId());


        int i = blogMapper.deleteBlogByBlogId(id);
        if (i == 1) {
            redisUtil.clearIndexCache();
            redisUtil.clearAllTypes();
            redisUtil.clearAllTags();
            return ResultData.success(null);
        }
        return ResultData.fail().setMsg("删除失败");
    }


    @Override
    public boolean checkBlogCollection(Long userId, Long blogId) {
        if (Objects.isNull(userId) || Objects.isNull(blogId))
            throw new RuntimeException("检查是否收藏博客时发现userId或blogId为null");
        Long aLong = blogMapper.checkIsCollectionBlog(userId, blogId);
        if (Objects.isNull(aLong)) return false;
        return true;
    }


    /*
     *
     * @since: 1.8
     * @description：用户博客收藏
     * @author: fgcy
     * @date: 2022/6/12
     */
    @Override
    public boolean doUserCollection(Long userId, Long blogId) {
        if (Objects.isNull(userId) || Objects.isNull(blogId))
            throw new RuntimeException("经行博客收藏时发现userId或blogId为null");

        MUserCollection collection = blogMapper.getUserCollectionStatus(userId, blogId);
        //新建记录
        if (Objects.isNull(collection)) {
            createCollection(userId, blogId);
            redisUtil.increanExtendBlogFieldCount(blogId, RedisUtil.COLLECTION);

        } else {
            collection.setBlogId(blogId);
            updateCollection(collection);
        }
        return true;
    }

    @Override
    public void updateBlogCount(ExtendBlog extendBlog) {
        blogMapper.updateBlogCount(extendBlog);
    }

    private void updateCollection(MUserCollection collection) {
        if (collection.getStatus() == 0) {
            collection.setStatus((byte) 1);
            redisUtil.increanExtendBlogFieldCount(collection.getBlogId(), RedisUtil.COLLECTION);
        } else {
            collection.setStatus((byte) 0);
            redisUtil.reduceFieldCountByNum(collection.getBlogId(), RedisUtil.COLLECTION, 1L);
        }
        blogMapper.updateUserCollectionStatus(collection.getId(), collection.getStatus());

    }

    private void createCollection(Long userId, Long blogId) {
        Long postId = blogMapper.getUserIdByBlogId(blogId);
        MUserCollection mUserCollection = new MUserCollection();
        mUserCollection.setUserId(userId);
        mUserCollection.setBlogId(blogId);
        mUserCollection.setCreated(new Date());
        mUserCollection.setModified(new Date());
        mUserCollection.setPostUserId(postId);
        mUserCollection.setStatus((byte) 0);
        blogMapper.addUserCollection(mUserCollection);
    }

    private void reducetagBlog(String tagIds, Long id) {
        List<Integer> collect = Arrays.stream(tagIds.split(",")).map(Integer::new).collect(Collectors.toList());
        blogMapper.deleteBlogTagByTagIds(collect, id);
    }

    private void reduceTypeCount(Integer typeId) {
        typeMapper.reduceTypeCount(typeId);
    }

    /*
     *
     * @since: 1.8
     * @description：删除博客时减少标签数量
     * @author: fgcy
     * @date: 2022/6/11
     */
    private void reduceTagcount(String tagIds) {
        List<Integer> collect = Arrays.stream(tagIds.split(",")).map(Integer::new).collect(Collectors.toList());
        tagMapper.reduceTagCount(collect);
    }
}
