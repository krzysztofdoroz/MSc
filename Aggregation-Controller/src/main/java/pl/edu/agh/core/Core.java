package pl.edu.agh.core;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.jms.core.JmsTemplate;

public class Core {

	static Logger logger = Logger.getLogger(Core.class);
	
	public static void main(String[] args) {

		PropertyConfigurator.configure("log4j.properties");
		Resource resource = new FileSystemResource("context.xml");
		BeanFactory factory = new XmlBeanFactory(resource);

		JmsTemplate jmsTemplate = (JmsTemplate) factory.getBean("jmsTemplate");

		logger.info("asasdaf");
		
		jmsTemplate.convertAndSend("simple","ala ma kota");
	}

}
