package com.github.bingoohuang.springrestclient.spring.api;

import com.github.bingoohuang.springrestclient.annotations.SpringRestClientEnabled;
import com.github.bingoohuang.springrestclient.boot.domain.Account;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

@SpringRestClientEnabled(baseUrl = "http://localhost:4849")
@RequestMapping("/RequestBody")
public interface RequestBodyApi {
    @RequestMapping(value = "/transfer", method = POST)
    String transfer(@RequestBody Account fromAccount);

    @RequestMapping(value = "/transfer", method = POST, consumes = MediaType.APPLICATION_XML_VALUE)
    String transferXml(@RequestBody Account fromAccount);
}
