package com.github.bingoohuang.springrestclient.boot.controller;

import com.github.bingoohuang.springrestclient.boot.annotations.RestfulSign;
import com.github.bingoohuang.springrestclient.boot.domain.Car;
import com.github.bingoohuang.springrestclient.boot.domain.Person;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/json")
@RestfulSign(ignore = true)
public class JsonRequestParamController {
    @RequestMapping("/case1")
    public Person case1(@RequestParam("person") Person person) {
        return person;
    }

    @RequestMapping("/case2")
    public Car case2(@RequestParam("person") Person person,
                        @RequestParam("car") Car car) {
        System.out.println(person);
        return car;
    }

    @RequestMapping("/case3")
    public List<Person> case3(@RequestParam("persons") List<Person> persons) {
        return persons;
    }

    @RequestMapping("/case4")
    public List<Person> case4(@RequestParam("car") Car car, @RequestParam("persons") List<Person> persons) {
        return persons;
    }

}
