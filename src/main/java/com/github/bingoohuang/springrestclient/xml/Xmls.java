package com.github.bingoohuang.springrestclient.xml;

import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import lombok.val;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import javax.xml.bind.JAXB;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
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

    @SneakyThrows
    public String prettyXml(String xml) {
        val omitXmlDeclaration = !xml.startsWith("<?xml");

        val transformerFactory = TransformerFactory.newInstance();
        transformerFactory.setAttribute("indent-number", 2);
        Transformer transformer = transformerFactory.newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION,
            omitXmlDeclaration ? "yes" : "no");
        val result = new StreamResult(new StringWriter());
        val source = new DOMSource(parseXmlFile(xml));
        transformer.transform(source, result);
        val xmlString = result.getWriter().toString();
        return xmlString;
    }

    @SneakyThrows
    public Document parseXmlFile(String xml) {
        val dbf = DocumentBuilderFactory.newInstance();
        val db = dbf.newDocumentBuilder();
        val is = new InputSource(new StringReader(xml));
        return db.parse(is);
    }
}
