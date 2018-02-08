package com.github.bingoohuang.springrestclient.tests.xml;

import com.github.bingoohuang.springrestclient.xml.Xmls;
import lombok.SneakyThrows;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static com.google.common.truth.Truth.assertThat;

public class XmlsTest {
    @Test @SneakyThrows
    public void getCarAsXml() {
        Car car = createCar();
        String xml = Xmls.marshal(car);
        String expected = "<car registration=\"abc123\"><brand>Volvo</brand><description><![CDATA[Sedan<xx.yy@gmail.com>]]></description></car>";
        assertThat(xml).isEqualTo(expected);

        Car car1 = Xmls.unmarshal(expected, Car.class);
        assertThat(car1).isEqualTo(car);

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("name", "bingoo");
        map.put("age", 123);
    }

    private Car createCar() {
        String registration = "abc123";
        String brand = "Volvo";
        String description = "Sedan<xx.yy@gmail.com>";

        return new Car(registration, brand, description);
    }

    //    @Test
//    public void multiBeans() {
//        Car car = createCar();
//        Account account = createAccount();
//
//        String xml = Xmls.marshal(car, account);
//        assertThat(xml).isEqualTo("<car registration=\"abc123\"><brand>Volvo</brand><description><![CDATA[Sedan<xx.yy@gmail.com>]]></description></car>");
//    }
//
//    private Account createAccount() {
//        return new Account(123, "bjh");
//    }
}
