package com.joaovidal.receitron.adapter.in.web;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("test/")
public class TestController {

    @GetMapping("valeu")
    public String test() {
        return "VALEU";
    }
}
