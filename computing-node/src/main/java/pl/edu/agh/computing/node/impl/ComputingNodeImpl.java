package pl.edu.agh.computing.node.impl;

import javax.jms.JMSException;

import org.apache.log4j.Logger;
import org.springframework.jms.JmsException;
import org.springframework.jms.core.JmsTemplate;

import pl.edu.agh.computing.node.IComputingNode;
import pl.edu.agh.msc.generic.genetic.algorithm.IGeneticAlgorithm;

public class ComputingNodeImpl implements IComputingNode {

	static Logger logger = Logger.getLogger(ComputingNodeImpl.class);

	private JmsTemplate jmsTemplate;
	private String queueName;
	private IGeneticAlgorithm geneticAlgorithm;

	public void sendResultsToAggregatingNode() throws JMSException {
		logger.info("sending message...");
		try {
			jmsTemplate.convertAndSend(queueName, geneticAlgorithm.calculateCurrentPortfolio());
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

	public void migrate() throws JMSException {
		geneticAlgorithm.calculateCurrentPortfolio();
		//TODO:
	}
	
	public IGeneticAlgorithm getGeneticAlgorithm() {
		return geneticAlgorithm;
	}

	public void setGeneticAlgorithm(IGeneticAlgorithm geneticAlgorithm) {
		this.geneticAlgorithm = geneticAlgorithm;
	}
}
