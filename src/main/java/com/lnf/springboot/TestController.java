package com.lnf.springboot;


import com.fasterxml.jackson.databind.util.JSONPObject;
import com.sun.deploy.net.HttpResponse;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.*;
import java.util.concurrent.TimeUnit;
@RestController
@RequestMapping("/test")
public class TestController {

    @RequestMapping("/aa")
    public Object say(@RequestParam(required = true,name="serviceCodeList") List<String> aa){
        Map map = new HashMap<>();
        Map map1 = new HashMap<>();
        List<IotServer> iotServers = new LinkedList<>();
        IotServer iotImage = new IotServer();
        iotImage.setServerName("2015年安徽生态系统格局一级分类分布图（30米）");
        iotImage.setServerText("2015年全国生态系统格局一级分类分布图（30米）");
        iotImage.setItemId(340000);
        iotImage.setUnit("生态环境部卫星环境应用中心");
        iotImage.sethJ("生态系统状况");
        iotImage.setServerType("空间服务");
        iotImage.setAccessNumber(0);
        iotImage.setServerAddress("http://10.240.138.3:6080/arcgis/rest/services//%E7%94%9F%E6%80%81%E7%B3%BB%E7%BB%9F%E6%A0%BC%E5%B1%80/STXTGJ2015/MapServer");
        iotServers.add(iotImage);
        map1.put("list",iotServers);

        map.put("data",map1);
        map.put("msg","执行成功");
        map.put("status",1);
        return map;
    }

    @RequestMapping( "/add")
    public void test(HttpServletRequest request, HttpServletResponse response){
        response.addHeader("headtoken","11111");
    }

}
