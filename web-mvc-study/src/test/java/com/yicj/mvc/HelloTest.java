package com.yicj.mvc;

import org.junit.Test;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class HelloTest {


    interface AHello{
        void hello() ;
    }

    interface BHello{
        void hello() ;
    }
    class AHelloImpl implements AHello{
        @Override
        public void hello(){
            System.out.println("hello A");
        }
    }

    class BHelloImpl implements BHello{
        @Override
        public void hello(){
            System.out.println("hello B");
        }
    }

    class ABHelloAdapter implements AHello, BHello{
        private AHello aHello ;
        private BHello bHello ;
        public ABHelloAdapter(AHello aHello, BHello bHello){
            this.aHello = aHello ;
            this.bHello = bHello ;
        }
        @Override
        public void hello() {
            if (this instanceof AHello){
                aHello.hello();
            }

            if(this instanceof BHello) {
                bHello.hello();
            }
        }
    }

    @Test
    public void test1(){
        AHello aHello ;
        BHello bHello ;
        ABHelloAdapter abHelloAdapter = new ABHelloAdapter(new AHelloImpl(), new BHelloImpl()) ;
        //abHelloAdapter.hello();
        bHello = abHelloAdapter ;
        bHello.hello();
    }

    @Test
    public void test2(){
        String a="A",b="B",c="C",d="D",e="E";
        List<String> list=new LinkedList<String>();
        list.add(a);
        list.add(e);
        list.add(d);
        Iterator<String> first=list.iterator();
        System.out.println("修改前集合中的元素是：");
        while(first.hasNext()){
            System.out.print(first.next()+" ");
        }
        //list.set(1, b);
        list.add(2,c);
        Iterator second=list.iterator();
        System.out.println("修改集合后的元素是：");
        while(second.hasNext()){
            System.out.print(second.next()+"    ");
        }
    }

    @Test
    public void testRandom(){
        Random random = new Random() ;
        int i = random.nextInt(100);
        System.out.println(i);
    }

    public String hello(){
        return "hello" ;
    }

    public String hello(String name){
        return "hello" ;
    }

    public String hello(Integer age){
        return "hello" ;
    }

    public String hello(Object object){
        return "hello" ;
    }
}
