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
public class User {

    private Long id;

    private String username;

    private String password;

    private String email;

    private String mobile;

    private String sign;

    private String wechat;

    private Date birthday;

    private String avatar;

    private Byte status;

    private Date gmtCreate;

    private Date gmtModified;

}
