package com.fgcy.pojo;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;


@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class MUserCollection {
    private long id;
    private long userId;
    private long blogId;
    private long postUserId;
    private Date created;
    private Date modified;
    private byte status;


}
