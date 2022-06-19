package com.fgcy.pojo.vo;

import com.fgcy.pojo.Blog;
import com.fgcy.pojo.Tag;
import com.fgcy.pojo.Type;
import com.fgcy.pojo.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;


import java.util.List;

/**
 * @Author fgcy
 * @Date 2022/5/22
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExtendBlog extends Blog {
    private Type type;
    private List<Tag> tags;
    private User user;


/*    public User getUser() {
        return user;
    }

    public ExtendBlog setUser(User user) {
        this.user = user;
        return this;
    }*/

    @Override
    public String toString() {
        return "ExtendBlog{" +
                "id=" + getId() +
                ", title='" + getTitle() + '\'' +
                ", firstPicture='" + getFirstPicture() + '\'' +
                ", flag='" + getFlag() + '\'' +
                ", view=" + getView() +
                ", status=" + getStatus() +
                ", shareStatement=" + getShareStatement() +
                ", appreciation=" + getAppreciation() +
                ", publiced=" + getPubliced() +
                ", commentabled=" + getCommentabled() +
                ", recommend=" + getRecommend() +
                ", gmtCreate=" + getGmtCreate() +
                ", gmtModified=" + getGmtModified() +
                ", userId=" + getUserId() +
                ", typeId=" + getTypeId() +
                ", content='" + getContent() + '\'' +
                ", tagIds='" + getTagIds() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", commentCount=" + getCommentCount() +
                ", collectCount=" + getCollectCount() +
                ",type=" + type +
                ", tags=" + tags +
                ", user=" + user +
                '}';


    }

}
