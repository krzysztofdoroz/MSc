package pl.edu.agh.msc.node.agent;

import java.util.List;

public interface IAgent {
	
	void die(List<IAgent> population);
	void seekAndGet(List<IAgent> population);
	double give();
	void get(double acquiredResource);
	boolean accept(IAgent agent);
	IAgent seekPartner(List<IAgent> population);
	int getId();
	double getExpectedReturn();
	double getRisk();
}
