package com.maizhong.portal.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;

/**
 * Created by Administrator on 2017/3/15.
 */
@RequestMapping("/test")
@Controller
public class TestController {



    @RequestMapping("/to/{url}")
    public String test(@PathVariable("url") String url){
        return url;
    }

}