package pl.edu.agh.msc.node.agent.impl;

import java.util.Iterator;
import java.util.List;

import pl.edu.agh.msc.generic.genetic.algorithm.MPTPortfolio;
import pl.edu.agh.msc.node.agent.IAgent;

public class Agent implements IAgent {

	private int id;
	private double risk;
	private double expectedReturn;
	private double resource;
	private MPTPortfolio portfolio;
	private int specie;

	public Agent(int id, double resource){
		this.id = id;
		this.resource = resource;
	}
	
	public void die(List<IAgent> population) {
		Iterator<IAgent> iter = population.iterator();
		
		while(iter.hasNext()){
			if(iter.next().getId() == getId()){
				iter.remove();
			}
		}
	}

	public void get(double acquiredResource) {
		setResource(getResource() + acquiredResource); 
	}
	
	public boolean accept(IAgent agent) {
		// TODO Auto-generated method stub
		return false;
	}

	public IAgent seekPartner(List<IAgent> population) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public void seekAndGet(List<IAgent> population) {
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
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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
		builder.append("id:");
		builder.append(getId());
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

	public int getSpecie() {
		return specie;
	}

	public void setSpecie(int specie) {
		this.specie = specie;
	}
}
