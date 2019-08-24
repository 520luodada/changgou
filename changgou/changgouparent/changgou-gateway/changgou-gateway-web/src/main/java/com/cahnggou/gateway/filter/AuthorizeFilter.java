package com.cahnggou.gateway.filter;

import com.changgou.entity.JwtUtil;
import com.changgou.entity.StatusCode;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.net.Authenticator;

@Component
public class AuthorizeFilter implements GlobalFilter, Ordered {
    private static final String AUTHORIZE_TOKEN = "Authorization";

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // 定义常量
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();

        // 判断用户是否是登入操作  是 直接放行
        String url = request.getURI().getPath();
        if(url.startsWith("/api/user/login")){
            return chain.filter(exchange);
        }

        // 如果是其他请求 则判断用户是否已经登入 （是否携带token）
        // 从请求头上获取token
        String token = request.getQueryParams().getFirst(AUTHORIZE_TOKEN);
        if (StringUtils.isEmpty(token)){
            token =request.getHeaders().getFirst(AUTHORIZE_TOKEN);
        }
        // 从cooike 找
        if (StringUtils.isEmpty(token)){
            HttpCookie cookie = request.getCookies().getFirst(AUTHORIZE_TOKEN);
            if (cookie != null) {
              token=cookie.getValue();
            }
        }
        if(StringUtils.isEmpty(token)){
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            return response.setComplete();
        }

        // 如果token存在 则解析
        try {
//            Claims claims = JwtUtil.parseJWT(token);
//request.mutate().header("AUTHORIZE_TOKEN",token);
        request.mutate().header(AUTHORIZE_TOKEN,"bearer "+token);
        } catch (Exception e) {
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            e.printStackTrace();
            return response.setComplete();

        }

        return chain.filter(exchange);
    }
// 现在配置的效果是 只要配置了路由。路由配置中-path的路径的  都会经过过滤器
    // 像很多页面都是不需要登录可以使用的  所以 有些可以排除
    // 思路  ：通过@value(""${- Path})得到 所以路径  人后 spilt 用等号切割
    //  得到数组   数组【2】得到路径  人后得到后面地址：然后根据【，】切割
    //如果路径是 m某某开头  直接放行  string.startwith:




    @Override
    public int getOrder() {
        return 0;
    }
}
