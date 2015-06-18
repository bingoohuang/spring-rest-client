package com.github.bingoohuang.springrestclient.tests.api;


import com.github.bingoohuang.springrestclient.boot.domain.Car;
import com.github.bingoohuang.springrestclient.boot.domain.Person;
import com.github.bingoohuang.springrestclient.spring.SpringRestClientConfig;
import com.github.bingoohuang.springrestclient.spring.api.JsonRequestParamApi;
import com.google.common.collect.Lists;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = SpringRestClientConfig.class)
public class JsonRequestParamApiTest {
    @Autowired
    JsonRequestParamApi api;

    @Test
    public void case1() {
        Person person1 = new Person("bingoo", "nanjing");
        Person person = api.case1(person1);
        assertThat(person, is(equalTo(person1)));
    }

    @Test
    public void case1get() {
        Person person1 = new Person("bingoo", "nanjing");
        Person person = api.case1Get(person1);
        assertThat(person, is(equalTo(person1)));
    }

    @Test
    public void case2() {
        Person person1 = new Person("bingoo", "nanjing");
        Car bmw = new Car("bmw", 1);
        Car myBmw = api.case2(person1, bmw);
        assertThat(myBmw, is(equalTo(bmw)));
    }

    @Test
    public void case2get() {
        Person person1 = new Person("bingoo", "nanjing");
        Car bmw = new Car("bmw", 2);
        Car myBmw = api.case2Get(person1, bmw);
        assertThat(myBmw, is(equalTo(bmw)));
    }

    @Test
    public void case3() {
        Person person1 = new Person("bingoo", "nanjing");
        List<Person> persons1 = Lists.newArrayList(person1);
        List<Person> persons2 = api.case3(persons1);

        assertThat(persons2, is(equalTo(persons1)));
    }

    @Test
    public void case3Get() {
        Person person1 = new Person("bingoo", "nanjing");
        List<Person> persons1 = Lists.newArrayList(person1);
        List<Person> persons2 = api.case3Get(persons1);

        assertThat(persons2, is(equalTo(persons1)));
    }

    @Test
    public void case4() {
        Car bmw = new Car("bmw", 1);
        Person person10 = new Person("bingooaa", "nanjingaa");
        Person person11 = new Person("bingoobb", "nanjingbb");
        List<Person> persons1 = Lists.newArrayList(person10, person11);
        List<Person> persons2 = api.case4(bmw, persons1);

        assertThat(persons2, is(equalTo(persons1)));
    }

    @Test
    public void case4Get() {
        Car bmw = new Car("bmw", 1);
        Person person10 = new Person("bingoo11", "nanjing11");
        Person person11 = new Person("bingoo22", "nanjing22");
        List<Person> persons1 = Lists.newArrayList(person10, person11);
        List<Person> persons2 = api.case4Get(bmw, persons1);

        assertThat(persons2, is(equalTo(persons1)));
    }
}
