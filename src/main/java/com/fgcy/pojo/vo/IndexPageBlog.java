package com.fgcy.pojo.vo;

import com.fgcy.pojo.Type;
import com.fgcy.pojo.User;

import java.util.Date;

/**
 * @Author fgcy
 * @Date 2022/5/29
 */
public class IndexPageBlog {
    private Long id;
    private String firstPicture;
    private String description;
    private String title;
    private User user;
    private Long view;
    private Date modifiedTime;
    private Long commentCount;
    private Long collectionCount;
    private Type type;

    @Override
    public String toString() {
        return "IndexPageBlog{" +
                "id=" + id +
                ", firstPicture='" + firstPicture + '\'' +
                ", description='" + description + '\'' +
                ", title='" + title + '\'' +
                ", user=" + user +
                ", view=" + view +
                ", modifiedTime=" + modifiedTime +
                ", commentCount=" + commentCount +
                ", collectionCount=" + collectionCount +
                ", type=" + type +
                '}';
    }

    public Long getId() {
        return id;
    }

    public IndexPageBlog setId(Long id) {
        this.id = id;
        return this;
    }

    public String getFirstPicture() {
        return firstPicture;
    }

    public IndexPageBlog setFirstPicture(String firstPicture) {
        this.firstPicture = firstPicture;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public IndexPageBlog setDescription(String description) {
        this.description = description;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public IndexPageBlog setTitle(String title) {
        this.title = title;
        return this;
    }

    public User getUser() {
        return user;
    }

    public IndexPageBlog setUser(User user) {
        this.user = user;
        return this;
    }

    public Long getView() {
        return view;
    }

    public IndexPageBlog setView(Long view) {
        this.view = view;
        return this;
    }

    public Date getModifiedTime() {
        return modifiedTime;
    }

    public IndexPageBlog setModifiedTime(Date modifiedTime) {
        this.modifiedTime = modifiedTime;
        return this;
    }

    public Long getCommentCount() {
        return commentCount;
    }

    public IndexPageBlog setCommentCount(Long commentCount) {
        this.commentCount = commentCount;
        return this;
    }

    public Long getCollectionCount() {
        return collectionCount;
    }

    public IndexPageBlog setCollectionCount(Long collectionCount) {
        this.collectionCount = collectionCount;
        return this;
    }

    public Type getType() {
        return type;
    }

    public IndexPageBlog setType(Type type) {
        this.type = type;
        return this;
    }
}
