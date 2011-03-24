package pl.edu.agh.computing.node.manager;

import javax.jms.JMSException;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import pl.edu.agh.computing.node.impl.ComputingNodeImpl;
import pl.edu.agh.msc.generic.computing.node.IComputingNode;

public class ComputingNodeManager {

	static Logger logger = Logger.getLogger(ComputingNodeManager.class);
	private static final int NUMBER_OF_ROUNDS = 253;

	public static void main(String[] args) throws JMSException {
		Resource resource = new ClassPathResource("computingNode-context.xml");
		BeanFactory factory = new XmlBeanFactory(resource);

		IComputingNode compNode = (ComputingNodeImpl) factory
				.getBean("computingNode");

		for (int round = 0; round < NUMBER_OF_ROUNDS; round++) {
			compNode.sendResultsToAggregatingNode();
		}
	}

}
