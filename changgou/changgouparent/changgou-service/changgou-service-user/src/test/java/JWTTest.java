import io.jsonwebtoken.*;
import org.apache.commons.collections.map.LinkedMap;
import org.junit.Test;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties;

import java.util.*;
import java.io.UnsupportedEncodingException;

public class JWTTest {
 @Test
 public void demo() throws UnsupportedEncodingException {
     String msg="com.qingfeng";
     byte[] encode = Base64.getEncoder().encode(msg.getBytes());
     String s = new String(encode, "utf-8");
     System.out.println(encode);
     System.out.println(s);

     byte[] decode = Base64.getDecoder().decode(s.getBytes());
     String s1 = new String(decode, "utf-8");
     System.out.println(s1);
 }

 @Test
    public void demo2() throws Exception{
     JwtBuilder builder = Jwts.builder();
     Map<String, Object> map = new HashMap<>();
     map.put("alg", "HS256");
     map.put("keyId", "JWT");

     // 构建 头部信息
  builder.setHeader(map);
     // 构建载荷
     builder.setId("666");
     builder.setIssuer("zhangsan");
     builder.setIssuedAt(new Date());
     builder.setExpiration(new Date(System.currentTimeMillis()+40000));

     // 自定义载荷
     Map<String,Object> map2 =new HashMap<>();
     map2.put("lll","ss");
     builder.setClaims(map2);
     // 构建签证
     builder.signWith(SignatureAlgorithm.HS256, "itheima");
     String token=builder.compact();
     System.out.println("token:"+token);


 }

 @Test
    public void jiemi(){
      String token ="eyJrZXlJZCI6IkpXVCIsImFsZyI6IkhTMjU2In0.eyJqdGkiOiI2NjYiLCJpc3MiOiJ6aGFuZ3NhbiIsImlhdCI6MTU2NjQwMDk5NSwiZXhwIjoxNTY2NDAxMDM1fQ.rqi_83QYzCA7cagaeSLgLBovAnzIznoVOnikVfFa09c";
     JwtParser parser = Jwts.parser();
     JwtParser itheima = parser.setSigningKey("itheima");
     Claims body = parser.parseClaimsJws(token).getBody();
     System.out.println(body);


 }
 @Test
    public void demo3(){
     Map<String,String> hashmap=new HashMap<>();
     hashmap.put("k","kkk");
     hashmap.put("k","kkk2");
     System.out.println(hashmap);
 }
}
