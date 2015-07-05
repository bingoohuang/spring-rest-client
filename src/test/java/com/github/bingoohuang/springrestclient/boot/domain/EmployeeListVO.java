package com.github.bingoohuang.springrestclient.boot.domain;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "employees")
public class EmployeeListVO {

    private List<EmployeeVO> employees = new ArrayList<EmployeeVO>();

    public List<EmployeeVO> getEmployees() {
        return employees;
    }

    public void setEmployees(List<EmployeeVO> employees) {
        this.employees = employees;
    }

    @Override
    public String toString() {
        return "EmployeeListVO{" +
                "employees=" + employees +
                '}';
    }
}