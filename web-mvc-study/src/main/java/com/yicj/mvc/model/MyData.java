package com.yicj.mvc.model;

import lombok.Data;

import javax.xml.bind.annotation.XmlRootElement;

@Data
@XmlRootElement
public class MyData {
    private String firstName ;
    private String lastName ;
}
