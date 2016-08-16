package com.github.bingoohuang.springrestclient.xml;

import com.google.common.base.Throwables;
import lombok.experimental.UtilityClass;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import javax.xml.bind.JAXB;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamWriter;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
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

    public String marshal(Object bean, Class... types) {
        StringWriter sw = new StringWriter();

        try {
            JAXBContext carContext = JAXBContext.newInstance(types);
            Marshaller marshaller = carContext.createMarshaller();

            XMLOutputFactory xof = XMLOutputFactory.newInstance();
            XMLStreamWriter streamWriter = xof.createXMLStreamWriter(sw);
            CDataXMLStreamWriter cdataStreamWriter = new CDataXMLStreamWriter(streamWriter);

//            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.setProperty(Marshaller.JAXB_FRAGMENT, true); //  without xml declaration
            marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
            marshaller.marshal(bean, cdataStreamWriter);
            cdataStreamWriter.flush();
            cdataStreamWriter.close();

            return sw.toString();
        } catch (Exception e) {
            throw Throwables.propagate(e);
        }
    }

    public <T> T unmarshal(String xml, Class<T> beanClass) {
        StringReader reader = new StringReader(xml);
        return JAXB.unmarshal(reader, beanClass);
    }

    public String prettyXml(String xml) {
        final boolean omitXmlDeclaration = !xml.startsWith("<?xml");

        try {
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            transformerFactory.setAttribute("indent-number", 2);
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, omitXmlDeclaration ? "yes" : "no");
            StreamResult result = new StreamResult(new StringWriter());
            DOMSource source = new DOMSource(parseXmlFile(xml));
            transformer.transform(source, result);
            String xmlString = result.getWriter().toString();
            return xmlString;
        } catch (TransformerException e) {
            throw new RuntimeException(e);
        }
    }

    public Document parseXmlFile(String xml) {
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            InputSource is = new InputSource(new StringReader(xml));
            return db.parse(is);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
