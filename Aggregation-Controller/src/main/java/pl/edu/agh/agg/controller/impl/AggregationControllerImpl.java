package pl.edu.agh.agg.controller.impl;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.ObjectMessage;

import org.apache.log4j.Logger;
import org.springframework.jms.JmsException;
import org.springframework.jms.core.JmsTemplate;

import pl.edu.agh.agg.controller.IAggregationController;
import pl.edu.agh.msc.MPTGeneticAlgorithm.MPTPortfolio;
import pl.edu.agh.msc.generic.genetic.algorithm.Portfolio;

public class AggregationControllerImpl implements IAggregationController {

	static Logger logger = Logger.getLogger(AggregationControllerImpl.class);

	private JmsTemplate jmsTemplate;
	private JmsTemplate jmsTopicTemplate;
	private int numberOfComputingAgents;
	private int numberOfRounds;

	private String queueName;
	List<Portfolio> portfolios = new LinkedList<Portfolio>();

	public void getResultsFromComputingNodes() throws JmsException {
		logger.info("receiving results from computing nodes");
		BufferedWriter bufferedWriter;
		try {
			Message message;
			bufferedWriter = new BufferedWriter(new FileWriter(new File(
					"aggregated_results")));

			for (int round = 0; round < numberOfRounds; round++) {
				portfolios.clear();

				for (int i = 0; i < numberOfComputingAgents; i++) {
					message = jmsTemplate.receive(queueName);

					logger.info("message received:"
							+ ((MPTPortfolio) ((ObjectMessage) message)
									.getObject()));

					System.out.println("CLASS:"
							+ ((ObjectMessage) message).getObject().getClass());

					if (((ObjectMessage) message).getObject() instanceof MPTPortfolio) {

						logger.info("RECEIVED MPTPORTFOLIO:"
								+ ((MPTPortfolio) ((ObjectMessage) message)
										.getObject()).getRisk());

						portfolios.add((MPTPortfolio) ((ObjectMessage) message)
								.getObject());
					}/*
					 * else { portfolios.add((Portfolio) ((ObjectMessage)
					 * message) .getObject()); }
					 */
				}

				MPTPortfolio bestPortfolio = selectBestPortfolio(portfolios);
				bufferedWriter.write(round + " " + bestPortfolio.getRisk()
						+ " " + bestPortfolio.getValue() + "\n");

				// request new results from computing nodes
				for (int j = 0; j < numberOfComputingAgents; j++) {
					jmsTopicTemplate.convertAndSend("controllerQueue", "ala");
				}
			}

			bufferedWriter.close();

		} catch (JmsException e) {
			logger.error("exception during receiving a message from computing node!!!");
			throw e;
		} catch (JMSException e) {
			e.printStackTrace();
			logger.error("exception JMSExc during receiving a message from computing node!!!");
		} catch (IOException e) {
			logger.error("IOException...");
			e.printStackTrace();
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

	private MPTPortfolio selectBestPortfolio(
			List<? extends Portfolio> portfolios) {
		double max = 0.0;
		Portfolio bestPortfolio = null;

		for (Portfolio portfolio : portfolios) {
			if (portfolio.getValue() > max) {
				max = portfolio.getValue();
				bestPortfolio = portfolio;
			}
		}

		return (MPTPortfolio) bestPortfolio;
	}

	public int getNumberOfRounds() {
		return numberOfRounds;
	}

	public void setNumberOfRounds(int numberOfRounds) {
		this.numberOfRounds = numberOfRounds;
	}

	public JmsTemplate getJmsTopicTemplate() {
		return jmsTopicTemplate;
	}

	public void setJmsTopicTemplate(JmsTemplate jmsTopicTemplate) {
		this.jmsTopicTemplate = jmsTopicTemplate;
	}
}
