package com.soft.app.controller.app;

import com.soft.app.repository.AppUserFavoriteRepository;
import com.soft.app.spring.security.AuthorizeUtil;
import com.soft.app.util.BeanUtils;
import flexjson.JSONSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/api/appuserfavorite")
public class AppUserFavoriteRestController {
    @Autowired
    AuthorizeUtil authorizeUtil;

    @Autowired
    AppUserFavoriteRepository appUserFavoriteRepository;

    @GetMapping("findByUsername")
    public ResponseEntity findByUsername(@RequestParam(value = "username",required = false)String username) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        if(BeanUtils.isNull(username)){
            username=authorizeUtil.getUserName();
        }
        return new ResponseEntity<>(
                new JSONSerializer()
                        .prettyPrint(true)
//                        .include("id")
//                        .exclude("*")
                        .serialize(appUserFavoriteRepository.findByUsername(username))
                , headers, HttpStatus.OK);
    }
}
