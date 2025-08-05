package com.deutsche.demo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

//    http://localhost:8080/hello

//    http requests -
//    get, post, put, delete, ...

    @GetMapping("hello")
    public String hello() {
        System.out.println("hello");
        return "Hello world!";
    }

    @GetMapping("hi")
    public String hi() {
        System.out.println("hi");
        return "Hi! How're you?";
    }
    //    @RequestMapping("hello")
//    public String hello() {
//        System.out.println("hello");
//        return "Hello world!";
//    }

//    @RequestMapping
//    public String hello() {
//        System.out.println("hello");
//        return "Hello world!";
//    }
}