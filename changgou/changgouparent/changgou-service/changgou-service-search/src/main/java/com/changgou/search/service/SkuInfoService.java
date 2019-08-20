package com.changgou.search.service;

import java.util.Map;

public interface SkuInfoService {

    // 把数据的数据存到中

    void importSkutoEs();

    Map<String,Object> searchmap(Map<String,String> map);
}
