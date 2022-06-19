package com.fgcy.pojo.vo;

import com.fgcy.mapper.BCommentMapper;
import com.fgcy.pojo.BComment;
import com.fgcy.pojo.User;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


/**
 * @Author fgcy
 * @Date 2022/6/7
 */
@Data
@NoArgsConstructor
public class ExtendComment extends BComment {
    private List<ExtendComment> replyComments;
    private BaseUser user;
    //父评论发布者名称
    private String parentName;
    private Boolean adminComment;

    @Override
    public String toString() {
        return "ExtendComment{" +
                "id=" + getId() +
                ", parentId=" + getParentId() +
                ", parentName=" + parentName +
                ", adminComment=" + adminComment +
                ", blogId=" + getBlogId() +
                ", userId=" + getUserId() +
                ", status=" + getStatus() +
                ", created=" + getCreated() +
                ", content='" + getContent() + '\'' +
                "replyComments=" + replyComments +
                ", user='" + user + '\'' +
                '}';
    }
}
