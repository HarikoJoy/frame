package com.frame.hariko.springboot.web.swagger2.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class SwaggerUiController {
    @RequestMapping("/docs")
    public ModelAndView docs(){
        return new ModelAndView("redirect:/swagger-ui.html");
    }
}
