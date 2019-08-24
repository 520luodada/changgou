package com.changgou.search.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.util.JavaBeanInfo;
import com.changgou.entity.Result;
import com.changgou.goods.feign.SkuFeign;
import com.changgou.goods.pojo.Sku;
import com.changgou.search.dao.SkuInfoMapper;
import com.changgou.search.pojo.SkuInfo;
import com.changgou.search.service.SkuInfoService;

import com.github.pagehelper.PageHelper;
import com.netflix.discovery.converters.Auto;
import com.sun.javafx.tk.TKSystemMenu;
import org.apache.lucene.search.BooleanQuery;
import org.elasticsearch.Build;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.*;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.SearchResultMapper;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.aggregation.impl.AggregatedPageImpl;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.common.Mapper;

import javax.naming.Name;
import java.util.*;

@Service
public class SkuInfoServiceImpl implements SkuInfoService {
    @Autowired(required = false)
    private SkuInfoMapper skuInfoMapper;
    @Autowired(required = false)
    private SkuFeign skuFeign;
    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;


    @Override
    public void importSkutoEs() {
        int start=1;
        int end=1000;
        List<Sku> skuList =new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            start+=1000;
            end+=1000;
            Result<List<Sku>> listResult = skuFeign.searchByStatus("1",start,end);
            // 把list<sku>转成 string
            String s = JSON.toJSONString(listResult.getData());
            // 把数据转换成skuinfo
            List<SkuInfo> skuInfos = JSON.parseArray(s, SkuInfo.class);


            for (SkuInfo skuInfo : skuInfos) {
                String spec = skuInfo.getSpec();
                Map<String,Object> specmap= JSON.parseObject(spec);
                skuInfo.setSpecMap(specmap);

            }
            // 存到es
            skuInfoMapper.saveAll(skuInfos);

            }




    }

    @Override
    public Map<String, Object> searchmap(Map<String, String> map) {
         //构建检索条件
        NativeSearchQueryBuilder builder = builderBasicQuery(map);
        Map<String, Object> resultMap = searchPage(builder);
//        List<String> list = searchCategoryList(builder);
//        resultMap.put("categorylist",list);
//        List<String> searchbrandlist = searchbrandlist(builder);
//        resultMap.put("branName",searchbrandlist);
//        Map<String, Set<String>> specMap = searchSpecMap(builder);
//        resultMap.put("specMap",specMap);
        Map<String, Object> listmap = allMap(builder);
        resultMap.putAll(listmap);


        return resultMap;
    }

    // 封装内容(无高亮  后面会用高亮代替这个结果)
    private  Map<String,Object> searchForPage(NativeSearchQueryBuilder builder){
        NativeSearchQuery build = builder.build();

        AggregatedPage<SkuInfo> skuInfos= elasticsearchTemplate.queryForPage(build, SkuInfo.class);
        List<SkuInfo> rows = skuInfos.getContent(); // 结果集
        long totalElements = skuInfos.getTotalElements();// 总条数
        int totalPages = skuInfos.getTotalPages(); //总页数

       Map<String,Object> map=new HashMap<>();
       map.put("rows",rows);
       map.put("totalElements",totalElements);
       map.put("totalPages",totalPages);


        return map;

    }

    // 封装内容(高亮  )
    private  Map<String,Object> searchPage(NativeSearchQueryBuilder builder){
      // 设置高亮条件
     HighlightBuilder.Field field=new HighlightBuilder.Field("name");
     field.preTags("<font color='red'>");
     field.postTags("</font>");
    // field.fragmentSize(100);// 显示的字符长度
        builder.withHighlightFields(field);
        SearchResultMapper searchResultMapper=new SearchResultMapper() {
            @Override
            public <T> AggregatedPage<T> mapResults(SearchResponse searchResponse, Class<T> aClass, Pageable pageable) {
                //封装高亮结果集
                List<T> content = new ArrayList<>();

                // 获取高亮结果集
                SearchHits hits = searchResponse.getHits();
                for (SearchHit hit : hits) {
                    String sourceAsString = hit.getSourceAsString();// 获取普通结果集
                    SkuInfo skuInfo = JSON.parseObject(sourceAsString, SkuInfo.class);//转成skuinfo
                    HighlightField name = hit.getHighlightFields().get("name");
                    if (name != null) {
                        Text[] fragments = name.getFragments();//获取高亮结果集
                        skuInfo.setName(fragments[0].toString());//替换掉无高亮的数据

                    }
                    content.add((T) skuInfo);
                }
                return new AggregatedPageImpl<>(content, pageable, hits.getTotalHits());

            }

        };
        NativeSearchQuery build = builder.build();
        AggregatedPage<SkuInfo> aggregatedPage = elasticsearchTemplate.queryForPage(build, SkuInfo.class,searchResultMapper);
        int totalPages = aggregatedPage.getTotalPages();
        long totalElements = aggregatedPage.getTotalElements();
        List<SkuInfo> rows = aggregatedPage.getContent();

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("rows", rows);
        map.put("totalElements", totalElements);
        map.put("totalPages", totalPages);
        map.put("pageNum",build.getPageable().getPageNumber()+1);
        map.put("pageSize",build.getPageable().getPageSize());

return map;
        // 构建原来的

    }

    // 封装检索条件

    private NativeSearchQueryBuilder builderBasicQuery(Map<String,String> map){
        // 封装检索条件
        NativeSearchQueryBuilder builder = new NativeSearchQueryBuilder();
        // 封装过滤条件
        BoolQueryBuilder booleanQuery=new BoolQueryBuilder();
        if (!StringUtils.isEmpty(map)){
            // 获取关键字
            String keywords = map.get("keywords");
            if (!StringUtils.isEmpty(keywords)){
                 builder.withQuery(QueryBuilders.matchQuery("name",keywords));
            }
            // 分类搜索
            String category = map.get("category");

            if (!StringUtils.isEmpty(category)){
                booleanQuery.must(QueryBuilders.matchQuery("categoryName",category));
            }

            // 品牌
            String brand=map.get("brand");
            if (!StringUtils.isEmpty(brand)){
                booleanQuery.must(QueryBuilders.matchQuery("brandName",brand));

            }
            // 规格
            Set<String> strings = map.keySet();
            for (String key : strings) {
                if (key.startsWith("spec_")){
                    booleanQuery.must(QueryBuilders.matchQuery("specMap."+key.substring(5)+".keyword",map.get(key)));
                }
            }
            // 价格
        String price= map.get("price");
            if (!StringUtils.isEmpty(price)){
                String[] splits = price.split("-");
                booleanQuery.must(QueryBuilders.rangeQuery("price").gte(splits[0]));
                if (splits.length==2){
                    booleanQuery.must(QueryBuilders.rangeQuery("price").lte(splits[1]));
                }
            }
            builder.withFilter(booleanQuery);
            // 分页
            String pageNum = map.get("pageNum");
            if (StringUtils.isEmpty(pageNum)){
                pageNum="1";
            }
            int page=Integer.parseInt(pageNum);
            int size=10;
            Pageable pageable = PageRequest.of(page - 1, 10);
                builder.withPageable(pageable);

        }

        //
        // 排序   排序规则（升序和降序）  排序字段 （根据什么（销量。。）排序）

        String sortRule = map.get("sortRule");
        String sortFiled = map.get("sortFiled");
        if (!StringUtils.isEmpty(sortFiled)&&!StringUtils.isEmpty(sortRule)){
            builder.withSort(SortBuilders.fieldSort(sortFiled).order(SortOrder.fromString(sortRule)));

        }
        return builder;
    }


    // 分类搜索

    private List<String> searchCategoryList(NativeSearchQueryBuilder builder){
        builder.addAggregation(AggregationBuilders.terms("skuCategory").field("categoryName"));

        AggregatedPage<SkuInfo> skuInfos = elasticsearchTemplate.queryForPage(builder.build(), SkuInfo.class);
        Aggregations aggregations = skuInfos.getAggregations();

        StringTerms stringTerms = aggregations.get("skuCategory");
        List<StringTerms.Bucket> buckets = stringTerms.getBuckets();
        List<String> list=new ArrayList<>();
        for (StringTerms.Bucket bucket : buckets) {
            list.add(bucket.getKeyAsString());
        }
        return list;
    }


    //  商品名称显示
    private List<String> searchbrandlist(NativeSearchQueryBuilder builder){
        builder.addAggregation(AggregationBuilders.terms("skubrand").field("brandName"));
        AggregatedPage<SkuInfo> aggregatedPage = elasticsearchTemplate.queryForPage(builder.build(), SkuInfo.class);
        Aggregations aggregations = aggregatedPage.getAggregations();
        StringTerms terms = aggregations.get("skubrand");
        List<StringTerms.Bucket> buckets = terms.getBuckets();
        List<String> list=new ArrayList<>();
        for (StringTerms.Bucket bucket : buckets) {
            String keyAsString = bucket.getKeyAsString();
            list.add(keyAsString);
        }
        return  list;

    }
    // 商品规格  spec

    private Map<String,Set<String>> searchSpecMap(NativeSearchQueryBuilder builder){
builder.addAggregation(AggregationBuilders.terms("specmap").field("spec.keyword"));
        AggregatedPage<SkuInfo> aggregatedPage = elasticsearchTemplate.queryForPage(builder.build(), SkuInfo.class);
        Aggregations aggregations = aggregatedPage.getAggregations();
        StringTerms stringTerms = aggregations.get("specmap");
        List<StringTerms.Bucket> buckets = stringTerms.getBuckets();
        List<String> list=new ArrayList<>();
        for (StringTerms.Bucket bucket : buckets) {
            String keyAsString = bucket.getKeyAsString();
         list.add(keyAsString);


        }
        Map<String,Set<String>>  map=listformap(list);
return map;
    }

    private Map<String, Set<String>> listformap(List<String> list) {
        HashMap<String, Set<String>> objectObjectHashMap = new HashMap<>();
       // 把list的数提转换换成map  把keyvalue提取出来  {"电视音响效果":"环绕","电视屏幕尺寸":"20英寸","尺码":"170"}
        for (String spec : list) {
            Map<String,String> map = JSON.parseObject(spec, Map.class);
            Set<Map.Entry<String, String>> entries = map.entrySet();
            for (Map.Entry<String, String> entry : entries) {
                String key = entry.getKey();//电视音响效果
                String value = entry.getValue();//环绕
                Set<String> set = objectObjectHashMap.get(key);
                if (set==null){
                    set=new HashSet<>();
                }
                set.add(value);
                objectObjectHashMap.put(key,set);
            }

        }
        return objectObjectHashMap;

    }
    //  分类 品牌搜索  category  brand



    // 优化
    private Map<String,Object> allMap(NativeSearchQueryBuilder builder){
        builder.addAggregation(AggregationBuilders.terms("skuCategory").field("categoryName"));
        builder.addAggregation(AggregationBuilders.terms("skubrand").field("brandName"));
        builder.addAggregation(AggregationBuilders.terms("specmap").field("spec.keyword"));
        AggregatedPage<SkuInfo> aggregatedPage = elasticsearchTemplate.queryForPage(builder.build(), SkuInfo.class);
        Aggregations aggregations = aggregatedPage.getAggregations();
        List<String> skuCategorylist = getList(aggregations, "skuCategory");
        List<String> skubrandlist = getList(aggregations, "skubrand");
        List<String> specmaplist = getList(aggregations, "specmap");
        Map<String, Set<String>> specmap = listformap(specmaplist);
        Map<String,Object> listMap=new HashMap<>();
        listMap.put("categoryList",skuCategorylist);
        listMap.put("brand",skubrandlist);
        listMap.put("specmap",specmap);

        return listMap;

    }

    private List<String> getList(Aggregations aggregations, String name) {
     StringTerms stringTerms   = aggregations.get(name);
     List<String> list=new ArrayList<>();
        List<StringTerms.Bucket> buckets = stringTerms.getBuckets();
        for (StringTerms.Bucket bucket : buckets) {
            String keyAsString = bucket.getKeyAsString();
          list.add(keyAsString);
        }
        return list;
    }

}




