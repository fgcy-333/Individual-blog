package com.fgcy.util;


import com.fgcy.service.CommentSservice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.web.context.ServletContextAware;

import javax.servlet.ServletContext;
import java.util.List;

@Component
public class ContextStartup implements ApplicationRunner, ServletContextAware {

    @Autowired
    CommentSservice commentSservice;
    //CategoryService categoryService;

    ServletContext servletContext;

/*    @Autowired
    PostService postService;*/

    @Override
    public void run(ApplicationArguments args) throws Exception {


        //commentSservice.initWeekRank();
    }

    @Override
    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
    }
}
