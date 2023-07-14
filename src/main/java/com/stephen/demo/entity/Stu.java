package com.stephen.demo.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author <a href="shishouchao@foresee.com.cn">shouchao.shi</a>
 * @version 1.0.0
 * @date 2023/06/08 10:36
 * @Description
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Stu {

    private Long id;

    private String nd;

    private String comment;

    private Date createDate;


}
