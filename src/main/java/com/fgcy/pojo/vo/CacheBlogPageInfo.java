package com.fgcy.pojo.vo;

import java.util.List;

/**
 * @Author fgcy
 * @Date 2022/5/29
 */
public class CacheBlogPageInfo {
    private Long total;
    private List<Long> list;
    private Long pageNum;
    private Long pages;
    private Boolean isFirstPage;
    private Boolean isLastPage;


    public Long getTotal() {
        return total;
    }

    public CacheBlogPageInfo setTotal(Long total) {
        this.total = total;
        return this;
    }

    public List<Long> getList() {
        return list;
    }

    public CacheBlogPageInfo setList(List<Long> list) {
        this.list = list;
        return this;
    }

    public Long getPageNum() {
        return pageNum;
    }

    public CacheBlogPageInfo setPageNum(Long pageNum) {
        this.pageNum = pageNum;
        return this;
    }

    public Long getPages() {
        return pages;
    }

    public CacheBlogPageInfo setPages(Long pages) {
        this.pages = pages;
        return this;
    }

    public Boolean getIsFirstPage() {
        return isFirstPage;
    }

    public CacheBlogPageInfo setIsFirstPage(Boolean firstPage) {
        isFirstPage = firstPage;
        return this;
    }

    public Boolean getIsLastPage() {
        return isLastPage;
    }

    public CacheBlogPageInfo setIsLastPage(Boolean lastPage) {
        isLastPage = lastPage;
        return this;
    }
}
