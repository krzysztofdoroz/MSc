package pl.edu.agh.computing.node;

import javax.jms.JMSException;

public interface IComputingNode {

	void sendResultsToAggregatingNode() throws JMSException;
	void migrate() throws JMSException;
}
