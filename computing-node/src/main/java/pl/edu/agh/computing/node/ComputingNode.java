package pl.edu.agh.computing.node;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.TextMessage;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.jms.core.JmsTemplate;



public class ComputingNode {

	public static void main(String[] args) throws JMSException {
		Resource resource = new FileSystemResource("computingNode-context.xml");
		BeanFactory factory = new XmlBeanFactory(resource);

		JmsTemplate jmsTemplate = (JmsTemplate) factory.getBean("jmsTemplate");

		Message message = jmsTemplate.receive("simple");
		
		if (message instanceof TextMessage){
			System.out.println(((TextMessage) message).getText());
		}
		
	}
	
}
