package pl.edu.agh.msc.SimpleGeneticAlgo;

import org.junit.Test;
import static org.junit.Assert.*;

import pl.edu.agh.msc.SimpleGeneticAlgoImpl.GeneticAlgorithmImpl;;

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
	
}
