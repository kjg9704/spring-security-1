package com.example.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(description = "로그인 확인용 페이지")
@RestController
public class HelloController {

	@ApiOperation(value = "로그인 확인용 페이지")
    @GetMapping("/hello")
    public String hello(){
        return "hello world!";
    }
}