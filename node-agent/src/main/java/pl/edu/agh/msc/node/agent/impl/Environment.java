package pl.edu.agh.msc.node.agent.impl;

import java.util.LinkedList;
import java.util.List;

import pl.edu.agh.msc.data.source.interfaces.IDataSource;
import pl.edu.agh.msc.generic.genetic.algorithm.MPTPortfolio;
import pl.edu.agh.msc.generic.genetic.algorithm.Portfolio;

public class Environment {

	private List<Agent> riskOrientedPopulation;
	private List<Agent> returnOrientedPopulation;
	private double totalResource;
	private double freeResource;
	private static final int FIRST_SPECIE = 1;
	private static final int SECOND_SPECIE = 2;
	IDataSource stockDataSource;
	private int day = 0;

	public Environment(double totalResource, int populationSize,
			IDataSource dataSource) {
		riskOrientedPopulation = new LinkedList<Agent>();
		returnOrientedPopulation = new LinkedList<Agent>();
		this.totalResource = totalResource;
		this.stockDataSource = dataSource;

		initPopulations(populationSize);
	}

	private void recalculateRiskAndReturn(int day) {
		for (Agent agent : riskOrientedPopulation) {
			agent.setRisk(getRisk(agent.getPortfolio(), day));
			agent.setExpectedReturn(calculateExpectedReturn(
					agent.getPortfolio(), day));
		}

		for (Agent agent : returnOrientedPopulation) {
			agent.setRisk(getRisk(agent.getPortfolio(), day));
			agent.setExpectedReturn(calculateExpectedReturn(
					agent.getPortfolio(), day));
		}
	}

	private void initPopulations(int populationSize) {
		int id = 1;

		for (int i = 0; i < populationSize; i += 2) {
			Agent agent = new Agent(id++, totalResource / (populationSize));
			agent.setSpecie(FIRST_SPECIE);
			riskOrientedPopulation.add(agent);

			Agent agent2 = new Agent(id++, totalResource / (populationSize));
			agent2.setSpecie(SECOND_SPECIE);
			returnOrientedPopulation.add(agent2);
		}
	}

	public MPTPortfolio getBestAgentPortfolio() {
		recalculateRiskAndReturn(day++);

		return null;
	}

	public Agent findNonDominatedSolution() {
		Agent nonDominatedSolution = riskOrientedPopulation.get(0);

		for (Agent agent : riskOrientedPopulation) {
			if (agent.getRisk() <= nonDominatedSolution.getRisk()
					&& agent.getExpectedReturn() >= nonDominatedSolution
							.getExpectedReturn()) {
				
				nonDominatedSolution = agent;
			}
		}
		
		for (Agent agent : returnOrientedPopulation) {
			if (agent.getRisk() <= nonDominatedSolution.getRisk()
					&& agent.getExpectedReturn() >= nonDominatedSolution
							.getExpectedReturn()) {
				
				nonDominatedSolution = agent;
			}
		}

		return nonDominatedSolution;
	}

	// in this case fitness is calculated according to CAPT
	public double calculateExpectedReturn(Portfolio portfolio, int day) {
		double result = 0.0;

		for (int i = 0; i < portfolio.getPortfolio().size(); i++) {
			result += (portfolio.getPortfolio().get(i) * stockDataSource
					.getCovarianceData(i, day))
					/ stockDataSource.getMarketVariance(day);
		}
		return result;
	}

	public double getRisk(Portfolio portfolio, int day) {
		double risk = 0.0;
		double correlationCoeff = 0.0;
		double stockNumberOneWeigth = 0.0;
		double stockNumberTwoWeigth = 0.0;

		for (int i = 0; i < portfolio.getSize(); i++) {
			for (int j = 0; j < portfolio.getSize(); j++) {

				if (i == j) {
					correlationCoeff = 1.0;
				} else {
					correlationCoeff = stockDataSource.getCorrelationCoeffData(
							i / 2, day);
				}

				stockNumberOneWeigth = portfolio.getPortfolio().get(i);
				stockNumberTwoWeigth = portfolio.getPortfolio().get(j);

				risk += stockNumberOneWeigth * stockNumberTwoWeigth
						* stockDataSource.getStandardDevData(i, day)
						* stockDataSource.getStandardDevData(j, day)
						* correlationCoeff;
			}
		}

		return Math.sqrt(risk);
	}

	public IDataSource getStockDataSource() {
		return stockDataSource;
	}

	public void setStockDataSource(IDataSource stockDataSource) {
		this.stockDataSource = stockDataSource;
	}

	public List<Agent> getRiskOrientedPopulation() {
		return riskOrientedPopulation;
	}

	public void setRiskOrientedPopulation(List<Agent> riskOrientedPopulation) {
		this.riskOrientedPopulation = riskOrientedPopulation;
	}

	public List<Agent> getReturnOrientedPopulation() {
		return returnOrientedPopulation;
	}

	public void setReturnOrientedPopulation(List<Agent> returnOrientedPopulation) {
		this.returnOrientedPopulation = returnOrientedPopulation;
	}
}
