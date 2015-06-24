package com.github.bingoohuang.springrestclient.spring.api;

import com.github.bingoohuang.springrestclient.annotations.SpringRestClientEnabled;
import com.github.bingoohuang.springrestclient.boot.domain.Car;
import com.github.bingoohuang.springrestclient.boot.domain.Person;
import com.github.bingoohuang.springrestclient.provider.DefaultSignProvider;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RequestMapping("/json")
@SpringRestClientEnabled(baseUrl = "http://localhost:4849", signProvider = DefaultSignProvider.class)
public interface JsonRequestParamApi {
    @RequestMapping("/case1")
    Person case1(@RequestParam("person") Person person);

    @RequestMapping(value = "/case1", method = GET)
    Person case1Get(@RequestParam("person") Person person);


    @RequestMapping("/case2")
    Car case2(@RequestParam("person") Person person, @RequestParam("car") Car car);

    @RequestMapping(value = "/case2", method = GET)
    Car case2Get(@RequestParam("person") Person person, @RequestParam("car") Car car);

    @RequestMapping("/case3")
    List<Person> case3(@RequestParam("persons") List<Person> persons);

    @RequestMapping(value = "/case3", method = GET)
    List<Person> case3Get(@RequestParam("persons") List<Person> persons);

    @RequestMapping("/case4")
    List<Person> case4(@RequestParam("car") Car car, @RequestParam("persons") List<Person> persons);

    @RequestMapping(value = "/case4", method = GET)
    List<Person> case4Get(@RequestParam("car") Car car, @RequestParam("persons") List<Person> persons);

    @RequestMapping("/case5")
    List<Person> case5(@RequestParam("car") Car car,
                       @RequestParam("persons") List<Person> persons,
                       @RequestBody String xml);
}
