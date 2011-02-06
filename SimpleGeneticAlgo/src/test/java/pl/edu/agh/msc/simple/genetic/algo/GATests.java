package pl.edu.agh.msc.simple.genetic.algo;

import java.util.LinkedList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import pl.edu.agh.msc.data.source.StockDataSource;
import pl.edu.agh.msc.data.source.interfaces.IDataSource;
import pl.edu.agh.msc.generic.genetic.algorithm.Portfolio;
import pl.edu.agh.msc.simple.genetic.algoImpl.GeneticAlgorithmImpl;

public class GATests {

	List<String> filenames;
	IDataSource dataSource;

	@Before
	public void init(){
		filenames =  new LinkedList<String>();
		filenames.add("/home/krzysztof/MSc/data-source/kghm.data");
		filenames.add("/home/krzysztof/MSc/data-source/tpsa.data");
		dataSource = new StockDataSource(filenames, 253);
	}
	
	@Test
	public void testGA(){
		
		double money = 100.0;
		
		System.out.println("GA test:");
		GeneticAlgorithmImpl gen = new GeneticAlgorithmImpl(2,16, 0.2, 0.1, 0.3, dataSource);
		
		for(int i = 0; i < 253 ; i++){
			Portfolio bestPortfolio = gen.calculateCurrentPortfolio();

			System.out.println("      CURRENT POPULATION STATE:  ");
			
			for(Portfolio portfolio : gen.getPopulation()){
			//	System.out.println(portfolio);
			}
			System.out.println("  BEST PORTFOLIO:");

			money *= bestPortfolio.getValue();
			System.out.println(bestPortfolio);
			System.out.println("TOTAL MONEY:" + money);
			
			System.out.println("----------------------------------------------------");
		}
	}
}
