package com.github.bingoohuang.springrestclient.tests.api;

import com.github.bingoohuang.springrestclient.boot.domain.EmployeeListVO;
import com.github.bingoohuang.springrestclient.boot.domain.EmployeeVO;
import com.github.bingoohuang.springrestclient.exception.RestException;
import com.github.bingoohuang.springrestclient.spring.SpringRestClientConfig;
import com.github.bingoohuang.springrestclient.spring.api.EmployeeXmlApi;
import lombok.val;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = SpringRestClientConfig.class)
public class EmployeeXmlApiTest {
    @Autowired
    EmployeeXmlApi employeeXmlApi;

    @Test
    public void getAllEmployees() {
        EmployeeListVO allEmployees = employeeXmlApi.getAllEmployees();
        assertAllEmployees(allEmployees);
    }

    private void assertAllEmployees(EmployeeListVO allEmployees) {
        assertThat(allEmployees.getEmployees().size(), is(equalTo(3)));

        assertThat(allEmployees.getEmployees().get(0).toString(),
                is(equalTo("EmployeeVO(id=1, firstName=Lokesh, lastName=Gupta, email=howtodoinjava@gmail.com)")));
        assertThat(allEmployees.getEmployees().get(1).toString(),
                is(equalTo("EmployeeVO(id=2, firstName=Amit, lastName=Singhal, email=asinghal@yahoo.com)")));
        assertThat(allEmployees.getEmployees().get(2).toString(),
                is(equalTo("EmployeeVO(id=3, firstName=Kirti, lastName=Mishra, email=kmishra@gmail.com)")));
    }

    @Test
    public void getEmployeeById() {
        EmployeeVO employeeVO = employeeXmlApi.getEmployeeById(1);
        assertThat(employeeVO.toString(),
                is(equalTo("EmployeeVO(id=1, firstName=Lokesh, lastName=Gupta, email=howtodoinjava@gmail.com)")));
    }

    @Test(expected = RestException.class)
    public void getEmployeeByIdNotFound() {
        employeeXmlApi.getEmployeeById(10);
    }

    @Test
    public void getAllEmployeesAsync() throws ExecutionException, InterruptedException {
        Future<EmployeeListVO> f1 = employeeXmlApi.getAllEmployeesAsync();

        EmployeeListVO allEmployees = f1.get();
        assertAllEmployees(allEmployees);
    }

    @Test
    public void getEmployeeByIdAsync() throws ExecutionException, InterruptedException {
        Future<EmployeeVO> employeeVO = employeeXmlApi.getEmployeeByIdAsync(1);
        assertThat(employeeVO.get().toString(),
                is(equalTo("EmployeeVO(id=1, firstName=Lokesh, lastName=Gupta, email=howtodoinjava@gmail.com)")));
    }

    @Test
    public void echoEmployeeVO() {
        val vo = new EmployeeVO(1, "BBBB", "DDDD", "xx@yy.com");
        val echo = employeeXmlApi.echoEmployeeVO(vo);
        assertThat(echo.toString(),
                is(equalTo("EmployeeVO(id=1, firstName=BingooHuang, lastName=DDDD, email=xx@yy.com)")));
    }

}
