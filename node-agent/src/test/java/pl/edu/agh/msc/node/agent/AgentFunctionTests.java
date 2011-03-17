package pl.edu.agh.msc.node.agent;

import java.util.LinkedList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import pl.edu.agh.msc.node.agent.impl.Agent;
import pl.edu.agh.msc.node.agent.impl.AgentUtils;
import pl.edu.agh.msc.node.agent.impl.Environment;

public class AgentFunctionTests {

	List<IAgent> population;
	List<Agent> riskPopulation;
	List<Agent> returnPopulation;
	private static final double REPRODUCTION_THRESHOLD = 0.5;

	@Before
	public void init() {
		population = new LinkedList<IAgent>();
		riskPopulation = new LinkedList<Agent>();
		returnPopulation = new LinkedList<Agent>();
	}

	@Test
	public void testDying() {
		Agent agent1 = new Agent(1, 0.02, 2, REPRODUCTION_THRESHOLD);
		Agent agent2 = new Agent(2, 0.1, 2, REPRODUCTION_THRESHOLD);
		Agent agent3 = new Agent(3, 0.03, 2, REPRODUCTION_THRESHOLD);

		riskPopulation.add(agent1);
		riskPopulation.add(agent2);
		returnPopulation.add(agent3);

		Environment env = new Environment(1.2, 4, 2, 0.1,
				REPRODUCTION_THRESHOLD, null);

		env.setReturnOrientedPopulation(returnPopulation);
		env.setRiskOrientedPopulation(riskPopulation);
		env.extinctTheWeakest();
		
		assertEquals(1, riskPopulation.size());
		assertEquals(0, returnPopulation.size());

		for (IAgent agent : riskPopulation) {
			System.out.println(agent);
		}
	}

	@Test
	public void testGive() {
		Agent agent = new Agent(1, 1.0, 2, REPRODUCTION_THRESHOLD);
		double result = agent.give();

		assertEquals(0.2, result, 0.001);
		assertEquals(0.8, agent.getResource(), 0.001);
	}

	@Test
	public void testGet() {
		Agent agent = new Agent(1, 1.0, 2, REPRODUCTION_THRESHOLD);
		agent.get(2.44);

		assertEquals(3.44, agent.getResource(), 0.001);
	}

	@Test
	public void testSeekAndGet() {
		System.out.println("seek and get test:");

		Agent agent1 = new Agent(1, 0.5, 2, REPRODUCTION_THRESHOLD);
		agent1.setRisk(0.1);
		agent1.setExpectedReturn(1.1);

		Agent agent2 = new Agent(2, 1.0, 2, REPRODUCTION_THRESHOLD);
		agent2.setRisk(0.2);
		agent2.setExpectedReturn(0.4);

		Agent agent3 = new Agent(3, 0.3, 2, REPRODUCTION_THRESHOLD);
		agent3.setRisk(0.1);
		agent3.setExpectedReturn(1.1);

		population.add(agent1);
		population.add(agent2);
		population.add(agent3);

		agent1.seekAndGet(population);

		assertEquals(0.7, agent1.getResource(), 0.001);

		for (IAgent agent : population) {
			System.out.println(agent);
		}
	}

	@Test
	public void testNonDominatedSolution() {
		System.out.println("nondominated solution test:");

		Agent agent1 = new Agent(1, 0.5, 2, REPRODUCTION_THRESHOLD);
		agent1.setRisk(0.1);
		agent1.setExpectedReturn(1.1);

		Agent agent2 = new Agent(2, 1.0, 2, REPRODUCTION_THRESHOLD);
		agent2.setRisk(0.2);
		agent2.setExpectedReturn(0.4);

		Agent agent3 = new Agent(3, 0.3, 2, REPRODUCTION_THRESHOLD);
		agent3.setRisk(0.1);
		agent3.setExpectedReturn(1.12);

		Agent agent4 = new Agent(4, 0.3, 2, REPRODUCTION_THRESHOLD);
		agent4.setRisk(0.15);
		agent4.setExpectedReturn(1.125);

		riskPopulation.add(agent1);
		riskPopulation.add(agent2);
		returnPopulation.add(agent3);

		Environment env = new Environment(100.0, 4, 2, 0.1,
				REPRODUCTION_THRESHOLD, null);

		env.setReturnOrientedPopulation(returnPopulation);
		env.setRiskOrientedPopulation(riskPopulation);

		Agent result = env.findNonDominatedSolution();
		assertEquals(0.1, result.getRisk(), 0.001);
		assertEquals(1.12, result.getExpectedReturn(), 0.001);
	}

	@Test
	public void testRandomPopulationInit() {
		System.out.println("random population init test:");

		Environment env = new Environment(100.0, 4, 2, 0.1,
				REPRODUCTION_THRESHOLD, null);

		for (Agent agent : env.getReturnOrientedPopulation()) {
			assertTrue(agent.getPortfolio().getPortfolio().get(0) > 0.0);
			assertTrue(agent.getPortfolio().getPortfolio().get(1) > 0.0);
		}

		for (Agent agent : env.getRiskOrientedPopulation()) {
			assertTrue(agent.getPortfolio().getPortfolio().get(0) > 0.0);
			assertTrue(agent.getPortfolio().getPortfolio().get(1) > 0.0);
		}

	}

	@Test
	public void testExtinction() {
		System.out.println("extinction test:");

		Agent agent1 = new Agent(1, 0.5, 2, REPRODUCTION_THRESHOLD);
		agent1.setRisk(0.1);
		agent1.setExpectedReturn(1.1);

		Agent agent2 = new Agent(2, 1.0, 2, REPRODUCTION_THRESHOLD);
		agent2.setRisk(0.2);
		agent2.setExpectedReturn(0.4);

		Agent agent3 = new Agent(3, 0.3, 2, REPRODUCTION_THRESHOLD);
		agent3.setRisk(0.1);
		agent3.setExpectedReturn(1.12);

		Agent agent4 = new Agent(4, 0.3, 2, REPRODUCTION_THRESHOLD);
		agent4.setRisk(0.15);
		agent4.setExpectedReturn(1.125);

		riskPopulation.add(agent1);
		riskPopulation.add(agent2);
		returnPopulation.add(agent3);
		returnPopulation.add(agent4);

		Environment env = new Environment(100.0, 4, 2, 0.4,
				REPRODUCTION_THRESHOLD, null);

		env.setReturnOrientedPopulation(returnPopulation);
		env.setRiskOrientedPopulation(riskPopulation);
		env.extinctTheWeakest();

		for (Agent agent : env.getReturnOrientedPopulation()) {
			System.out.println(agent);
		}

		assertEquals(2, env.getRiskOrientedPopulation().size());
		assertEquals(0, env.getReturnOrientedPopulation().size());
	}

	@Test
	public void testMutation() {
		System.out.println("mutation test:");
		Agent agent = new Agent(1, 1.0, 2, REPRODUCTION_THRESHOLD);

		agent.getPortfolio().getPortfolio().set(0, 0.1);
		agent.getPortfolio().getPortfolio().set(1, 0.9);

		assertEquals(0.1, agent.getPortfolio().getPortfolio().get(0), 0.001);
		assertEquals(0.9, agent.getPortfolio().getPortfolio().get(1), 0.001);

		agent.setPortfolio(AgentUtils.mutate(agent.getPortfolio()));

		assertTrue(agent.getPortfolio().getPortfolio().get(0) > 0.0
				&& agent.getPortfolio().getPortfolio().get(0) < 1.02);
		assertTrue(agent.getPortfolio().getPortfolio().get(1) > 0.0
				&& agent.getPortfolio().getPortfolio().get(1) < 1.0);
		System.out.println(agent.getPortfolio().getPortfolio());
		System.out.println("mutation test: OK");
	}
}
