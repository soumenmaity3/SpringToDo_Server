package com.soumen.SpringToDo.Controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class HelloController {
    @RequestMapping("hello")
    public String hello(){
        return "Hello All is ok";
    }
}