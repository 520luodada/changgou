package com.changgou.goods.feign;

import com.changgou.entity.Result;
import com.changgou.goods.pojo.Sku;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "goods")
@RequestMapping("/sku")
public interface SkuFeign {
    @GetMapping("/searchByStatus/{status}/{start}/{end}")
    public Result<List<Sku>> searchByStatus(@PathVariable(value = "status") String status,@PathVariable(value = "start") Integer start,@PathVariable(value = "end") Integer end);

    @PostMapping(value = "/search" )
    public Result<List<Sku>> findList(@RequestBody(required = false)  Sku sku);

}