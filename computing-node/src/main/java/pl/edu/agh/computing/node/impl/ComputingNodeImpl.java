package pl.edu.agh.computing.node.impl;

import org.apache.log4j.Logger;
import org.springframework.jms.core.JmsTemplate;

import pl.edu.agh.computing.node.IComputingNode;

public class ComputingNodeImpl implements IComputingNode{

	static Logger logger = Logger.getLogger(ComputingNodeImpl.class);
	
	private JmsTemplate jmsTemplate;
	private String queueName;

	public JmsTemplate getJmsTemplate() {
		return jmsTemplate;
	}

	public void setJmsTemplate(JmsTemplate jmsTemplate) {
		this.jmsTemplate = jmsTemplate;
	}
	
	public String getQueueName() {
		return queueName;
	}

	public void setQueueName(String queueName) {
		this.queueName = queueName;
	}

	public void sendResultsToAggregatingNode() {
		logger.info("sending message...");
		
		jmsTemplate.convertAndSend(queueName,"ala ma kota");
	}
}
