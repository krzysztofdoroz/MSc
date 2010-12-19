package pl.edu.agh.agg.controller.impl;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.TextMessage;

import org.apache.log4j.Logger;
import org.springframework.jms.JmsException;
import org.springframework.jms.core.JmsTemplate;

import pl.edu.agh.agg.controller.IAggregationController;

public class AggregationControllerImpl implements IAggregationController {

	static Logger logger = Logger.getLogger(AggregationControllerImpl.class);

	private JmsTemplate jmsTemplate;
	private int numberOfComputingAgents;
	private String queueName;

	public void getResultsFromComputingNodes() throws JmsException {
		logger.info("receiving results from computing nodes");
		try {
			Message message;

			for (int i = 0; i < numberOfComputingAgents; i++) {
				message = jmsTemplate.receive(queueName);
				logger.info("message received:" + ((TextMessage) message).getText());
			}
		} catch (JmsException e) {
			logger.error("exception during receiving a message from computing node!!!");
			throw e;
		} catch (JMSException e) {
			logger.error("exception JMSExc during receiving a message from computing node!!!");
		}
	}

	public JmsTemplate getJmsTemplate() {
		return jmsTemplate;
	}

	public void setJmsTemplate(JmsTemplate jmsTemplate) {
		this.jmsTemplate = jmsTemplate;
	}

	public int getNumberOfComputingAgents() {
		return numberOfComputingAgents;
	}

	public void setNumberOfComputingAgents(int numberOfComputingAgents) {
		this.numberOfComputingAgents = numberOfComputingAgents;
	}

	public String getQueueName() {
		return queueName;
	}

	public void setQueueName(String queueName) {
		this.queueName = queueName;
	}

}
