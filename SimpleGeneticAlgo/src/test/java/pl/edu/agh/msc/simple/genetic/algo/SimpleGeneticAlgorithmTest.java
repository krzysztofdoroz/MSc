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
		
		System.out.println(gen.calculateFitness(portfolio, 0));
		assertEquals(100.0, gen.calculateFitness(portfolio, 0), 0.01);
		
	}
	
}
