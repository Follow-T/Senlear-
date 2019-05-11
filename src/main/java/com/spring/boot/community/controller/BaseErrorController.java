package com.spring.boot.community.controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class BaseErrorController implements ErrorController {
	
    @Override
    public String getErrorPath() {
        return "/error";
    }

    @RequestMapping("/error")
    public String error() {
        return getErrorPath();
    }

}
