package com.github.bingoohuang.springrestclient.xml;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class XmlsTest {
    @Test
    public void getCarAsXml() {
        String registration = "abc123";
        String brand = "Volvo";
        String description = "Sedan<xx.yy@gmail.com>";

        Car car = new Car(registration, brand, description);
        String xml = Xmls.marshal(car);
        assertThat(xml, is(equalTo("<car registration=\"abc123\"><brand>Volvo</brand><description><![CDATA[Sedan<xx.yy@gmail.com>]]></description></car>")));
        System.out.println(xml);
    }
}
