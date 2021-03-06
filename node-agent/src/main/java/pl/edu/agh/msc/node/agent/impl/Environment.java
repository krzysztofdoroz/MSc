package pl.edu.agh.msc.node.agent.impl;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
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
	private BufferedWriter output;

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

		try {
			output = new BufferedWriter(
					new FileWriter(new File("pareto_front")));
		} catch (IOException e) {
			System.out.println("EXCEPTION!!!");
		}
		
		initPopulations(populationSize);
	}

	public double calculateGlobalResource() {
		double currentResource = 0.0;

		for (Agent agent : returnOrientedPopulation) {
			currentResource += agent.getResource();
		}

		for (Agent agent : riskOrientedPopulation) {
			currentResource += agent.getResource();
		}

		return currentResource;
	}

	public void redistributeFreeResource() {

		if (calculateGlobalResource() < totalResource) {
			int totalPopulationSize = riskOrientedPopulation.size()
					+ returnOrientedPopulation.size();
			double ammountOfResourcePerAgent = freeResource
					/ totalPopulationSize;

			for (Agent agent : riskOrientedPopulation) {
				agent.setResource(agent.getResource()
						+ ammountOfResourcePerAgent);
			}

			for (Agent agent : returnOrientedPopulation) {
				agent.setResource(agent.getResource()
						+ ammountOfResourcePerAgent);
			}
		}
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

		for (int i = 0; i < populationSize; i += 2) {
			Agent agent = new Agent(FIRST_SPECIES, totalResource
					/ (populationSize), NUMBER_OF_STOCKS,
					REPRODUCTION_THRESHOLD);
			riskOrientedPopulation.add(agent);

			Agent agent2 = new Agent(SECOND_SPECIES, totalResource
					/ (populationSize), NUMBER_OF_STOCKS,
					REPRODUCTION_THRESHOLD);
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
		System.out.println("current env:" + returnOrientedPopulation.size()
				+ " risk:" + riskOrientedPopulation.size());
		double res = 0.0;

		for (Agent agent : returnOrientedPopulation) {
			System.out.println(agent);
			res += agent.getResource();
		}

		for (Agent agent : riskOrientedPopulation) {
			System.out.println(agent);
			res += agent.getResource();
		}
		System.out.println("total REsource:" + res);

		
		
		recalculateRiskAndReturn(day);
		simulateInteractions(3);
		recalculateRiskAndReturn(day);

		try{
			// dump all subpopulations to a file
			for (Agent portfolio : riskOrientedPopulation) {
				output.write(day + " " + portfolio.getRisk()  + " "
						+ portfolio.getExpectedReturn() + "\n");
			}

			for (Agent portfolio : returnOrientedPopulation) {
				output.write(day + " " + portfolio.getRisk()  + " "
						+ portfolio.getExpectedReturn() + "\n");
			}
			} catch (IOException e) {
				System.out.println("EXCEPTION WHILE DUMPING TO A FILE!");
			}
		
		day++;	
		
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

	public Agent getAgentToMigrate() {
		int whichPopulation = Math.abs(rand.nextInt() % 2);
		Agent result = null;

		int index;
		if (whichPopulation == 0) {
			index = Math.abs(rand.nextInt() % riskOrientedPopulation.size());
			result = riskOrientedPopulation.get(index);
			riskOrientedPopulation.remove(index);
		} else {
			index = Math.abs(rand.nextInt() % returnOrientedPopulation.size());
			result = returnOrientedPopulation.get(index);
			returnOrientedPopulation.remove(index);
		}
		return result;
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
					// 60% chance of rivalry between members of the same species
					currentAgent
							.seekAndGet(currentAgent.getSpecies() == FIRST_SPECIES ? riskOrientedPopulation
									: returnOrientedPopulation);
					break;
				case 6:
				case 7:
					// finding mate from members of the other species
					IAgent mate = currentAgent
							.seekPartner(currentAgent.getSpecies() == FIRST_SPECIES ? returnOrientedPopulation
									: riskOrientedPopulation);

					if (mate != null) {
						List<Agent> children = currentAgent
								.accept((Agent) mate);

						for (Agent agent : children) {
							if (agent.getSpecies() == FIRST_SPECIES) {
								riskOrientedPopulation.add(agent);
							} else {
								returnOrientedPopulation.add(agent);
							}
						}
					}
					break;
				case 8:
					// mutation
					currentAgent.accept(null);
				case 9:
					// migration
					// NodeAgentImpl takes care of migration...
				}
			}
			extinctTheWeakest();
			redistributeFreeResource();
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
							i, j, day);
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

	public double getFreeResource() {
		return freeResource;
	}

	public void setFreeResource(double freeResource) {
		this.freeResource = freeResource;
	}

	public void acceptMigrant(Agent migrant) {
		if (migrant.getSpecies() == FIRST_SPECIES) {
			riskOrientedPopulation.add(migrant);
		} else {
			returnOrientedPopulation.add(migrant);
		}
	}

}
