package org.paranora.spring.test.web.web;


import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/hello")
public class HelloController {

    protected Logger logger = LoggerFactory.getLogger(HelloController.class);

    @RequestMapping(value = "/morning", method = RequestMethod.GET)
    public String morning(@RequestParam("first") String first){
        return JSON.toJSONString(String.format("morning, i am paranora today eat %s, at %s",first,System.currentTimeMillis()));
    }

}