package com.github.bingoohuang.springrestclient.boot.domain;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.*;

@XmlRootElement(name = "employee")
@XmlAccessorType(XmlAccessType.NONE)
@Data @NoArgsConstructor @AllArgsConstructor
public class EmployeeVO {
    @XmlAttribute Integer id;

    @XmlElement String firstName;
    @XmlElement String lastName;
    @XmlElement String email;
}