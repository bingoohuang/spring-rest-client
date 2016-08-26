package com.github.bingoohuang.springrestclient.boot.controller;

import com.github.bingoohuang.springrestclient.boot.domain.EmployeeListVO;
import com.github.bingoohuang.springrestclient.boot.domain.EmployeeVO;
import lombok.val;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
@RequestMapping("/employees")
public class EmployeeXmlController {
    @RequestMapping(produces = {"application/xml"})
    public EmployeeListVO getAllEmployees() {
        val employees = new EmployeeListVO();

        val empOne = new EmployeeVO(1, "Lokesh", "Gupta", "howtodoinjava@gmail.com");
        val empTwo = new EmployeeVO(2, "Amit", "Singhal", "asinghal@yahoo.com");
        val empThree = new EmployeeVO(3, "Kirti", "Mishra", "kmishra@gmail.com");


        employees.getEmployees().add(empOne);
        employees.getEmployees().add(empTwo);
        employees.getEmployees().add(empThree);

        return employees;
    }

    @RequestMapping(value = "/{id}", produces = {"application/xml"})
    public ResponseEntity<EmployeeVO> getEmployeeById(
        @PathVariable("id") int id) {
        if (id <= 3) {
            val employee = new EmployeeVO(1, "Lokesh", "Gupta", "howtodoinjava@gmail.com");
            return new ResponseEntity<EmployeeVO>(employee, HttpStatus.OK);
        }

        return new ResponseEntity(HttpStatus.NOT_FOUND);
    }

    @RequestMapping(value = "/echo/", method = POST, consumes = MediaType.APPLICATION_XML_VALUE)
    public EmployeeVO echoEmployeeVO(@RequestBody EmployeeVO employeeVO) {
        employeeVO.setFirstName("BingooHuang");
        return employeeVO;
    }
}
