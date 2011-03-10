package pl.edu.agh.computing.node.impl;

import java.util.Date;
import java.util.Random;

import javax.jms.JMSException;
import javax.jms.ObjectMessage;

import org.apache.log4j.Logger;
import org.springframework.jms.JmsException;
import org.springframework.jms.core.JmsTemplate;

import pl.edu.agh.computing.node.IComputingNode;
import pl.edu.agh.msc.MPTGeneticAlgorithm.MPTPortfolio;
import pl.edu.agh.msc.generic.genetic.algorithm.IGeneticAlgorithm;
import pl.edu.agh.msc.generic.genetic.algorithm.Portfolio;

public class ComputingNodeImpl implements IComputingNode {

	static Logger logger = Logger.getLogger(ComputingNodeImpl.class);
	private Random rand = new Random();

	private JmsTemplate jmsTemplate;
	private String queueName;
	private IGeneticAlgorithm geneticAlgorithm;
	private static final int NUMBER_OF_MILIS = 50;

	public void sendResultsToAggregatingNode() throws JMSException {

		logger.info("sending message...");
		try {
			
			MPTPortfolio portfolio = (MPTPortfolio) geneticAlgorithm.calculateCurrentPortfolio();
			
			System.out.println("RISK:" + portfolio.getRisk());
			
			jmsTemplate.convertAndSend(queueName,
					portfolio);
			
			
			
			migrate();
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
		Portfolio migratingPortfolio = geneticAlgorithm.getPortfolioToMigrate();

		long timestamp = new Date().getTime();

		logger.info("migration:" + timestamp + migratingPortfolio);

		jmsTemplate.convertAndSend("migrationQueue", migratingPortfolio);

		// sleeping in order to assure that messages would be get from a queue
		// at random
		try {
			Thread.sleep(Math.abs(rand.nextInt() % NUMBER_OF_MILIS));
		} catch (Exception e) {
			logger.error("sleeping failed");
		}

		ObjectMessage message = (ObjectMessage) jmsTemplate
				.receive("migrationQueue");
		Portfolio migrant = (Portfolio) message.getObject();
		logger.info("new migrant has arrived: " + timestamp + migrant);
		geneticAlgorithm.acceptMigrant(migrant);
	}

	public IGeneticAlgorithm getGeneticAlgorithm() {
		return geneticAlgorithm;
	}

	public void setGeneticAlgorithm(IGeneticAlgorithm geneticAlgorithm) {
		this.geneticAlgorithm = geneticAlgorithm;
	}

}
