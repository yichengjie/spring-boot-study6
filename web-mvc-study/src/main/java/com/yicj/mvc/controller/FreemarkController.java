package com.yicj.mvc.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class FreemarkController {

    @RequestMapping("/freemarkView")
    public String fpage(Model model) {
        return "fpage";
    }
}