package com.yicj.study.classloading;

import java.util.ArrayList;
import java.util.List;

public class GenericTypes {
    public static String method(List<String> list){
        System.out.println("invoke method (List<String> list)");
        return "abc" ;
    }
    /*public static int method(List<Integer> list){
        System.out.println("invoke method (List<Integer> list)");
        return 1 ;
    }*/
    public static void main(String[] args) {
        method(new ArrayList<String>()) ;
        //List<Integer> list = [1,2,3,4,5] ;
        //method(new ArrayList<Integer>()) ;
    }
}
