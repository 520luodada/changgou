package com.changgou.item;
import com.changgou.entity.Result;
import com.changgou.entity.StatusCode;
import com.changgou.item.service.PageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import	java.util.ResourceBundle.Control;
@Controller
@RequestMapping("/page")
public class PageController {

    // 模板
    @Autowired(required = false)
    private PageService pageService;
    @RequestMapping("/creatHtml/{id}")
    public Result creatHtml(@PathVariable(value = "id") Long id){
            pageService.creatHtml(id);
            return new Result(true, StatusCode.OK,"创建成功");
    }
}
