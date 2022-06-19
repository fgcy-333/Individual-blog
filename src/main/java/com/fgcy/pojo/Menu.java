package com.fgcy.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Menu {

    private long id;
    private String menuName;
    private String status;
    private long createBy;
    private java.sql.Timestamp createTime;
    private long updateBy;
    private java.sql.Timestamp updateTime;
    private long delFlag;
    private String remark;
    private String perms;


}
