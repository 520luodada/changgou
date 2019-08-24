package com.changgou.search.controller;
import	java.util.Date;


import com.changgou.entity.Page;
import com.changgou.search.feign.SearchFeign;
import com.changgou.search.pojo.SkuInfo;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;
import java.util.Set;

@Controller
@RequestMapping("/websearch")
public class SearchWebController {
    @Autowired
    private SearchFeign searchFeign;
        @RequestMapping("/resultList")
    public String list(@RequestParam(required = false) Map<String,String> searchmap, Model model){

            Map<String, Object> result = searchFeign.search(searchmap);

            model.addAttribute("now",new Date());
            model.addAttribute("searchmap",searchmap);
            model.addAttribute("resultmap",result);
            String url=getUrl(searchmap);
            model.addAttribute("url",url);
            // 获取分页
            Page<SkuInfo> page=new Page<>(
                Long.parseLong(result.get("totalElements").toString()),
               Integer.parseInt(result.get("pageNum").toString()),
                Integer.parseInt(result.get("pageSize").toString())
            );

          model.addAttribute("page", page);
            return "search";
        }

    private String getUrl(Map<String, String> searchmap) {

        System.out.println(searchmap);
            String url="/websearch/resultList";
            if (searchmap.size()>0){
                url+="?";
                Set<Map.Entry<String, String>> entries = searchmap.entrySet();

                for (Map.Entry<String, String> entry : entries) {

                    String key = entry.getKey();
                    String value = entry.getValue();
                    if (key.equals("pageNum")){
                        continue;
                    }
                    url+=key+"="+value+"&";
                }
             url=url.substring(0,url.length()-1);

            }
            return url;
    }
}
