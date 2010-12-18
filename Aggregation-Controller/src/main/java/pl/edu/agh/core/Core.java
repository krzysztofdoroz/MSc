package pl.edu.agh.core;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.jms.core.JmsTemplate;

public class Core {

	public static void main(String[] args) {

		Resource resource = new FileSystemResource("context.xml");
		BeanFactory factory = new XmlBeanFactory(resource);

		JmsTemplate jmsTemplate = (JmsTemplate) factory.getBean("jmsTemplate");

		jmsTemplate.convertAndSend("simple","ala ma kota");
	}

}
