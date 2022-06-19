package com.fgcy.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;


import java.util.Date;
import java.util.List;


@NoArgsConstructor
@ToString
@AllArgsConstructor
@Data
public class BCollection {
    private Long id;
    private String title;
    private Date updateTime;
    private String flag;
}
