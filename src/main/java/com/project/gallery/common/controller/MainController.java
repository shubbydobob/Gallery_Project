package com.project.gallery.common.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller
public class MainController {

    @GetMapping(value = {"/", "{path:[^.]*}", "{path1:[^.]*}, {path2:[^.]*}"})
    public String index() {
        return "/index.html";
    }


}
