package com.lnf.springboot;


import com.fasterxml.jackson.databind.util.JSONPObject;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.TimeUnit;
@RestController
@RequestMapping("/test")
public class TestController {

    @RequestMapping("/aa")
    public String say(@RequestParam(required = true,name="serviceCodeList") List<String> aa){
        aa.forEach(x->{
            System.out.println(x);
        });
        System.out.println(aa);
        return "a";
    }

}
