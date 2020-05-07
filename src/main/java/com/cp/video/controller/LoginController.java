package com.cp.video.controller;

import com.cp.video.model.RespBean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author feipeng
 * @site www.gcp168.cn
 * @create 2020-04-28 22:46
 */
@RestController
public class LoginController {
    @GetMapping("/login")
    //@CrossOrigin("*")
    public RespBean login(){
        return RespBean.error("尚未登录，请登录！");
    }

}
