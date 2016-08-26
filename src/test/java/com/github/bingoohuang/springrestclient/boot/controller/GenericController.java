package com.github.bingoohuang.springrestclient.boot.controller;

import com.google.common.collect.ImmutableMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author shuwei@asiainfo.com
 */
@RestController
@RequestMapping("/generic")
public class GenericController {

    @RequestMapping("/map")
    public Map<String, String> map() {
        return ImmutableMap.of("fuck", "generic");
    }
}
