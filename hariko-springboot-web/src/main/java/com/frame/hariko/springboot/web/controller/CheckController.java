package com.frame.hariko.springboot.web.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CheckController {

    @RequestMapping("/ngx/check")
    public String check(){
        return "success";
    }

}
