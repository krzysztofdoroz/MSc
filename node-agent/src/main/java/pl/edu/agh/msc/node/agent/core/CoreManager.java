package pl.edu.agh.msc.node.agent.core;

import javax.jms.JMSException;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import pl.edu.agh.msc.generic.computing.node.IComputingNode;
import pl.edu.agh.msc.node.agent.impl.NodeAgentImpl;

public class CoreManager {

	private static final int NUMBER_OF_ROUNDS = 3;

	public static void main(String[] args) throws JMSException {
		Resource resource = new ClassPathResource("nodeAgentContext.xml");
		BeanFactory factory = new XmlBeanFactory(resource);

		IComputingNode compNode = (NodeAgentImpl) factory
				.getBean("computingNode");

		for (int round = 0; round < NUMBER_OF_ROUNDS; round++) {
			compNode.sendResultsToAggregatingNode();
		}
	}
}
