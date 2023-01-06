package com.example.springSecurity.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/")
public class LoginController {

    @GetMapping("login")
    public String login() {
        return "login";
    }

    @GetMapping("courses")
    public String courses() {
        return "courses";
    }
}
