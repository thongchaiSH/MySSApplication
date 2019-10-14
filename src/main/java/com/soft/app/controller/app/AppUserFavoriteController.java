package com.soft.app.controller.app;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("appuserfavorite")
public class AppUserFavoriteController {

    @GetMapping("")
    public String index() {
        return "appuserfavorites/index";
    }
}
