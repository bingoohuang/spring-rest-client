package com.github.bingoohuang.springrestclient.boot.domain;

import lombok.Data;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@Data
@XmlRootElement(name = "employees")
public class EmployeeListVO {
    private List<EmployeeVO> employees = new ArrayList<EmployeeVO>();
}