package com.spring.batch.common.config.xml;

import com.thoughtworks.xstream.XStream;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.oxm.xstream.XStreamMarshaller;

import javax.xml.bind.Marshaller;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class XmlConfiguration {

    public XmlConfiguration(XStreamMarshaller xStreamMarshaller) {
        XStream xstream = xStreamMarshaller.getXStream();
        xstream.allowTypesByWildcard(new String[]{"com.spring.batch.**"});
    }

    @Bean
    public XStreamMarshaller xStreamMarshaller() {
        return new XStreamMarshaller();
    }

    @Bean
    public Map<String, Object> marshallerProperties() {
        Map<String, Object> properties = new HashMap<>();
        properties.put(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        return properties;
    }

    @Bean
    public Jaxb2Marshaller jaxb2Marshaller() {
        Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
        marshaller.setMarshallerProperties(marshallerProperties());
        marshaller.setPackagesToScan("com.spring.batch");
        return marshaller;
    }
}
