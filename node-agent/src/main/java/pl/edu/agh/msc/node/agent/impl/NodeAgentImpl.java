package pl.edu.agh.msc.node.agent.impl;

import java.util.Date;
import java.util.Random;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.ObjectMessage;
import javax.jms.TextMessage;

import org.apache.log4j.Logger;
import org.springframework.jms.core.JmsTemplate;

import pl.edu.agh.msc.generic.computing.node.IComputingNode;

public class NodeAgentImpl implements IComputingNode {

	private String queueName;
	private JmsTemplate jmsTemplate;
	private JmsTemplate jmsTopicTemplate;
	private Environment environment;
	static Logger logger = Logger.getLogger(NodeAgentImpl.class);
	private static final int NUMBER_OF_MILIS = 5;
	private Random rand = new Random();
	private boolean maySend = true;

	public void sendResultsToAggregatingNode() throws JMSException {

		if (maySend) {
			logger.info("sending message...");
			jmsTemplate.convertAndSend(queueName,
					environment.getBestAgentPortfolio());
			maySend = false;
		}

		logger.info("receiving message from agg controller..");
		Message message = jmsTopicTemplate.receive("controllerQueue");

		System.out.println("received MESSAGE:"
				+ ((TextMessage) message).getText());

		maySend = true;
		
		migrate();
	}

	public void migrate() throws JMSException {
		Agent agentToMigrate = environment.getAgentToMigrate();

		long timestamp = new Date().getTime();

		logger.info("migration:" + timestamp + agentToMigrate);

		jmsTemplate.convertAndSend("migrationQueue", agentToMigrate);

		// sleeping in order to assure that messages would be get from a queue
		// at random
		try {
			Thread.sleep(Math.abs(rand.nextInt() % NUMBER_OF_MILIS));
		} catch (Exception e) {
			logger.error("sleeping failed");
		}

		ObjectMessage message = (ObjectMessage) jmsTemplate
				.receive("migrationQueue");
		Agent migrant = (Agent) message.getObject();
		logger.info("new migrant has arrived: " + timestamp + migrant);
		environment.acceptMigrant(migrant);

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
