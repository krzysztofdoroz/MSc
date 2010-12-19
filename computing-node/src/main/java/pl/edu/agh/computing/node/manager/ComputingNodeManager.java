package pl.edu.agh.computing.node.manager;

import javax.jms.JMSException;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import pl.edu.agh.computing.node.IComputingNode;
import pl.edu.agh.computing.node.impl.ComputingNodeImpl;

public class ComputingNodeManager {

	static Logger logger = Logger.getLogger(ComputingNodeManager.class);
	
	public static void main(String[] args) throws JMSException {
		Resource resource = new ClassPathResource("computingNode-context.xml");
		BeanFactory factory = new XmlBeanFactory(resource);

		IComputingNode compNode = (ComputingNodeImpl) factory.getBean("computingNode");
		
		compNode.sendResultsToAggregatingNode();
	}
	
}
