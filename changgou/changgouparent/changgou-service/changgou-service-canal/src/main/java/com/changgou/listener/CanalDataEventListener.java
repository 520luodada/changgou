package com.changgou.listener;

import com.alibaba.fastjson.JSON;
import com.alibaba.otter.canal.protocol.CanalEntry;
import com.changgou.content.feign.ContentFeign;
import com.changgou.content.pojo.Content;
import com.changgou.entity.Result;
import com.netflix.discovery.converters.Auto;
import com.xpand.starter.canal.annotation.CanalEventListener;
import com.xpand.starter.canal.annotation.InsertListenPoint;
import com.xpand.starter.canal.annotation.ListenPoint;
import com.xpand.starter.canal.annotation.UpdateListenPoint;
import feign.Feign;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;


import java.security.PublicKey;
import java.util.List;

@CanalEventListener
public class CanalDataEventListener {
@Autowired
private ContentFeign contentFeign;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;


    @ListenPoint(destination = "examole",schema = "changgou_content",table = {"tb_content"},eventType ={ CanalEntry.EventType.INSERT, CanalEntry.EventType.UPDATE,CanalEntry.EventType.DELETE})
   public void onEven(CanalEntry.EntryType entryType,CanalEntry.RowData rowData){

        String category_id = getColumvalue(rowData, "category_id");
        Result<List<Content>> listResult = contentFeign.selectByCategoryId(Long.parseLong(category_id));
        List<Content> contentList = listResult.getData();
        stringRedisTemplate.boundValueOps("category_id").set(JSON.toJSONString(contentList));

    }

//    // 自定义监听器
//    @ListenPoint(destination = "example", schema = "changgou_content", table = {"tb_content"},
//            eventType = {CanalEntry.EventType.INSERT, CanalEntry.EventType.UPDATE})
//    public void onEventContent(CanalEntry.EntryType entryType, CanalEntry.RowData rowData){
//        // 获取分类id
//        getColumvalue(rowData, "category_id");
//        // 通过分类id查询广告列表
//        Result<List<Content>> result = contentFeign.findListByCategoryId(Long.parseLong(categoryId));
//        // 存入redis
//        stringRedisTemplate.boundValueOps("content_" + categoryId).set(JSON.toJSONString(result.getData()));
//    }

    private String getColumvalue(CanalEntry.RowData rowData, String columnvalue){

        List<CanalEntry.Column> afterColumnsList = rowData.getAfterColumnsList();
        System.out.println(afterColumnsList);
        for (CanalEntry.Column column : afterColumnsList) {
            String name = column.getName();
            if (columnvalue.equals(column)){
                return column.getValue();
            }
        }

        return null;
    }

    // 监听插入
@InsertListenPoint
    public void onEnvenInsert(CanalEntry.EntryType entryType,CanalEntry.RowData rowData){
    List<CanalEntry.Column> afterColumnsList = rowData.getAfterColumnsList();
    for (CanalEntry.Column column : afterColumnsList) {

        System.out.println("列名："+column.getName()+"    值："+ column.getValue());
    }



}

    //监听更新
    @UpdateListenPoint
    public void onEnventUpdate(CanalEntry.EntryType entryType,CanalEntry.RowData rowData){

        List<CanalEntry.Column> beforeColumnsList = rowData.getBeforeColumnsList();
        for (CanalEntry.Column column : beforeColumnsList) {
            System.out.println("更新前key:"+column.getName()+"value:"+column.getValue());
        }

        System.out.println("...............................");

        List<CanalEntry.Column> afterColumnsList = rowData.getAfterColumnsList();
        for (CanalEntry.Column column : afterColumnsList) {
            System.out.println("更新后key："+column.getName()+"value:"+column.getValue());
        }

    }





        }