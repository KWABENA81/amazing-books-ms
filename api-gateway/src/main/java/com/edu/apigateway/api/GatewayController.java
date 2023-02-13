package com.edu.apigateway.api;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
public class GatewayController {
    @GetMapping(path = "/gateway")
    public String welcome() {
        return "Edureka Spring Security - Authentication Required";
    }

//    @RequestMapping(method = RequestMethod.GET, value = "/login")
//    public String login() { return "login"; }
}


