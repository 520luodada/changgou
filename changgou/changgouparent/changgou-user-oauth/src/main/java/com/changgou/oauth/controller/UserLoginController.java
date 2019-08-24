package com.changgou.oauth.controller;

import com.changgou.oauth.service.UserLoginService;
import com.changgou.oauth.util.AuthToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/user")
public class UserLoginController {
@Autowired
private UserLoginService userLoginService;
    @Value("${auth.clientId}")
    String clientId;
    @Value("${auth.clientSecret}")
    String clientSecret;
    @Value("${auth.cookieDomain}")
    String cookieDomain;
    @Value("${auth.cookieDomain}")
    String cookieMaxAge;

    @PostMapping("/login")
    public AuthToken login(String username, String password, HttpServletResponse response){
        String grant_type="password";
     AuthToken authToken= userLoginService.login(username, password, clientId, clientSecret, grant_type);
        String accessToken = authToken.getAccessToken();
        Cookie cookie = new Cookie("Authorization",accessToken);
        cookie.setDomain("localhost");
        cookie.setPath("/");
        response.addCookie(cookie);
        return authToken;


    }
}
