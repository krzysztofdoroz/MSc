package pl.edu.agh.core;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.TextMessage;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.jms.core.JmsTemplate;

public class Core {

	static Logger logger = Logger.getLogger(Core.class);
	
	public static void main(String[] args) throws JMSException {

		PropertyConfigurator.configure("log4j.properties");
		Resource resource = new ClassPathResource("context.xml");
		BeanFactory factory = new XmlBeanFactory(resource);
		
		JmsTemplate jmsTemplate = (JmsTemplate) factory.getBean("jmsTemplate");

		logger.info("receiving message...");
		
		Message message = jmsTemplate.receive("simple");
		
		if (message instanceof TextMessage){
			logger.info(((TextMessage) message).getText());
		}
	}

}
