package com.github.bingoohuang.springrestclient.tests.xml;

import com.github.bingoohuang.springrestclient.xml.Xmls;
import org.junit.Test;

import static com.google.common.truth.Truth.assertThat;

public class XmlsTest {
    @Test
    public void getCarAsXml() {
        String registration = "abc123";
        String brand = "Volvo";
        String description = "Sedan<xx.yy@gmail.com>";

        Car car = new Car(registration, brand, description);
        String xml = Xmls.marshal(car);
        assertThat(xml).isEqualTo("<car registration=\"abc123\"><brand>Volvo</brand><description><![CDATA[Sedan<xx.yy@gmail.com>]]></description></car>");
    }
}
