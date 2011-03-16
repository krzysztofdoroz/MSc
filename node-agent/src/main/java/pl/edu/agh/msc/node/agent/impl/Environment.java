package pl.edu.agh.msc.node.agent.impl;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import pl.edu.agh.msc.data.source.interfaces.IDataSource;
import pl.edu.agh.msc.generic.genetic.algorithm.MPTPortfolio;
import pl.edu.agh.msc.generic.genetic.algorithm.Portfolio;
import pl.edu.agh.msc.node.agent.IAgent;

public class Environment {

	private List<Agent> riskOrientedPopulation;
	private List<Agent> returnOrientedPopulation;
	private double totalResource;
	private double freeResource;
	private static final int FIRST_SPECIES = 1;
	private static final int SECOND_SPECIES = 2;
	private final int NUMBER_OF_STOCKS;
	private final double DYING_THRESHOLD;
	private IDataSource stockDataSource;
	private int day = 0;
	private Random rand = new Random();
	private double REPRODUCTION_THRESHOLD;

	public Environment(double totalResource, int populationSize,
			int numberOfStocks, double dyingThreshold,
			double reproductionThreshold, IDataSource dataSource) {
		riskOrientedPopulation = new LinkedList<Agent>();
		returnOrientedPopulation = new LinkedList<Agent>();
		this.totalResource = totalResource;
		this.stockDataSource = dataSource;
		this.NUMBER_OF_STOCKS = numberOfStocks;
		this.DYING_THRESHOLD = dyingThreshold;
		this.REPRODUCTION_THRESHOLD = reproductionThreshold;

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
			Agent agent = new Agent(id++, totalResource / (populationSize),
					NUMBER_OF_STOCKS, REPRODUCTION_THRESHOLD);
			agent.setSpecies(FIRST_SPECIES);
			riskOrientedPopulation.add(agent);

			Agent agent2 = new Agent(id++, totalResource / (populationSize),
					NUMBER_OF_STOCKS, REPRODUCTION_THRESHOLD);
			agent2.setSpecies(SECOND_SPECIES);
			returnOrientedPopulation.add(agent2);
		}

		randomPopulationInit(populationSize / 2);
	}

	private void randomPopulationInit(int populationSize) {
		Random rand = new Random();
		double sum = 0.0;

		for (int i = 0; i < populationSize; i++) {

			sum = 0.0;
			for (int j = 0; j < NUMBER_OF_STOCKS; j++) {
				double randomStockPart = rand.nextDouble();
				sum += randomStockPart;

				riskOrientedPopulation.get(i).getPortfolio().getPortfolio()
						.set(j, randomStockPart);
				returnOrientedPopulation.get(i).getPortfolio().getPortfolio()
						.set(j, randomStockPart);
			}

			// now we have to normalize the result
			for (int j = 0; j < NUMBER_OF_STOCKS; j++) {
				riskOrientedPopulation
						.get(i)
						.getPortfolio()
						.getPortfolio()
						.set(j,
								riskOrientedPopulation.get(i).getPortfolio()
										.getPortfolio().get(j)
										/ sum);
				returnOrientedPopulation
						.get(i)
						.getPortfolio()
						.getPortfolio()
						.set(j,
								returnOrientedPopulation.get(i).getPortfolio()
										.getPortfolio().get(j)
										/ sum);
			}
		}
	}

	public MPTPortfolio getBestAgentPortfolio() {
		recalculateRiskAndReturn(day++);
		simulateInteractions(3);

		return findNonDominatedSolution().getPortfolio();
	}

	public void extinctTheWeakest() {

		for (Iterator<Agent> iter = riskOrientedPopulation.iterator(); iter
				.hasNext();) {
			Agent agent = iter.next();
			if (agent.getResource() < DYING_THRESHOLD) {
				freeResource += agent.getResource();
				iter.remove();
			}
		}

		for (Iterator<Agent> iter = returnOrientedPopulation.iterator(); iter
				.hasNext();) {
			Agent agent = iter.next();
			if (agent.getResource() < DYING_THRESHOLD) {
				freeResource += agent.getResource();
				iter.remove();
			}
		}
	}

	private void simulateInteractions(int numberOfRounds) {

		int behaviourStrategy;
		List<Agent> allAgents = new LinkedList<Agent>();
		allAgents.addAll(riskOrientedPopulation);
		allAgents.addAll(returnOrientedPopulation);
		Agent currentAgent;

		for (int round = 0; round < numberOfRounds; round++) {
			for (int i = 0; i < allAgents.size(); i++) {

				behaviourStrategy = Math.abs(rand.nextInt() % 10);
				currentAgent = allAgents.get(i);

				switch (behaviourStrategy) {
				case 0:
				case 1:
				case 2:
				case 3:
				case 4:
				case 5:
					//60% chance of rivalry between members of the same species
					currentAgent.seekAndGet(currentAgent.getSpecies() == FIRST_SPECIES ? riskOrientedPopulation 
							: returnOrientedPopulation );
					break;
				case 6:
				case 7:
					//finding mate from members of the other species
					IAgent mate = currentAgent
							.seekPartner(currentAgent.getSpecies() == FIRST_SPECIES ? returnOrientedPopulation
									: riskOrientedPopulation);
					currentAgent.accept((Agent)mate);
					break;
				case 8:
					//mutation
					currentAgent.accept(null);
				case 9:
					//migration
				}
			}
			extinctTheWeakest();
		}
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
