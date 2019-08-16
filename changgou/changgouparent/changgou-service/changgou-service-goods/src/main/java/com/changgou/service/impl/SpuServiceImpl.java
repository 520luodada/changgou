package com.changgou.service.impl;


import com.changgou.dao.SkuMapper;
import com.changgou.dao.SpuMapper;
import com.changgou.goods.pojo.Spu;
import com.changgou.service.SpuService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/****
 * @Author:shenkunlin
 * @Description:Spu业务层接口实现类
 * @Date 2019/6/14 0:16
 *****/
@Service
public class SpuServiceImpl implements SpuService {

    @Autowired(required = false)
    private SpuMapper spuMapper;

    @Override
    public void pull(long spuId) {
        Spu spu = spuMapper.selectByPrimaryKey(spuId);

        //物理删除  判断这个商品是否存在
        if (spu==null){
            return;
        }else {

            // 逻辑删除  判断isdelete 是否为0  0则删除
            if (spu.getIsDelete().equalsIgnoreCase("1")) {
                throw new RuntimeException("该商品已经删除  不能上架");
            }

            spu.setIsMarketable("0");// 下架
            spuMapper.updateByPrimaryKeySelective(spu);

        }

    }

    // 商品上架不传status
    @Override
    public void auditStatusByspuId(long spuId) {
        Spu spu = spuMapper.selectByPrimaryKey(spuId);

        //物理删除  判断这个商品是否存在
        if (spu==null){
            return;
        }else {
            System.out.println(spu);
            // 逻辑删除  判断isdelete 是否为0  0则删除
            if (spu.getIsDelete().equalsIgnoreCase("1")) {
                throw new RuntimeException("该商品已经删除  不能上架");
            }
            spu.setStatus("1");// 审核通过
            spu.setIsMarketable("1");// 上架
            spuMapper.updateByPrimaryKeySelective(spu);

        }
    }
    // 商品上架传stsatus版本

    @Override
    public void auditStatus(long spuId, String status) {
        Spu spu = spuMapper.selectByPrimaryKey(spuId);

        //物理删除  判断这个商品是否存在
        if (spu==null){
            return;
        }

        // 逻辑删除  判断isdelete 是否为0  0则删除
//        if (spu.getIsDelete().equalsIgnoreCase("1")){
//
//            return;
//
//        }

        if ("1".equals(spu.getIsDelete())){
            throw new  RuntimeException("删除后的商品不能删除");
        }
        spu.setStatus(status);// 审核通过
        spu.setIsMarketable("1");// 上架
        spuMapper.updateByPrimaryKeySelective(spu);


    }

    /**
     * Spu条件+分页查询
     * @param spu 查询条件
     * @param page 页码
     * @param size 页大小
     * @return 分页结果
     */
    @Override
    public PageInfo<Spu> findPage(Spu spu, int page, int size){

        PageHelper.startPage(page,size);
        List<Spu> spus = spuMapper.selectAll();
Example example=createExample(spu);
        return new PageInfo<>(spus);
//        //分页
//        PageHelper.startPage(page,size);
//        //搜索条件构建
//        Example example = createExample(spu);
//        //执行搜索
//        return new PageInfo<Spu>(spuMapper.selectByExample(example));
    }

    /**
     * Spu分页查询
     * @param page
     * @param size
     * @return
     */
    @Override
    public PageInfo<Spu> findPage(int page, int size){
        //静态分页
        PageHelper.startPage(page,size);
        //分页查询
        return new PageInfo<Spu>(spuMapper.selectAll());
    }

    /**
     * Spu条件查询
     * @param spu
     * @return
     */
    @Override
    public List<Spu> findList(Spu spu){
        //构建查询条件
        Example example = createExample(spu);
        //根据构建的条件查询数据
        return spuMapper.selectByExample(example);
    }


    /**
     * Spu构建查询对象
     * @param spu
     * @return
     */
    public Example createExample(Spu spu){
       Example example=new Example(Spu.class);
        Example.Criteria criteria = example.createCriteria();

        if(spu!=null){
            // 主键
            if(!StringUtils.isEmpty(spu.getId())){
                    criteria.andEqualTo("id",spu.getId());
            }
            // 货号
            if(!StringUtils.isEmpty(spu.getSn())){
                    criteria.andEqualTo("sn",spu.getSn());
            }
            // SPU名
            if(!StringUtils.isEmpty(spu.getName())){
                    criteria.andLike("name","%"+spu.getName()+"%");
            }
            // 副标题
            if(!StringUtils.isEmpty(spu.getCaption())){
                    criteria.andEqualTo("caption",spu.getCaption());
            }
            // 品牌ID
            if(!StringUtils.isEmpty(spu.getBrandId())){
                    criteria.andEqualTo("brandId",spu.getBrandId());
            }
            // 一级分类
            if(!StringUtils.isEmpty(spu.getCategory1Id())){
                    criteria.andEqualTo("category1Id",spu.getCategory1Id());
            }
            // 二级分类
            if(!StringUtils.isEmpty(spu.getCategory2Id())){
                    criteria.andEqualTo("category2Id",spu.getCategory2Id());
            }
            // 三级分类
            if(!StringUtils.isEmpty(spu.getCategory3Id())){
                    criteria.andEqualTo("category3Id",spu.getCategory3Id());
            }
            // 模板ID
            if(!StringUtils.isEmpty(spu.getTemplateId())){
                    criteria.andEqualTo("templateId",spu.getTemplateId());
            }
            // 运费模板id
            if(!StringUtils.isEmpty(spu.getFreightId())){
                    criteria.andEqualTo("freightId",spu.getFreightId());
            }
            // 图片
            if(!StringUtils.isEmpty(spu.getImage())){
                    criteria.andEqualTo("image",spu.getImage());
            }
            // 图片列表
            if(!StringUtils.isEmpty(spu.getImages())){
                    criteria.andEqualTo("images",spu.getImages());
            }
            // 售后服务
            if(!StringUtils.isEmpty(spu.getSaleService())){
                    criteria.andEqualTo("saleService",spu.getSaleService());
            }
            // 介绍
            if(!StringUtils.isEmpty(spu.getIntroduction())){
                    criteria.andEqualTo("introduction",spu.getIntroduction());
            }
            // 规格列表
            if(!StringUtils.isEmpty(spu.getSpecItems())){
                    criteria.andEqualTo("specItems",spu.getSpecItems());
            }
            // 参数列表
            if(!StringUtils.isEmpty(spu.getParaItems())){
                    criteria.andEqualTo("paraItems",spu.getParaItems());
            }
            // 销量
            if(!StringUtils.isEmpty(spu.getSaleNum())){
                    criteria.andEqualTo("saleNum",spu.getSaleNum());
            }
            // 评论数
            if(!StringUtils.isEmpty(spu.getCommentNum())){
                    criteria.andEqualTo("commentNum",spu.getCommentNum());
            }
            // 是否上架
            if(!StringUtils.isEmpty(spu.getIsMarketable())){
                    criteria.andEqualTo("isMarketable",spu.getIsMarketable());
            }
            // 是否启用规格
            if(!StringUtils.isEmpty(spu.getIsEnableSpec())){
                    criteria.andEqualTo("isEnableSpec",spu.getIsEnableSpec());
            }
            // 是否删除
            if(!StringUtils.isEmpty(spu.getIsDelete())){
                    criteria.andEqualTo("isDelete",spu.getIsDelete());
            }
            // 审核状态
            if(!StringUtils.isEmpty(spu.getStatus())){
                    criteria.andEqualTo("status",spu.getStatus());
            }
        }
        return example;
    }

    /**
     * 删除
     * @param id
     */
    @Override
    public void delete(Long id){
        spuMapper.deleteByPrimaryKey(id);
    }

    /**
     * 修改Spu
     * @param spu
     */
    @Override
    public void update(Spu spu){
        spuMapper.updateByPrimaryKey(spu);
    }

    /**
     * 增加Spu
     * @param spu
     */
    @Override
    public void add(Spu spu){
        spuMapper.insert(spu);
    }

    /**
     * 根据ID查询Spu
     * @param id
     * @return
     */
    @Override
    public Spu findById(Long id){
        return  spuMapper.selectByPrimaryKey(id);
    }

    /**
     * 查询Spu全部数据
     * @return
     */
    @Override
    public List<Spu> findAll() {
        return spuMapper.selectAll();
    }
    // 批量上架

    @Override
    public void putmany(long[] ids) {
        Spu spu = new Spu();
        spu.setStatus("1");

        Example example = new Example(Spu.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andIn("id", Arrays.asList(ids));
            criteria.andEqualTo("isMarketable","1");
        //审核通过的
        criteria.andEqualTo("status","1");
        //非删除的
        criteria.andEqualTo("isDelete","0");
        spuMapper.updateByExampleSelective(spu,example);



    }

    // 批量下

    @Override
    public void pullmany(long[] ids) {
        Spu spu = new Spu();
        spu.setStatus("1");
        Example example = new Example(Spu.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andIn("id", Arrays.asList(ids));
        criteria.andEqualTo("isMarketable","0");
        //审核通过的
        criteria.andEqualTo("status","1");
        //非删除的
        criteria.andEqualTo("isDelete","0");
        spuMapper.updateByExampleSelective(spu,example);



    }
    //逻辑删除
    @Override
    public void logicdelete(long spuId) {
        Spu spu = spuMapper.selectByPrimaryKey(spuId);

        if (!spu.getIsMarketable().equals("0")) {
        throw new RuntimeException("请先下架商品在删除");
        }

        spu.setIsDelete("1");//逻辑删除
        spu.setStatus("0");

        spuMapper.updateByPrimaryKeySelective(spu);
    }

    @Override
    public void delete(long spuId) {
        Spu spu = spuMapper.selectByPrimaryKey(spuId);

        if (!spu.getIsMarketable().equals("0")) {
            throw new RuntimeException("请先下架商品在删除");
        }



        spuMapper.deleteByPrimaryKey(spu);
    }

    @Override
    public void restore(long spuId) {
        Spu spu = spuMapper.selectByPrimaryKey(spuId);

        if (!spu.getIsDelete().equals("1")) {
            throw new RuntimeException("此商品未被删除");
        }
        spu.setIsDelete("0");

spu.setStatus("0");


        spuMapper.updateByPrimaryKeySelective(spu);
    }


}
