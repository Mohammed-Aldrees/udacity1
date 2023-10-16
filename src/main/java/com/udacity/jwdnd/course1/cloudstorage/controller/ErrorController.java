package com.udacity.jwdnd.course1.cloudstorage.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/errors")
public class ErrorController {

    @GetMapping
    public String errorRedirect() {
        return "redirect:/result?status=-1";
    }

    @PostMapping
    public String errorPostRedirect() {
        return "redirect:/result?status=-1";
    }
}
