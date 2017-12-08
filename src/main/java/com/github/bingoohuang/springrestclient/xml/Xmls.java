package com.github.bingoohuang.springrestclient.xml;

import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import lombok.val;

import javax.xml.bind.JAXB;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.stream.XMLOutputFactory;
import java.io.StringReader;
import java.io.StringWriter;

@UtilityClass
public class Xmls {
    public String marshal(Object bean) {
        return marshal(bean, bean.getClass());
    }

    @SneakyThrows
    public String marshal(Object bean, Class... types) {
        val sw = new StringWriter();

        val carContext = JAXBContext.newInstance(types);
        val marshaller = carContext.createMarshaller();

        val xof = XMLOutputFactory.newInstance();
        val streamWriter = xof.createXMLStreamWriter(sw);
        val cdataStreamWriter = new CDataXMLStreamWriter(streamWriter);

//            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        marshaller.setProperty(Marshaller.JAXB_FRAGMENT, true); //  without xml declaration
        marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
        marshaller.marshal(bean, cdataStreamWriter);
        cdataStreamWriter.flush();
        cdataStreamWriter.close();

        return sw.toString();
    }

    public <T> T unmarshal(String xml, Class<T> beanClass) {
        val reader = new StringReader(xml);
        return JAXB.unmarshal(reader, beanClass);
    }

}
