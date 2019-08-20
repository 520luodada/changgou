package com.changgou.search.controller;

import com.changgou.entity.Result;
import com.changgou.entity.StatusCode;
import com.changgou.search.pojo.SkuInfo;
import com.changgou.search.service.SkuInfoService;
import com.changgou.search.service.impl.SkuInfoServiceImpl;
import com.netflix.discovery.converters.Auto;
import jdk.net.SocketFlow;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/skuinfo")
public class SkuInfocontroller {
    @Autowired(required = false)
    private SkuInfoService skuInfoService;
    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;
@GetMapping("/import")
    public Result importSkutoEs(){
      skuInfoService.importSkutoEs();
return new Result(true, StatusCode.OK,"导入es成功");
    }

@GetMapping("/search")
    public Map<String,Object> search(@RequestParam(required = false) Map<String,String> map){
    Map<String, Object> searchmap = skuInfoService.searchmap(map);
    return  searchmap;
}
}
