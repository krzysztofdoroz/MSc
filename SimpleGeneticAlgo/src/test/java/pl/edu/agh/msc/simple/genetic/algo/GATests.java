package pl.edu.agh.msc.simple.genetic.algo;

import org.junit.Test;

import pl.edu.agh.msc.generic.genetic.algorithm.Portfolio;
import pl.edu.agh.msc.simple.genetic.algoImpl.GeneticAlgorithmImpl;

public class GATests {

	@Test
	public void testGA(){
		System.out.println("GA test:");
		GeneticAlgorithmImpl gen = new GeneticAlgorithmImpl(2,16, 0.2, 0.1, 0.3);
		
		for(int i = 0; i < 4 ; i++){
			Portfolio bestPortfolio = gen.calculateCurrentPortfolio();

			System.out.println("      CURRENT POPULATION STATE:  ");
			
			for(Portfolio portfolio : gen.getPopulation()){
				System.out.println(portfolio);
			}
			System.out.println("  BEST PORTFOLIO:");
			System.out.println(bestPortfolio);
			System.out.println("----------------------------------------------------");
		}
	}
	
	
}
