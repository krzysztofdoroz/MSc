package pl.edu.agh.msc.node.agent.impl;

import javax.jms.JMSException;

import org.springframework.jms.core.JmsTemplate;

import pl.edu.agh.msc.generic.computing.node.IComputingNode;

public class NodeAgentImpl implements IComputingNode {

	private String queueName;
	private JmsTemplate jmsTemplate;
	private JmsTemplate jmsTopicTemplate;
	private Environment environment;
	
	public void sendResultsToAggregatingNode() throws JMSException {
		// TODO Auto-generated method stub
		
	}

	public void migrate() throws JMSException {
		// TODO Auto-generated method stub
	}

	public String getQueueName() {
		return queueName;
	}

	public void setQueueName(String queueName) {
		this.queueName = queueName;
	}
	
	public JmsTemplate getJmsTemplate() {
		return jmsTemplate;
	}

	public void setJmsTemplate(JmsTemplate jmsTemplate) {
		this.jmsTemplate = jmsTemplate;
	}
	
	public JmsTemplate getJmsTopicTemplate() {
		return jmsTopicTemplate;
	}

	public void setJmsTopicTemplate(JmsTemplate jmsTopicTemplate) {
		this.jmsTopicTemplate = jmsTopicTemplate;
	}

	public Environment getEnvironment() {
		return environment;
	}

	public void setEnvironment(Environment environment) {
		this.environment = environment;
	}
}
