package com.edu.oauth2service.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
public class Oauth2WelcomeController {

    @RequestMapping({"/welcome"})
    public String welcome() {
        return "Welcome to Spring Security - Authentication Required";
    }

//    @RequestMapping(method = RequestMethod.GET, value = "/login")
//    public String login() {
//        return "login";
//    }

}
