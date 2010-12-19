package pl.edu.agh.agg.controller;

import org.springframework.jms.JmsException;

public interface IAggregationController {

	void getResultsFromComputingNodes() throws JmsException;
}
