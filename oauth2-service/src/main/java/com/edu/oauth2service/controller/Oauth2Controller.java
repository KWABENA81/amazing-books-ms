package com.edu.oauth2service.controller;

import com.edu.oauth2service.config.OauthService;
import com.edu.oauth2service.entity.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping
public class Oauth2Controller {
    @Autowired
    private OauthService oauthService;

    @GetMapping({"/welcome"})
    public String welcome() {
        return "Welcome to Spring Security - Authentication Required";
    }

    @GetMapping( "/login")
    public String login() {
        return "login";
    }

    @PutMapping("/addUser")
    public String addNewUser(@RequestBody UserInfo userInfo) {
        return oauthService.addUser(userInfo);
    }
}
