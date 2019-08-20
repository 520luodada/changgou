package com.es;

import com.changgou.SearchApplication;
import com.changgou.search.pojo.SkuInfo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest(classes = SearchApplication.class)
@RunWith(SpringRunner.class)
public class EsTest {

    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

    @Test
    public void testMapping(){
        elasticsearchTemplate.createIndex(SkuInfo.class);
        elasticsearchTemplate.putMapping(SkuInfo.class);
    }
}
