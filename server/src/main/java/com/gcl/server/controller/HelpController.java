package com.gcl.server.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/other")
public class HelpController {

    @GetMapping("/help")
    public String help(HttpSession session){
        return "help";
    }
}
