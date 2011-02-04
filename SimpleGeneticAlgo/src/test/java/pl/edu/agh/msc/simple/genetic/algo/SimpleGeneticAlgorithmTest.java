package pl.edu.agh.msc.simple.genetic.algo;

import org.junit.Test;
import static org.junit.Assert.*;

import pl.edu.agh.msc.generic.genetic.algorithm.Portfolio;
import pl.edu.agh.msc.simple.genetic.algoImpl.GeneticAlgorithmImpl;

public class SimpleGeneticAlgorithmTest {

	@Test
	public void testRandomInit() {
		GeneticAlgorithmImpl gen = new GeneticAlgorithmImpl(2,2);
		
		assertEquals(2,gen.getPopulationSize());
		assertEquals(2,gen.getNumberOfStocks());
		assertEquals(2,gen.getPopulation().get(0).getSize());
		
		for(double d : gen.getPopulation().get(0).getPortfolio()){
			System.out.println(d);
		}
	}
	
	@Test
	public void testFitnessFunctionCalculation(){
		
		GeneticAlgorithmImpl gen = new GeneticAlgorithmImpl(2,2);
		
		Portfolio portfolio = new Portfolio(2);
		portfolio.getPortfolio().set(0, 0.5);
		portfolio.getPortfolio().set(1, 0.5);
		
		System.out.println(GeneticAlgorithmImpl.calculateFitness(portfolio, 0));
		assertEquals(1.0, GeneticAlgorithmImpl.calculateFitness(portfolio, 0), 0.01);
	}
	
	@Test
	public void testCrossover(){
		Portfolio parentA = new Portfolio(2);
		parentA.getPortfolio().set(0, 0.5);
		parentA.getPortfolio().set(1, 0.5);
		
		Portfolio parentB = new Portfolio(2);
		parentB.getPortfolio().set(0, 1.0);
		parentB.getPortfolio().set(1, 0.0);
		
		Portfolio child = GeneticAlgorithmImpl.crossover(parentA, parentB);
		
		assertEquals(0.75, child.getPortfolio().get(0), 0.001);
		assertEquals(0.25, child.getPortfolio().get(1), 0.001);
	}
	
	@Test
	public void testSelectingBestPortfolio(){
		GeneticAlgorithmImpl gen = new GeneticAlgorithmImpl(2,2);
		
		for(int i = 0; i < 4 ; i++){
			Portfolio bestPortfolio = gen.getBestPortfolio(i);
			System.out.println(bestPortfolio);
		}
	}
	
	@Test
	public void testgeneticAlgo(){
		System.out.println("GA test:");
		GeneticAlgorithmImpl gen = new GeneticAlgorithmImpl(2,8);
		
		for(int i = 0; i < 4 ; i++){
			Portfolio bestPortfolio = gen.calculateCurrentPortfolio();
			System.out.println(bestPortfolio);
		}
	}
	
	@Test
	public void testMutation(){
		Portfolio portfolio = new Portfolio(3);
		portfolio.getPortfolio().set(0, 0.25);
		portfolio.getPortfolio().set(1, 0.5);
		portfolio.getPortfolio().set(2, 0.25);
		
		GeneticAlgorithmImpl.mutate(portfolio);
		System.out.println(portfolio);
	}
	
}
