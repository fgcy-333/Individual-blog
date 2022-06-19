package com.fgcy.util;

/**
 * @Author fgcy
 * @Date 2022/5/28
 */
public interface RedisConst {
    String ALL_TAGS = "all:tags";
    String ALL_TYPES = "all:types";

    //完整博客信息，三天过期
    String BLOG_CACHE = "blog:";
    String INDEX_BLOG = "index:blog:";

    String INDEX_LATEST_TEN = "index:latest10";

    String IP = "ip:";

    String LOGIN_USER = "login:user:";

    String VALIDATE_CODE_ID = "validate_code_id:";

    String NEED_TO_UPDATE = "need_to_update";
    String INTERFILE = "interfile";
}
