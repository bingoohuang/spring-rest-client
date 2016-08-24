package com.github.bingoohuang.springrestclient.spring.api;

import com.github.bingoohuang.springrestclient.annotations.SpringRestClientEnabled;
import com.github.bingoohuang.springrestclient.boot.domain.EmployeeListVO;
import com.github.bingoohuang.springrestclient.boot.domain.EmployeeVO;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.concurrent.Future;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RequestMapping("/employees")
@SpringRestClientEnabled(baseUrl = "http://localhost:4849")
public interface EmployeeXmlApi {
    @RequestMapping(value = "")
    EmployeeListVO getAllEmployees();

    @RequestMapping(value = "/{id}")
    EmployeeVO getEmployeeById(@PathVariable("id") int id);

    @RequestMapping(value = "")
    Future<EmployeeListVO> getAllEmployeesAsync();

    @RequestMapping(value = "/{id}")
    Future<EmployeeVO> getEmployeeByIdAsync(@PathVariable("id") int id);

    @RequestMapping(value = "/echo/", consumes = MediaType.APPLICATION_XML_VALUE, method = POST)
    EmployeeVO echoEmployeeVO(@RequestBody EmployeeVO employeeVO);
}
