package pl.edu.agh.msc.node.agent;

import java.util.List;

import pl.edu.agh.msc.generic.genetic.algorithm.MPTPortfolio;
import pl.edu.agh.msc.node.agent.impl.Agent;

public interface IAgent {
	
	int getSpecies();
	void seekAndGet(List<? extends IAgent> population);
	double give();
	void get(double acquiredResource);
	List<? extends IAgent> accept(Agent mate);
	IAgent seekPartner(List<? extends IAgent> population);
	double getExpectedReturn();
	double getRisk();
	double getResource();
	MPTPortfolio getPortfolio();
}
