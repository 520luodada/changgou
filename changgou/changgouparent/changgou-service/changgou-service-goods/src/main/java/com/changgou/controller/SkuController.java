package com.changgou.controller;

import com.changgou.entity.Result;
import com.changgou.entity.StatusCode;
import com.changgou.goods.pojo.Sku;

import com.changgou.service.SkuService;
import com.github.pagehelper.PageInfo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/****
 * @Author:shenkunlin
 * @Description:
 * @Date 2019/6/14 0:18
 *****/

@RestController
@RequestMapping("/sku")
@CrossOrigin
public class SkuController {

    @Autowired(required = false)
    private SkuService skuService;


    //  修改sku商品数量1
    @PutMapping("/updateSkuWeightByIdForSku/{id}")
    public Result updateSkuWeightByIdForSku(@RequestBody Sku sku){
        skuService.updateSkuWeightByIdForSku(sku);
        return  new Result(true,StatusCode.OK,"更改商品数量成功");
    }
    //  修改sku商品数量2  通过
    @PutMapping("/updateSkuWeightById/{id}")
    public Result updateSkuWeightById(@RequestParam String weight,@PathVariable(value = "id")long id){
        System.out.println("ooooooo------");
        System.out.println(weight+"_____"+id);
        skuService.updateSkuWeightById(Integer.valueOf(weight),id);
return  new Result(true,StatusCode.OK,"更改商品数量成功");
    }

    // 查找spu下的商品列表
    @PostMapping("/listsku/{spuId}")
    public  Result<List<Sku>> listSku(@PathVariable(value = "spuId") long spuId){
        List<Sku> skus = skuService.listSku(spuId);
        return  new Result<>(true,StatusCode.ACCESSERROR,"查询成功",skus);
    }

    /***
     * Sku分页条件搜索实现
     * @param sku
     * @param page
     * @param size
     * @return
     */
    @PostMapping(value = "/search/{page}/{size}" )
    public Result<PageInfo> findPage(@RequestBody(required = false)  Sku sku, @PathVariable  int page, @PathVariable  int size){
        //调用SkuService实现分页条件查询Sku
        PageInfo<Sku> pageInfo = skuService.findPage(sku, page, size);
        return new Result(true, StatusCode.OK,"查询成功",pageInfo);
    }

    /***
     * Sku分页搜索实现
     * @param page:当前页
     * @param size:每页显示多少条
     * @return
     */
    @GetMapping(value = "/search/{page}/{size}" )
    public Result<PageInfo> findPage(@PathVariable  int page, @PathVariable  int size){
        //调用SkuService实现分页查询Sku
        PageInfo<Sku> pageInfo = skuService.findPage(page, size);
        return new Result<PageInfo>(true,StatusCode.OK,"查询成功",pageInfo);
    }

    /***
     * 多条件搜索品牌数据
     * @param sku
     * @return
     */
    @PostMapping(value = "/search" )
    public Result<List<Sku>> findList(@RequestBody(required = false)  Sku sku){
        //调用SkuService实现条件查询Sku
        List<Sku> list = skuService.findList(sku);
        return new Result<List<Sku>>(true,StatusCode.OK,"查询成功",list);
    }

    /***
     * 根据ID删除品牌数据
     * @param id
     * @return
     */
    @DeleteMapping(value = "/{id}" )
    public Result delete(@PathVariable Long id){
        //调用SkuService实现根据主键删除
        skuService.delete(id);
        return new Result(true,StatusCode.OK,"删除成功");
    }

    /***
     * 修改Sku数据
     * @param sku
     * @param id
     * @return
     */
    @PutMapping(value="/{id}")
    public Result update(@RequestBody  Sku sku,@PathVariable Long id){
        //设置主键值
        sku.setId(id);
        //调用SkuService实现修改Sku
        skuService.update(sku);
        return new Result(true,StatusCode.OK,"修改成功");
    }

    /***
     * 新增Sku数据
     * @param sku
     * @return
     */
    @PostMapping
    public Result add(@RequestBody   Sku sku){
        //调用SkuService实现添加Sku
        skuService.add(sku);
        return new Result(true,StatusCode.OK,"添加成功");
    }

    /***
     * 根据ID查询Sku数据
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public Result<Sku> findById(@PathVariable Long id){
        //调用SkuService实现根据主键查询Sku
        Sku sku = skuService.findById(id);
        return new Result<Sku>(true,StatusCode.OK,"查询成功",sku);
    }

    /***
     * 查询Sku全部数据
     * @return
     */
    @GetMapping
    public Result<List<Sku>> findAll(){
        //调用SkuService实现查询所有Sku
        List<Sku> list = skuService.findAll();
        return new Result<List<Sku>>(true, StatusCode.OK,"查询成功",list) ;
    }
}
