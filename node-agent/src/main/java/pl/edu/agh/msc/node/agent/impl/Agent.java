package pl.edu.agh.msc.node.agent.impl;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import pl.edu.agh.msc.generic.genetic.algorithm.MPTPortfolio;
import pl.edu.agh.msc.node.agent.IAgent;

public class Agent implements IAgent, Serializable {

	private static final long serialVersionUID = 1L;
	
	private double risk;
	private double expectedReturn;
	private double resource;
	private MPTPortfolio portfolio;
	private int species;
	private final int NUMBER_OF_STOCKS;
	private final double REPRODUCTION_THRESHOLD;
	private final static double REPRODUCTION_RESOURCE_TRANSFER = 0.2;

	public Agent(int species, double resource, int numberOfStocks, double reproductionThreshold){
		this.species = species;
		this.resource = resource;
		this.NUMBER_OF_STOCKS = numberOfStocks;
		portfolio = new MPTPortfolio(NUMBER_OF_STOCKS);
		REPRODUCTION_THRESHOLD = reproductionThreshold;
	}

	public void get(double acquiredResource) {
		setResource(getResource() + acquiredResource); 
	}
	
	public List<Agent> accept(Agent mate) {
		if (mate != null){
			//regular mating
			mate.setResource(mate.getResource() - REPRODUCTION_RESOURCE_TRANSFER);
			setResource(getResource() - REPRODUCTION_RESOURCE_TRANSFER);
			
			List<Agent> result = new LinkedList<Agent>();
			
			List<MPTPortfolio> childrenPortfolios = AgentUtils.crossoverWithGenomeSwapping(getPortfolio(), mate.getPortfolio());
			
			Agent childA = new Agent(species, REPRODUCTION_RESOURCE_TRANSFER, NUMBER_OF_STOCKS, REPRODUCTION_THRESHOLD);
			childA.setPortfolio(childrenPortfolios.get(0));
			
			Agent childB = new Agent(species, REPRODUCTION_RESOURCE_TRANSFER, NUMBER_OF_STOCKS, REPRODUCTION_THRESHOLD);
			childB.setPortfolio(childrenPortfolios.get(1));
			
			result.add(childA);
			result.add(childB);
			
			return result;
		} else {
			//mutation
			MPTPortfolio mutant = AgentUtils.mutate(getPortfolio());
			setPortfolio(mutant);
			return null;
		}
	}

	public IAgent seekPartner(List<? extends IAgent> population) {
		for(IAgent agent : population){
			if(agent.getResource() > REPRODUCTION_THRESHOLD){
				return agent;
			}
		}
		return null;
	}
	
	public void seekAndGet(List<? extends IAgent> population) {
		for(IAgent agent : population){
			if(agent.getRisk() > getRisk() && agent.getExpectedReturn() < getExpectedReturn()){
				double acquiredResource = agent.give();
				setResource(getResource() + acquiredResource);
				return;
			}
		}
	}

	public double give() {
		double result = getResource() * 0.2;
		setResource(getResource() * 0.8);
		
		return result;
	}
	
	public double getRisk() {
		return risk;
	}

	public void setRisk(double risk) {
		this.risk = risk;
	}

	public double getExpectedReturn() {
		return expectedReturn;
	}

	public void setExpectedReturn(double expectedReturn) {
		this.expectedReturn = expectedReturn;
	}

	public double getResource() {
		return resource;
	}

	public void setResource(double resource) {
		this.resource = resource;
	}

	@Override
	public String toString(){
		StringBuilder builder = new StringBuilder();
		builder.append("species:");
		builder.append(getSpecies());
		builder.append(", res:");
		builder.append(getResource());
		
		return builder.toString();
	}

	public MPTPortfolio getPortfolio() {
		return portfolio;
	}

	public void setPortfolio(MPTPortfolio portfolio) {
		this.portfolio = portfolio;
	}

	public int getSpecies() {
		return species;
	}

	public void setSpecies(int species) {
		this.species = species;
	}
}
