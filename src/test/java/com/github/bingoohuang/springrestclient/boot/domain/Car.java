package com.github.bingoohuang.springrestclient.boot.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlRootElement;

@Data @NoArgsConstructor @AllArgsConstructor
@XmlRootElement(name = "xml")
public class Car {
    private String brand;
    private int age;
}
