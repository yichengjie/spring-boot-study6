package com.yicj.study.jpa.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@NoArgsConstructor
public class User {
    @Id
    // 主键策略
    //IDENTITY: 使用数据库表的主键策略
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id ;

    // 用户名称
    @Column
    private String name ;

    // 创建时间
    @Column(name = "create_time")
    private Date createTime ;

    // 部门id
    @ManyToOne
    @JoinColumn(name = "department_id")
    private Department department ;
}
