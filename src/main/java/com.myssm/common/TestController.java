package com.myssm.common;

import com.myssm.annocation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@MyController
public class TestController {
    @MyAutowried
    public TestService testService;

    @MyValue("${IntCount}")
    private int IntCount;
    @MyValue("${ShortCount}")
    private short ShortCount;
    @MyValue("${LongCount}")
    private Long LongCount;
    @MyValue("${FloatCount}")
    private float FloatCount;
    @MyValue("${DoubleCount}")
    private double DoubleCount;
    @MyValue("${BooleanFlag}")
    private boolean BooleanFlag;
    @MyValue("${ByteCount}")
    private byte ByteCount;
    @MyValue("${CharFlag}")
    private char CharFlag;


    @MyRequestMapping("/findAll")
    @MyResponseBody
    public List<User> add(){
        System.out.println(IntCount);
        System.out.println(ShortCount);
        System.out.println(DoubleCount);
        System.out.println(LongCount);
        System.out.println(ByteCount);
        System.out.println(BooleanFlag);
        System.out.println(CharFlag);
        System.out.println(FloatCount);
        return testService.findAll();
    }

    @MyRequestMapping("/select")
    @MyResponseBody
    public User select(String no){
        return testService.findByNo(no);
    }

    @MyRequestMapping("/add")
    public String add(HttpServletRequest request,User user){
        request.setAttribute("user",user);
        return "test2";
    }

    @MyRequestMapping("/register")
    public String register(){
        return "test1";
    }
}
