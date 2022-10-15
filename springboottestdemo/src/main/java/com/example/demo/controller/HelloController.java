package com.example.demo.controller;

import com.example.demo.bean.ModelDes;
import com.example.demo.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;

@Controller
public class HelloController {

    @Autowired
    TestService service;

    @RequestMapping("index")
    public String sayHello(){
        return "index";
    }

    @ResponseBody
    @RequestMapping(value="query",method = RequestMethod.POST)
    public HashMap <String,Object> queryModelDes(){
        ModelDes des= service.queryById(1L);
        HashMap<String,Object> result=new HashMap <>();
        result.put("dex",des);
        return result;
    }
}
