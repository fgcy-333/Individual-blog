package com.fgcy.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Blog {

    private Long id;

    private String title;

    private String firstPicture;

    private String flag;

    private Integer view;

    private Boolean shareStatement;

    private Boolean appreciation;

    private Boolean publiced;

    private Boolean commentabled;

    private Boolean recommend;

    private Date gmtCreate;

    private Date gmtModified;

    private Long userId;

    private Integer typeId;

    private String content;

    private String tagIds;

    private String description;

    private Long commentCount;

    private Long collectCount;

    private Byte status;

    @Override
    public String toString() {
        return "Blog{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", firstPicture='" + firstPicture + '\'' +
                ", flag='" + flag + '\'' +
                ", view=" + view +
                ", shareStatement=" + shareStatement +
                ", appreciation=" + appreciation +
                ", publiced=" + publiced +
                ", commentabled=" + commentabled +
                ", recommend=" + recommend +
                ", gmtCreate=" + gmtCreate +
                ", status=" + status +
                ", gmtModified=" + gmtModified +
                ", userId=" + userId +
                ", typeId=" + typeId +
                ", content='" + content + '\'' +
                ", tagIds='" + tagIds + '\'' +
                ", description='" + description + '\'' +
                ", commentCount=" + commentCount +
                ", collectCount=" + collectCount +
                '}';
    }
}
