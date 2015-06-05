package com.github.bingoohuang.springrestclient.spring.api;

import com.github.bingoohuang.springrestclient.annotations.SpringRestClientEnabled;
import com.github.bingoohuang.springrestclient.provider.DiamondBaseUrlProvider;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RequestMapping("/another")
@SpringRestClientEnabled(
        baseUrlProvider = DiamondBaseUrlProvider.class,
        createClassFileForDiagnose = true)
public interface AnotherApi {
    @RequestMapping("/add")
    int add(@RequestParam("offset") int offset);
}
