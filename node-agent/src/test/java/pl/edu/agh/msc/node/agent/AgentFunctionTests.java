package pl.edu.agh.msc.node.agent;

import java.util.LinkedList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;


import pl.edu.agh.msc.node.agent.impl.Agent;

public class AgentFunctionTests{

	
	List<IAgent> population;
	
	
	@Before
	public void init(){
		population = new LinkedList<IAgent>();
	}
	
	@Test
	public void testDying(){
		Agent agent1 = new Agent(1, 0.5);
		Agent agent2 = new Agent(2, 0.4);
		Agent agent3 = new Agent(3, 0.3);
		
		population.add(agent1);
		population.add(agent2);
		population.add(agent3);
		
		agent2.die(population);
		assertEquals(2, population.size());
		
		agent2.die(population);
		assertEquals(2, population.size());
		
		for(IAgent agent : population){
			System.out.println(agent);
		}
		
	}
	
	@Test
	public void testGive(){
		
		Agent agent = new Agent(1, 1.0);
		double result = agent.give();
		
		assertEquals(0.2, result, 0.001);
		assertEquals(0.8, agent.getResource(), 0.001);
	}
	
	@Test
	public void testGet(){
		Agent agent = new Agent(1, 1.0);
		agent.get(2.44);
		
		assertEquals(3.44, agent.getResource(), 0.001);
	}
}
