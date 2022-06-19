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
public class BComment {
    private Long id;

    private Long parentId;

    private Long blogId;

    private Long userId;

    private Byte status;

    private Date created;

    private String content;

    @Override
    public String toString() {
        return "BComment{" +
                "id=" + id +
                ", parentId=" + parentId +
                ", blogId=" + blogId +
                ", userId=" + userId +
                ", status=" + status +
                ", created=" + created +
                ", content='" + content + '\'' +
                '}';
    }
}
