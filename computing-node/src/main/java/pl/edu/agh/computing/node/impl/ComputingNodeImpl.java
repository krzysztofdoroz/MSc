package pl.edu.agh.computing.node.impl;

import javax.jms.JMSException;

import org.apache.log4j.Logger;
import org.springframework.jms.JmsException;
import org.springframework.jms.core.JmsTemplate;

import pl.edu.agh.computing.node.IComputingNode;

public class ComputingNodeImpl implements IComputingNode {

	static Logger logger = Logger.getLogger(ComputingNodeImpl.class);

	private JmsTemplate jmsTemplate;
	private String queueName;

	public void sendResultsToAggregatingNode() throws JMSException {
		logger.info("sending message...");
		try {
			jmsTemplate.convertAndSend(queueName, "ala ma kota");
		} catch (JmsException e) {
			logger.error("exception during sending message to aggregating component!!!");
			throw e;
		}
	}

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
}
