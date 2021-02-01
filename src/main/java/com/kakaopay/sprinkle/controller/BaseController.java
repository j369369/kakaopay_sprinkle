package com.kakaopay.sprinkle.controller;

import io.swagger.annotations.Api;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.View;
import springfox.documentation.annotations.ApiIgnore;

@Controller
@ApiIgnore
public class BaseController {

    @RequestMapping("/")
    public String redirect(){
        return "redirect:swagger-ui.html";
    }
    @RequestMapping("/h2")
    public String redirect2(){
        return "redirect:h2-console";
    }
}
