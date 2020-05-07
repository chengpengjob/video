package com.cp.video.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author feipeng
 * @site www.gcp168.cn
 * @create 2020-04-28 22:12
 */
@RestController
public class HelloController {
    @GetMapping("/hello")
    public String hello(){
        return "hello";
    }
}
