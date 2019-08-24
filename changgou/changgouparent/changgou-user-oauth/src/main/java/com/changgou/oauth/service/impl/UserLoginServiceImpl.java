package com.changgou.oauth.service.impl;

import com.changgou.oauth.service.UserLoginService;
import com.changgou.oauth.util.AuthToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.UnsupportedEncodingException;
import java.util.Base64;
import java.util.Map;

/**
 * @ClassName UserLoginServiceImpl
 * @Description
 * @Author 传智播客
 * @Date 12:18 2019/8/22
 * @Version 2.1
 **/
@Service
public class UserLoginServiceImpl implements UserLoginService {


    @Autowired
    private RestTemplate restTemplate;

//    @Autowired
//    private DiscoveryClient discoveryClient;

    @Autowired
    private LoadBalancerClient loadBalancerClient;

    /**
     * @author 栗子
     * @Description 授权操作-发请求
     * @Date 12:23 2019/8/22
     * @param username
     * @param password
     * @param clientId
     * @param clientSecret
     * @param grant_type
     * @return void
     **/
    @Override
    public AuthToken login(String username, String password, String clientId, String clientSecret, String grant_type) {

//        String url = "http://localhost:9001/oauth/token";
        // 请求url拼接
        ServiceInstance serviceInstance = loadBalancerClient.choose("user-auth");
        String uri = serviceInstance.getUri().toString();
        String url = uri + "/oauth/token";


        // 请求体（数据）
        MultiValueMap body = new LinkedMultiValueMap();
        body.add("username", username);
        body.add("password", password);
        body.add("grant_type", grant_type);

        MultiValueMap headers = new LinkedMultiValueMap();
        byte[] encode = Base64.getEncoder().encode((clientId + ":" + clientSecret).getBytes());
        String encodeMsg = null;
        try {
            encodeMsg = new String(encode, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        headers.add("Authorization", "Basic " + encodeMsg);
        HttpEntity httpEntity = new HttpEntity(body, headers);

        // 发请求，arg0:请求的url  arg1:请求方式  arg2:请求体（数据）  arg0：响应结果
        ResponseEntity<Map> responseEntity = restTemplate.exchange(url, HttpMethod.POST, httpEntity, Map.class);
        Map map = responseEntity.getBody();

        // 将令牌信息封装到AuthToken中
        AuthToken authToken = new AuthToken();
        authToken.setAccessToken(map.get("access_token").toString());   // 生成的令牌
        authToken.setRefreshToken(map.get("refresh_token").toString());  // 刷新令牌
        authToken.setJti(map.get("jti").toString());                        // 短令牌
        return authToken;
    }
}
