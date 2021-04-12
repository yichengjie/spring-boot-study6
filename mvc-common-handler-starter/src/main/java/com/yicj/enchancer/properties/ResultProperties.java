package com.yicj.enchancer.properties;

import lombok.Data;

@Data
public class ResultProperties{
    private String statusFieldName = "code";
    private String tipFieldName = "msg";
    private String dataFieldName = "obj";
}