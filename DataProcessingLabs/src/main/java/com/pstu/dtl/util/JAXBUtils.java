package com.pstu.dtl.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringReader;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.SchemaOutputResolver;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.Result;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

/** To generate java classes based on schema files use <b>{JDK_HOME}/bin/xjc</b> */
public class JAXBUtils {

    @SuppressWarnings("unchecked")
    public static <T> T unmarshal(InputStream stream, Class<T> clazz) {
        try {
            JAXBContext context = JAXBContext.newInstance(clazz);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            return (T) unmarshaller.unmarshal(stream);
        }
        catch (Exception t) {
            throw new RuntimeException("Failed to read object from stream.", t);
        }
    }

    public static <T> T unmarshal(byte[] input, Class<T> clazz) {
        return unmarshal(new ByteArrayInputStream(input), clazz);
    }

    public static <T> void marshal(OutputStream stream, T object) {
        try {
            JAXBContext context = JAXBContext.newInstance(object.getClass());
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
            marshaller.marshal(object, stream);
        }
        catch (Exception e) {
            throw new RuntimeException("Failed to write object to stream.", e);
        }
    }

    public static <T> byte[] marshal(T object) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        marshal(stream, object);
        return stream.toByteArray();
    }

    public static void generateSchema(final OutputStream stream, final Class<?>... classes) {
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(classes);
            jaxbContext.generateSchema(new SchemaOutputResolver() {
                public Result createOutput(String namespaceURI, String suggestedFileName) throws IOException {
                    final StreamResult result = new StreamResult(stream);
                    result.setSystemId("no-id");
                    return result;
                }
            });
        }
        catch (Exception e) {
            throw new RuntimeException("Failed to generate schema from classes.", e);
        }
    }

    public static boolean validateAgainstXSD(String xsd, String xml) {
        try {
            InputStream resourceUrl = JAXBUtils.class.getResourceAsStream(xsd);
            SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            Schema schema = factory.newSchema(new StreamSource(resourceUrl));
            Validator validator = schema.newValidator();
            StringReader reader = new StringReader(xml);
            validator.validate(new StreamSource(reader));
            return true;
        }
        catch (Exception ex) {
            return false;
        }
    }
}
