package com.thymeleaf.controller;

import com.sun.javafx.tk.TKPulseListener;
import com.thymeleaf.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@Controller
public class ThymealController {
    @RequestMapping("/demo")
    public  String demo(Model model){
        model.addAttribute("hello","hello,word");
        List<User> users = new ArrayList<User>();
        users.add(new User(1,"张三","深圳"));
        users.add(new User(2,"李四","北京"));
        users.add(new User(3,"王五","武汉"));
        model.addAttribute("users",users);
        Map<String,Object> dataMap = new HashMap<String,Object>();
        dataMap.put("No","123");
        dataMap.put("address","深圳");
        model.addAttribute("dataMap",dataMap);
        String[] names = {"张三","李四","王五"};
        model.addAttribute("names",names);

        String[] s = {"张三","李四","王五"};
        model.addAttribute("names",names);

        model.addAttribute("now",new Date());
        model.addAttribute("age",80);
        return "demo01";
    }
    @RequestMapping("/demo1")
    public  String demo1(Model model){
        model.addAttribute("hello","hello,word");
        return "footer";
    }
}
