package com.github.bingoohuang.springrestclient.boot.domain;


import javax.xml.bind.annotation.*;

@XmlRootElement(name = "employee")
@XmlAccessorType(XmlAccessType.NONE)
public class EmployeeVO {

    @XmlAttribute
    private Integer id;

    @XmlElement
    private String firstName;

    @XmlElement
    private String lastName;

    @XmlElement
    private String email;

    public EmployeeVO(Integer id, String firstName, String lastName, String email) {
        super();
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    public EmployeeVO() {

    }

    //Setters and Getters

    @Override
    public String toString() {
        return "EmployeeVO [id=" + id + ", firstName=" + firstName
                + ", lastName=" + lastName + ", email=" + email + "]";
    }
}